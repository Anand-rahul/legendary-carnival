package com.Ai.StoryGen.Service;

import com.Ai.StoryGen.Model.ChatSession;
import com.Ai.StoryGen.Model.Message;
import com.Ai.StoryGen.Model.SingularChat;
import com.Ai.StoryGen.Model.UserDetails;
import com.Ai.StoryGen.Repository.ChatSessionRepository;
import com.Ai.StoryGen.Repository.MessageRepository;
import com.Ai.StoryGen.Repository.SingularChatRepository;
import com.Ai.StoryGen.Repository.UserRepository;
import com.Ai.StoryGen.dto.AudioSectionJson;
import com.Ai.StoryGen.dto.ContextualChatResponse;
import com.Ai.StoryGen.dto.Sections;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.Instant;
import java.util.*;

@Service
public class AiCompletionService {
    private final WebClient webClient;
    @Autowired
    public ChatSessionRepository chatSessionRepository;
    @Autowired
    public MessageRepository messageRepository;
    @Autowired
    public SingularChatRepository singularChatRepository;
    @Autowired
    public UserRepository userRepository;

    @Value("${spring.ai.openai.base-url}")
    private String groqApiUrl;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.chat.options.model}")
    private String modelName;

    public AiCompletionService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<String> getSingularChat(long userId, String prompt) {
        Map<String, Object> requestBody = Map.of(
                "model", modelName,
                "messages", List.of(
                        Map.of("role", "system", "content", "You are an AI storyteller."),
                        Map.of("role", "user", "content", prompt)
                )
        );

        return sendRequest(requestBody)
                .flatMap(response -> Mono.fromRunnable(() -> {
                    SingularChat singularChat = new SingularChat();
                    singularChat.setUserId(userId);
                    singularChat.setQuestion(prompt);
                    singularChat.setAnswer(response);
                    singularChat.setCreatedAt(Instant.ofEpochSecond(Instant.now().toEpochMilli()));
                    singularChatRepository.save(singularChat);
                }).thenReturn(response));
    }

    public Mono<ContextualChatResponse> getContextualChat(long userId, String chatGuidStr, String prompt) {
        UUID chatGuid = UUID.fromString(chatGuidStr);
        ChatSession chatSession = chatSessionRepository.findByGuid(chatGuid)
                .orElseGet(() -> {
                    ChatSession newSession = new ChatSession();
                    newSession.setGuid(chatGuid);
                    newSession.setUserId(userId);
                    newSession.setTitle("New Chat Session");
                    newSession.setCreatedAt(Instant.now());
                    newSession.setUpdatedAt(Instant.now());
                    return chatSessionRepository.save(newSession);
                });

        List<Message> chatMessages = messageRepository.findByChatGuidOrderByTimestamp(chatGuid);
        List<Map<String, Object>> messages = new ArrayList<>();

        messages.add(Map.of("role", "system", "content", "You are an AI storyteller."));
        for (Message message : chatMessages) {
            messages.add(Map.of("role", message.getMessageType(), "content", message.getMessageText()));
        }
        messages.add(Map.of("role", "user", "content", prompt));

        Map<String, Object> requestBody = Map.of(
                "model", modelName,
                "messages", messages
        );

        return sendRequest(requestBody)
                .flatMap(response -> {
                    Message userMessage = new Message();
                    userMessage.setChatGuid(chatGuid);
                    userMessage.setUserId(userId);
                    userMessage.setMessageType("user");
                    userMessage.setMessageText(prompt);
                    userMessage.setTimestamp(Instant.ofEpochSecond(Instant.now().toEpochMilli()));

                    Message aiMessage = new Message();
                    aiMessage.setChatGuid(chatGuid);
                    aiMessage.setUserId(userId);
                    aiMessage.setMessageType("assistant");
                    aiMessage.setMessageText(response);
                    aiMessage.setTimestamp(Instant.ofEpochSecond(Instant.now().toEpochMilli()));

                    return Mono.fromRunnable(() -> {
                        messageRepository.save(userMessage);
                        messageRepository.save(aiMessage);
                        chatSession.setUpdatedAt(Instant.now());
                        chatSessionRepository.save(chatSession);
                    }).thenReturn(response);
                }).map(response -> new ContextualChatResponse(chatGuidStr, response));
    }
    public Mono<AudioSectionJson> getOutputAsAudioJson(long userId, String chatGuidStr, String prompt) {
        UUID chatGuid = UUID.fromString(chatGuidStr);
        ChatSession chatSession = chatSessionRepository.findByGuid(chatGuid)
                .orElseGet(() -> {
                    ChatSession newSession = new ChatSession();
                    newSession.setGuid(chatGuid);
                    newSession.setUserId(userId);
                    newSession.setTitle("New Chat Session");
                    newSession.setCreatedAt(Instant.now());
                    newSession.setUpdatedAt(Instant.now());
                    return chatSessionRepository.save(newSession);
                });

        List<Message> chatMessages = messageRepository.findByChatGuidOrderByTimestamp(chatGuid);
        List<Map<String, Object>> messages = new ArrayList<>();

        messages.add(Map.of("role", "system", "content", "You are an AI storyteller."));
        for (Message message : chatMessages) {
            messages.add(Map.of("role", message.getMessageType(), "content", message.getMessageText()));
        }
        messages.add(Map.of("role", "user", "content", prompt));

        Map<String, Object> requestBody = Map.of(
                "model", modelName,
                "messages", messages
        );

        return sendRequest(requestBody)
                .flatMap(response -> {
                    Message userMessage = new Message();
                    userMessage.setChatGuid(chatGuid);
                    userMessage.setUserId(userId);
                    userMessage.setMessageType("user");
                    userMessage.setMessageText(prompt);
                    userMessage.setTimestamp(Instant.now());

                    Message aiMessage = new Message();
                    aiMessage.setChatGuid(chatGuid);
                    aiMessage.setUserId(userId);
                    aiMessage.setMessageType("assistant");
                    aiMessage.setMessageText(response);
                    aiMessage.setTimestamp(Instant.now());

                    messageRepository.save(userMessage);
                    messageRepository.save(aiMessage);
                    chatSession.setUpdatedAt(Instant.now());
                    chatSessionRepository.save(chatSession);

                    return Mono.just(response);
                })
                .map(response -> {
                    try {
                        System.out.println(response);
                        ObjectMapper objectMapper = new ObjectMapper();
                        return objectMapper.readValue(response, AudioSectionJson.class);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse AI response to AudioSectionJson", e);
                    }
                });
    }

    private Mono<String> sendRequest(Map<String, Object> requestBody) {
        return webClient.post()
                .uri(groqApiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    try {
                        List<?> choices = (List<?>) response.get("choices");
                        if (choices == null || choices.isEmpty()) {
                            return "No response from AI.";
                        }
                        Map<?, ?> choice = (Map<?, ?>) choices.get(0);
                        Map<?, ?> message = (Map<?, ?>) choice.get("message");
                        return message != null ? (String) message.get("content") : "No response content.";
                    } catch (Exception e) {
                        return "Error parsing AI response: " + e.getMessage();
                    }
                })
                .onErrorResume(error -> Mono.just("Error generating response: " + error.getMessage()));
    }

    public Map<String,String> getGuid(String email) {
        Map<String,String> guidMap = new HashMap<>();
        UserDetails userDetails = userRepository.findByEmail(email).orElse(null);
        if(userDetails !=null){
            long id= userDetails.getId();
            List<ChatSession> chatSessionList=chatSessionRepository.findChatSessionByUserId(id);
            for(var chatSession:chatSessionList){
                guidMap.put(chatSession.getTitle(),chatSession.getGuid().toString());
            }
        }
        return guidMap;
    }
}
