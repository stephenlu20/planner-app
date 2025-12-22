package com.planner.api.mapper;

import com.planner.api.dto.user.UserRequestDTO;
import com.planner.api.dto.user.UserResponseDTO;
import com.planner.api.dto.user.UserSummaryDTO;
import com.planner.api.entity.User;

public class UserMapper {

    public static UserResponseDTO toResponseDTO(User user) {
        if (user == null) return null;

        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static UserSummaryDTO toSummaryDTO(User user) {
        if (user == null) return null;

        return UserSummaryDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static User toEntity(UserRequestDTO requestDTO) {
        if (requestDTO == null) return null;

        return new User(requestDTO.getName());
    }

    public static void updateEntity(User user, UserRequestDTO requestDTO) {
        if (user == null || requestDTO == null) return;

        user.setName(requestDTO.getName());
    }
}
