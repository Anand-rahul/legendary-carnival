package com.Ai.StoryGen.dto;

public class ContextualChatRequest {
    private long userId;
    private String prompt;
    private String chatGuidStr;

    // Getters and Setters
    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }

    public String getChatGuidStr() { return chatGuidStr; }
    public void setChatGuidStr(String chatGuidStr) { this.chatGuidStr = chatGuidStr; }
}
