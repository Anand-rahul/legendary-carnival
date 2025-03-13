package com.Ai.StoryGen.Controller;

import com.Ai.StoryGen.Service.AiCompletionService;
import com.Ai.StoryGen.dto.*;
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
    public ResponseEntity<Mono<ContextualChatResponse>> getContextualChat(@RequestBody ContextualChatRequest contextualChatRequest) {
        return ResponseEntity.ok(aiCompletionService.getContextualChat(contextualChatRequest.getUserId(), contextualChatRequest.getChatGuidStr(), contextualChatRequest.getPrompt()));
    }

    @PostMapping("/contextual/detailed")
    public ResponseEntity<Mono<ContextualChatResponse>> getDetailedContextualChat(@RequestBody DetailPrompt detailPromptRequest) {
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

    @PostMapping("/tts")
    public ResponseEntity<Mono<AudioSectionJson>> getListOfPromptsForAudioGen(@RequestBody ContextualChatRequest contextualChatRequest){
        String prePrompt= """
                Go through the given script carefully and perform the following tasks:
                PLease do not add any additional text at start and end of the output , just return the JSON as output
                Return me the Json do not encapsulate with external variables , just JSON
                Identify Focus Areas: Break the script into sections based on context (e.g., introduction, main story, emotional moments, climax, conclusion).
                Assign Speed (Float Value between 0.5 and 2.0):
                Fast-paced sections (1.5 - 2.0) → High-energy moments, action-packed sequences, exciting scenes.
                Medium-paced sections (1.0 - 1.5) → Standard narration, story progression, general descriptions.
                Slow-paced sections (0.5 - 1.0) → Emotional moments, dramatic pauses, deep reflections.
                Enhance the Script: Improve readability by adding necessary punctuation marks (commas, semicolons, ellipses, dashes, etc.) without adding extra words like "(laughs)" or "(sighs)".
                Output in JSON format, with each section containing:
                "text": The improved script segment with added punctuation.
                "speed": A float value between 0.5 and 2.0 representing the pacing.
                """+"Script:"+contextualChatRequest.getPrompt();
        return ResponseEntity.ok(aiCompletionService.getOutputAsAudioJson(contextualChatRequest.getUserId(), contextualChatRequest.getChatGuidStr(), prePrompt));
    }
}
