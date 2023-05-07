package com.example.work.security.services.CRUD;

import com.example.work.models.User;
import com.example.work.repository.RoleRepository;
import com.example.work.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseEntity<?> deleteUser(Long id){
        userRepository.deleteUserRoles(id);
        userRepository.deleteUser(id);
        return ResponseEntity.ok("");
    }

    public void updateUser(Long id, String email, String username){
        if ((email != null && !email.isEmpty()) || (username != null && !username.isEmpty())) {
            userRepository.updateUser(id, username, email);
        } else {
            userRepository.updateUser(id, "", "");
        }
    }

    public List<User> findAll(){
        return userRepository.getAllUsers();
    }

    public User getUser(Long id){
        User user = userRepository.findUserById(id).get();
        return user;
    }

}
