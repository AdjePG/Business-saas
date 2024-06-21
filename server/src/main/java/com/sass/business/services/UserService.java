package com.sass.business.services;

import com.sass.business.dtos.APIResponse;
import com.sass.business.dtos.users.LogInDTO;
import com.sass.business.dtos.users.UserDTO;
import com.sass.business.mappers.UserMapper;
import com.sass.business.models.User;
import com.sass.business.repositories.UserRepository;
import com.sass.business.others.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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

    public APIResponse<List<UserDTO>> getUsers() {
        APIResponse<List<UserDTO>> apiResponse;
        List<User> users = userRepository.findAll();
        List<UserDTO> usersDTO = users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());

        apiResponse = new APIResponse<>(
                HttpStatus.OK.value(),
                "Success!",
                usersDTO
        );

        return apiResponse;
    }

    public APIResponse<Void> signUpUser(UserDTO userDTO) {
        APIResponse<Void> apiResponse;

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            apiResponse = new APIResponse<>(
                    HttpStatus.FOUND.value(),
                    "Email already exists"
            );

            return apiResponse;
        }

        User user = UserMapper.toModel(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userRepository.save(user);

        apiResponse = new APIResponse<>(
                HttpStatus.OK.value(),
                "Success!"
        );

        return apiResponse;
    }

    public APIResponse<String> logIn(LogInDTO logInDTO, HttpServletResponse response) {
        APIResponse<String> apiResponse;
        User user;
        Optional<User> userByEmail;
        String token;

        userByEmail = userRepository.findByEmail(logInDTO.getEmail());

        if (userByEmail.isEmpty()) {
            apiResponse = new APIResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "User not exists"
            );

            return apiResponse;
        }

        user = userByEmail.get();

        if (!passwordEncoder.matches(logInDTO.getPassword(), user.getPassword())) {
            apiResponse = new APIResponse<>(
                    HttpStatus.UNAUTHORIZED.value(),
                    "Invalid password"
            );

            return apiResponse;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        token = jwtUtil.generateToken(user.getUuid(), user.getEmail(), user.getName());

        ResponseCookie cookie = ResponseCookie.from("auth-user", token)
                .httpOnly(true)
                .path("/")
                .maxAge(10000000)
                .sameSite("None")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        apiResponse = new APIResponse<>(
                HttpStatus.OK.value(),
                "Success!",
                token
        );

        return apiResponse;
    }

    // endregion
}
