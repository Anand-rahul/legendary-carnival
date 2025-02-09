package com.Ai.StoryGen.Repository;

import com.Ai.StoryGen.Model.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatSessionRepository extends JpaRepository<ChatSession, UUID> {
    Optional<ChatSession> findByGuid(UUID guid);
    List<ChatSession> findChatSessionByUserId(long userId);
}