package com.sass.business.services;

import com.sass.business.dtos.UserDTO;
import com.sass.business.mappers.UserMapper;
import com.sass.business.models.User;
import com.sass.business.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }
    public UserDTO createUser(UserDTO userDTO) {
        User user = UserMapper.toModel(userDTO);
        User savedUser = userRepository.save(user);
        return UserMapper.toDTO(savedUser);
    }
}
