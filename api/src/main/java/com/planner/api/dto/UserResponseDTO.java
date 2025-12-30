package com.planner.api.dto;

import java.util.UUID;

public class UserResponseDTO {

    private UUID id;
    private String username;

    public UserResponseDTO(UUID id, String username) {
        this.id = id;
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
