package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.AiGenerationService;
import com.projectlove.lovable_clone.entity.*;
import com.projectlove.lovable_clone.enums.ChatEventType;
import com.projectlove.lovable_clone.enums.MessageRole;
import com.projectlove.lovable_clone.error.ResourceNotFoundException;
import com.projectlove.lovable_clone.llm.PromptUtils;
import com.projectlove.lovable_clone.llm.advisors.FileTreeContextAdvisor;
import com.projectlove.lovable_clone.llm.tools.CodeGenerationTools;
import com.projectlove.lovable_clone.llm.tools.LlmResponseParser;
import com.projectlove.lovable_clone.repository.*;
import com.projectlove.lovable_clone.security.AuthUtil;
import io.jsonwebtoken.security.MalformedKeyException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class AiGenerationServiceImpl implements AiGenerationService {

   ChatClient chatClient;
   AuthUtil authUtil;
   ProjectFileServiceImpl projectFileService;
   FileTreeContextAdvisor fileTreeContextAdvisor;
   ChatSessionRepository chatSessionRepository;
   LlmResponseParser llmResponseParser;
   ProjectRepository projectRepository;
   UserRepository userRepository;
   ChatMessageRepository chatMessageRepository;
   ChatEventRepository chatEventRepository;

   //We could just append our system prompt with our filetreecontext like just append my systemprompt
    //with the file tree context with just like projectfileservice.getfiletree(projectid) and append like normal string
    //but the spring ai convention is that to add what ever you want to add in advisors only
    //if that thing is other than user or system prompt


     static  Pattern FILE_TAG_PATTERN=  Pattern.compile("<file path=\"([^\"]+)\">(.*?)</file>",Pattern.DOTALL);

    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")//here if we want to pass the arguments from the below method call we use # telling use the parameters passed in below function
    //as parameters
    public Flux<String> streamResponse(String userPrompt, Long projectId) {
            Long userId=authUtil.getCurrentUserId();
           ChatSession chatSession= createChatSessionIfNotExists(projectId,userId);

        Map<String,Object> advisorParams =Map.of(
                "userId" ,userId,
                "projectId",projectId
        );


        StringBuilder fullResponseBuffer=new StringBuilder();

        CodeGenerationTools codeGenerationTools=new CodeGenerationTools(projectFileService,projectId);

        AtomicReference<Long> startTime=new AtomicReference<Long>(System.currentTimeMillis());
        AtomicReference<Long> endTime=new AtomicReference<>(0L);

        return chatClient.prompt()
                .system(PromptUtils.CODE_GENERATION_SYSTEM_PROMPT)
                .user(userPrompt)
                .tools(codeGenerationTools)
                .advisors(
                        advisorSpec -> {
                            advisorSpec.params(advisorParams);
                            advisorSpec.advisors(fileTreeContextAdvisor);
                        }
                ).stream()
                .chatResponse()
                .doOnNext(response->{
                    String content=response.getResult().getOutput().getText();
                    if(content!=null && !content.isEmpty()&&endTime.get()==0){ //first non empty chunk we received
                        endTime.set(System.currentTimeMillis());
                    }

                        fullResponseBuffer.append(content);
                })
                .doOnComplete(() -> {
                    long duration=(endTime.get()-startTime.get())/1000;

                    finalizeChats(userPrompt,chatSession,fullResponseBuffer.toString(),duration);

                })
                .doOnError(error ->
                        log.error(
                                "Error occurred during streaming of projectId: {}",
                                projectId,
                                error
                        )
                )                .map(response-> Objects.requireNonNull(response.getResult().getOutput().getText()));

    }


//    Don't think:
//    LLM → XML
//    Think:
//    LLM → TEXT
//    The text can be:
//    Hello
//    or:
//<message>Hello</message>
//    or:
//<file path="App.tsx">...</file>
//    or:
//    {"name":"Nishanth"}
//    or:
//    I am a banana.
//    The model fundamentally generates tokens that become text.
//    Your system prompt influences what text it generates.
//    JSON is the API/transport structure; <file>, <message>, <tool> are merely text generated inside the model's content because your system prompt instructed the model to use those tags.
//{
//  "choices": [
//    {
//      "message": {
//        "content": "<file path="App.tsx">...</file>" -> text but in xml format==>we asked the llm in system prompt to give in this format
//      }
//    }
//  ]
//}



private void finalizeChats(String userMessage, ChatSession chatSession,String fullText,Long duration){
        Long projectId=chatSession.getProject().getId();
   //saving the user message
    chatMessageRepository.save(
            ChatMessage.builder()
                    .chatSession(chatSession)
                    .role(MessageRole.USER)
                    .content(userMessage)
                    .build()
    );

    ChatMessage assistantChatMessage=ChatMessage.builder()
            .role(MessageRole.ASSISTANT)
            .chatSession(chatSession)
            .content("Assistant message here")
            .build();

    chatMessageRepository.save(assistantChatMessage);

    List<ChatEvent> chatEventList= llmResponseParser.parseChatEvents(fullText,assistantChatMessage);
    chatEventList.addFirst(ChatEvent.builder()
                    .type(ChatEventType.THOUGHT)
                    .chatMessage(assistantChatMessage)
                    .content("Thought for "+duration+"s")
                    .sequenceOrder(0)
                    .build());
    chatEventList.stream()
            .filter(e->e.getType()== ChatEventType.FILE_EDIT)
            .forEach(e->projectFileService.saveFile(projectId,e.getFilePath(),e.getContent()));

    chatEventRepository.saveAll(chatEventList);



}





//not in user
    private void parseAndSaveFiles(String fullResponse, Long projectId) {
//            String dummy= """
//                    <message>good sounding verbs related to thinking</message>
//                      <file>
//                      </file>
//                    <message>good sounding verbs related to thinking</message>
//                      <file>
//                      </file>
//                    """;
        Matcher matcher=FILE_TAG_PATTERN.matcher(fullResponse);

//        The value inside content happens to be XML-looking text.
        //remembers where it previously stopped.
//            while (there is another matching <file>):
//            process it

        while(matcher.find()){
            String filePath= matcher.group(1);
            String fileContent=matcher.group(2).trim();

            //group(2) might contain:
            //"\n\nimport React from \"react\";\n\nexport default App;\n\n"
            //Then:
            //.trim()
            //removes whitespace from the beginning and end.
//            "\n\nimport React...\n\n"
//            becomes:
//            "import React..."
//            It does not remove indentation inside the file.

            projectFileService.saveFile(projectId,filePath,fileContent);
        }




    }

    private ChatSession createChatSessionIfNotExists(Long projectId, Long userId) {
        ChatSessionId chatSessionId=new ChatSessionId(projectId,userId);
        ChatSession chatSession=chatSessionRepository.findById(chatSessionId).orElse(null);

        if(chatSession==null){
            Project project=projectRepository.findById(projectId).orElseThrow(()->new ResourceNotFoundException("project",projectId.toString()));
            User user=userRepository.findById(authUtil.getCurrentUserId()).orElseThrow(()->new ResourceNotFoundException("user",userId.toString()));
            ChatSession freshChatSession=ChatSession.builder().project(project).user(user).chatSessionId(chatSessionId).build();
            chatSessionRepository.save(freshChatSession);
            return freshChatSession;
        }

        return chatSession;

    }
}
