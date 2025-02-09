package com.Ai.StoryGen.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@Entity
public class SingularChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public long userId;

    @Column(columnDefinition = "TEXT")
    public String question;
    @Column(columnDefinition = "TEXT")
    public String answer;

    @CreationTimestamp
    public Instant createdAt;

    public SingularChat(long id, long userId, String question, String answer, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.question = question;
        this.answer = answer;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public SingularChat() {
    }
}