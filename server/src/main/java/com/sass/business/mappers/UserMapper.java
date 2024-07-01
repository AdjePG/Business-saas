package com.sass.business.mappers;

import com.sass.business.dtos.users.UserDTO;
import com.sass.business.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setFirstSurname(user.getFirstSurname());
        userDTO.setLastSurname(user.getLastSurname());
        userDTO.setPhoto(user.getPhoto());
        userDTO.setPhone(user.getPhone());
        return userDTO;
    }

    public User toModel(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setFirstSurname(userDTO.getFirstSurname());
        user.setLastSurname(userDTO.getLastSurname());
        user.setPhoto(userDTO.getPhoto());
        user.setPhone(userDTO.getPhone());
        return user;
    }
}