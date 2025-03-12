package com.Ai.StoryGen.Controller;

import com.Ai.StoryGen.Service.AiCompletionService;
import com.Ai.StoryGen.dto.ContextualChatRequest;
import com.Ai.StoryGen.dto.DetailPrompt;
import com.Ai.StoryGen.dto.EmailRequest;
import com.Ai.StoryGen.dto.SingularChatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/chat")
public class ChatController {

    @Autowired
    private AiCompletionService aiCompletionService;

    @PostMapping("/singular")
    public ResponseEntity<Mono<String>> getSingularChat(@RequestBody SingularChatRequest request) {
        return ResponseEntity.ok(aiCompletionService.getSingularChat(request.getUserId(), request.getPrompt()));
    }

    @PostMapping("/contextual")
    public ResponseEntity<Mono<String>> getContextualChat(@RequestBody ContextualChatRequest contextualChatRequest) {
        return ResponseEntity.ok(aiCompletionService.getContextualChat(contextualChatRequest.getUserId(), contextualChatRequest.getChatGuidStr(), contextualChatRequest.getPrompt()));
    }

    @PostMapping("/contextual/detailed")
    public ResponseEntity<Mono<String>> getDetailedContextualChat(@RequestBody DetailPrompt detailPromptRequest) {
        String detailedPrompt="The script should be Sophisticated "+detailPromptRequest.getPrompt()+"\n"
                +"The video should be "+detailPromptRequest.getVideoLength()+" minutes long"+"\n"
                +"The Audience Demographic is "+detailPromptRequest.getAudienceDemographic() +"\n"
                +"The Audience Persona is "+detailPromptRequest.getAudiencePersona()+"\n"
                +"The Tone of the script should be "+detailPromptRequest.getTone() +"\n"+
                "The Story Style should be "+detailPromptRequest.getStoryStyle()+"\n"+
                "The Emotional Keywords are "+detailPromptRequest.getEmotionalKeywords()+"\n"+
                "The Retention Keywords are "+detailPromptRequest.getRetentionKeywords()+"\n"+"The Call to Action is "+detailPromptRequest.getCallToAction()+"\n"+
                "Keep the Script in third Person Perspective as if a narrator is explaining the things properly";
        return ResponseEntity.ok(aiCompletionService.getContextualChat(detailPromptRequest.getUserId(), detailPromptRequest.getChatGuidStr(), detailedPrompt));
    }

    @PostMapping("/guid")
    public ResponseEntity<Map<String, String>> getGuidMap(@RequestBody EmailRequest request) {
        return ResponseEntity.ok(aiCompletionService.getGuid(request.getEmail()));
    }
}
