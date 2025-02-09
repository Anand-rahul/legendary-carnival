package com.Ai.StoryGen.Repository;

import com.Ai.StoryGen.Model.SingularChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SingularChatRepository extends JpaRepository<SingularChat, Long> {
}