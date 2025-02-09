package com.Ai.StoryGen.dto;

public class SingularChatRequest {
    private long userId;
    private String prompt;

    // Getters and Setters
    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
}
