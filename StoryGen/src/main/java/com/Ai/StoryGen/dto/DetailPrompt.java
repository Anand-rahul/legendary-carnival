package com.Ai.StoryGen.dto;

public class DetailPrompt {
    public long userId;
    public String chatGuidStr;
    public String prompt;
    public String videoLength;
    public String audienceDemographic;
    public String audiencePersona;
    public String tone;
    public String storyStyle;
    public String emotionalKeywords;
    public String retentionKeywords;
    public String callToAction;

    public DetailPrompt(long userId, String chatGuidStr, String prompt, String videoLength, String audienceDemographic, String audiencePersona, String tone, String storyStyle, String emotionalKeywords, String retentionKeywords, String callToAction) {
        this.userId = userId;
        this.chatGuidStr = chatGuidStr;
        this.prompt = prompt;
        this.videoLength = videoLength;
        this.audienceDemographic = audienceDemographic;
        this.audiencePersona = audiencePersona;
        this.tone = tone;
        this.storyStyle = storyStyle;
        this.emotionalKeywords = emotionalKeywords;
        this.retentionKeywords = retentionKeywords;
        this.callToAction = callToAction;
    }

    public DetailPrompt() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getChatGuidStr() {
        return chatGuidStr;
    }

    public void setChatGuidStr(String chatGuidStr) {
        this.chatGuidStr = chatGuidStr;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(String videoLength) {
        this.videoLength = videoLength;
    }

    public String getAudienceDemographic() {
        return audienceDemographic;
    }

    public void setAudienceDemographic(String audienceDemographic) {
        this.audienceDemographic = audienceDemographic;
    }

    public String getAudiencePersona() {
        return audiencePersona;
    }

    public void setAudiencePersona(String audiencePersona) {
        this.audiencePersona = audiencePersona;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public String getStoryStyle() {
        return storyStyle;
    }

    public void setStoryStyle(String storyStyle) {
        this.storyStyle = storyStyle;
    }

    public String getEmotionalKeywords() {
        return emotionalKeywords;
    }

    public void setEmotionalKeywords(String emotionalKeywords) {
        this.emotionalKeywords = emotionalKeywords;
    }

    public String getRetentionKeywords() {
        return retentionKeywords;
    }

    public void setRetentionKeywords(String retentionKeywords) {
        this.retentionKeywords = retentionKeywords;
    }

    public String getCallToAction() {
        return callToAction;
    }

    public void setCallToAction(String callToAction) {
        this.callToAction = callToAction;
    }
}
