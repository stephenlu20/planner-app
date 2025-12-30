package com.planner.api.service;

import com.planner.api.entity.User;
import com.planner.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String username) {
        User user = new User(username);
        return userRepository.save(user);
    }

    public User getUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User updateUsername(UUID id, String newUsername) {
        User user = getUser(id);
        user.setUsername(newUsername);
        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        User user = getUser(id);
        userRepository.delete(user);
    }
}
