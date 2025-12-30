package com.planner.api.controller;

import com.planner.api.dto.UserResponseDTO;
import com.planner.api.entity.User;
import com.planner.api.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponseDTO createUser(@RequestParam String username) {
        User user = userService.createUser(username);
        return toDto(user);
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUser(@PathVariable UUID id) {
        return toDto(userService.getUser(id));
    }

    @GetMapping("/byname/{username}")
    public UserResponseDTO getUserByUsername(@PathVariable String username) {
        return toDto(userService.getUserByUsername(username));
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUsername(@PathVariable UUID id, @RequestParam String username) {
        User updated = userService.updateUsername(id, username);
        return toDto(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    private UserResponseDTO toDto(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername());
    }
}
