package com.Ai.StoryGen.Repository;

import com.Ai.StoryGen.Model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatGuidOrderByTimestamp(UUID chatGuid);
}