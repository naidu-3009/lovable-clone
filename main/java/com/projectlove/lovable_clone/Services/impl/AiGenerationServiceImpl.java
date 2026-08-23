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
                .map(respone-> Objects.requireNonNull(respone.getResult().getOutput().getText()));



    }

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

        while(matcher.find()){
            String filePath= matcher.group(1);
            String fileContent=matcher.group(2).trim();

            projectFileService.saveFile(projectId,filePath,fileContent);
        }




    }

    private void createChatSessionIfNotExists(Long projectId, Long userId) {
    }
}
