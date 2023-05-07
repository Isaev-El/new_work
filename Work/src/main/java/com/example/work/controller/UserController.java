package com.example.work.controller;

import com.example.work.models.User;
import com.example.work.security.services.CRUD.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/find-all")
    public List<User> findAll(){
        return userService.findAll();
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User existingUser = userService.getUser(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        } else {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        userService.updateUser(id, user.getEmail(), user.getUsername());
        return ResponseEntity.noContent().build();
    }
}
