package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.AiGenerationService;
import com.projectlove.lovable_clone.llm.PromptUtils;
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

import java.util.Map;
import java.util.Objects;
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


     static  Pattern FILE_TAG_PATTERN=  Pattern.compile("<file path=\"([^\"]+)\">(.*?)</file>",Pattern.DOTALL);

    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")//here if we want to pass the arguments from the below method call we use # telling use the parameters passed in below function
    //as parameters
    public Flux<String> streamResponse(String userPrompt, Long projectId) {
            Long userId=authUtil.getCurrentUserId();
            createChatSessionIfNotExists(projectId,userId);

        Map<String,Object> advisorParams =Map.of(
                "userId" ,userId,
                "projectId",projectId
        );


        StringBuilder fullResponseBuffer=new StringBuilder();

        return chatClient.prompt()
                .system(PromptUtils.CODE_GENERATION_SYSTEM_PROMPT)
                .user(userPrompt)
                .advisors(
                        advisorSpec -> {
                            advisorSpec.params(advisorParams);
                        }
                ).stream()
                .chatResponse()
                .doOnNext(response->{
                    String content=response.getResult().getOutput().getText();
                    fullResponseBuffer.append(content);
                })
                .doOnComplete(()->{
                    parseAndSaveFiles(fullResponseBuffer.toString(),projectId);
                })
                .doOnError(error -> log.error( "Error occured during streaming of projectId: {}",projectId ))
                .map(response-> Objects.requireNonNull(response.getResult().getOutput().getText()));

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

    private void createChatSessionIfNotExists(Long projectId, Long userId) {
    }
}
