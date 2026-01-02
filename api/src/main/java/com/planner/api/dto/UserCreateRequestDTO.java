package com.planner.api.dto;

public class UserCreateRequestDTO {
    private String username;

    public UserCreateRequestDTO() { }

    public UserCreateRequestDTO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
