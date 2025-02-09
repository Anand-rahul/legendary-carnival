package com.Ai.StoryGen.Service;

import com.Ai.StoryGen.Model.UserDetails;
import com.Ai.StoryGen.Repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails createNewUser(UserDetails userDetails) {
        userDetails.setUpdatedAt(LocalDateTime.now());
        userDetails.setCreatedAt(LocalDateTime.now());
        return userRepository.save(userDetails);
    }

    public Optional<UserDetails> getUser(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<UserDetails> updateUser(String email, UserDetails userDetails) {
        return userRepository.findByEmail(email).map(existingUserDetails -> {
            existingUserDetails.setEmail(userDetails.getEmail());
            existingUserDetails.setName(userDetails.getName());
            existingUserDetails.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(existingUserDetails);
        });
    }

    public Optional<UserDetails> deleteUser(String email) {
        return userRepository.findByEmail(email).map(userDetails -> {
            userRepository.delete(userDetails);
            return userDetails;
        });
    }
}