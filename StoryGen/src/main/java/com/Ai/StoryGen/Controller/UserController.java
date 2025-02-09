package com.Ai.StoryGen.Controller;

import com.Ai.StoryGen.Model.UserDetails;
import com.Ai.StoryGen.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    public UserService userService;

    @PostMapping
    public ResponseEntity<UserDetails> createNewUser(@RequestBody UserDetails userDetails) throws InterruptedException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createNewUser(userDetails));
    }

    @GetMapping
    public ResponseEntity<UserDetails> getUser(@RequestParam String email) {
        return userService.getUser(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<UserDetails> updateUser(@RequestParam String email, @RequestBody UserDetails userDetails) {
        return userService.updateUser(email, userDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<UserDetails> deleteUser(@RequestParam String email) {
        return userService.deleteUser(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
