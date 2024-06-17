package com.sass.business.mappers;

import com.sass.business.dtos.UserDTO;
import com.sass.business.models.User;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setPhoto(user.getPhoto());
        userDTO.setPhone(user.getPhone());
        return userDTO;
    }

    public static User toModel(UserDTO userDTO) {
        User user = new User();
        user.setUuid(userDTO.getUuid());
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setPhoto(userDTO.getPhoto());
        user.setPhone(userDTO.getPhone());
        // Note: Password is intentionally not mapped here for security reasons
        return user;
    }
}