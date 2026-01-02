package com.planner.api.controller;

import com.planner.api.dto.UserResponseDTO;
import com.planner.api.dto.UserCreateRequestDTO;
import com.planner.api.entity.User;
import com.planner.api.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "http://[::1]:5173"})
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @PostMapping
    public UserResponseDTO createUser(@RequestBody UserCreateRequestDTO request) {
        User user = userService.createUser(request.getUsername());
        return toDto(user);
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUser(@PathVariable Long id) {
        return toDto(userService.getUser(id));
    }

    @GetMapping("/username/{username}")
    public UserResponseDTO getUserByUsername(@PathVariable String username) {
        return toDto(userService.getUserByUsername(username));
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUsername(@PathVariable Long id, @RequestParam String username) {
        User updated = userService.updateUsername(id, username);
        return toDto(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    private UserResponseDTO toDto(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername());
    }
}
