package com.Ai.StoryGen.Repository;

import com.Ai.StoryGen.Model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDetails, Long> {
    Optional<UserDetails> findByEmail(String email);
}