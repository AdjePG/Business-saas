package com.sass.business.services;

import com.sass.business.dtos.users.LogInDTO;
import com.sass.business.dtos.users.UserDTO;
import com.sass.business.mappers.UserMapper;
import com.sass.business.models.User;
import com.sass.business.repositories.UserRepository;
import com.sass.business.others.JWTUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    // region INJECTED DEPENDENCIES

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JWTUtil jwtUtil;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JWTUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // endregion

    // region SERVICE METHODS

    public List<UserDTO> getUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO createUser(UserDTO userDTO) {

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = UserMapper.toModel(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        user = userRepository.save(user);
        return UserMapper.toDTO(user);
    }

    public String logIn(LogInDTO logInDTO) {
        Optional<User> userByEmail = userRepository.findByEmail(logInDTO.getEmail());
        User user;

        if (userByEmail.isEmpty()) {
            return "User not exists";
        }

        user = userByEmail.get();

        if (!passwordEncoder.matches(logInDTO.getPassword(), user.getPassword())) {
            return "Invalid password";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        String jwt = jwtUtil.generateToken(user.getUuid(), user.getEmail(), user.getName());

        return jwt;
    }

    public String logOut(LogInDTO logInDTO) {
        Optional<User> userByEmail = userRepository.findByEmail(logInDTO.getEmail());
        User user;

        if (userByEmail.isEmpty()) {
            return "User not exists";
        }

        user = userByEmail.get();

        if (!passwordEncoder.matches(logInDTO.getPassword(), user.getPassword())) {
            return "Invalid password";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        String jwt = jwtUtil.generateToken(user.getUuid(), user.getEmail(), user.getName());

        return jwt;
    }

    // endregion
}
