package com.sass.business.services;

import com.sass.business.dtos.APIResponse;
import com.sass.business.dtos.users.LogInDTO;
import com.sass.business.dtos.users.UserDTO;
import com.sass.business.exceptions.APIResponseException;
import com.sass.business.mappers.UserMapper;
import com.sass.business.models.User;
import com.sass.business.repositories.UserRepository;
import com.sass.business.others.AuthUtil;
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
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private AuthUtil authUtil;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            AuthUtil authUtil
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authUtil = authUtil;
    }

    // endregion

    // region SERVICE METHODS

    public APIResponse<List<UserDTO>> getUsers() {
        APIResponse<List<UserDTO>> apiResponse;
        List<User> users;
        List<UserDTO> usersDTO;

        try {
            users = userRepository.findAll();
            usersDTO = users.stream()
                    .map(userMapper::toDTO)
                    .collect(Collectors.toList());

            apiResponse = new APIResponse<>(
                    HttpStatus.OK.value(),
                    "Success!",
                    usersDTO
            );
        } catch (APIResponseException exception) {
            apiResponse = new APIResponse<>(
                    exception.getHttpStatus(),
                    exception.getMessage()
            );
        } catch (Exception exception) {
            apiResponse = new APIResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "There's a server error"
            );
        }

        return apiResponse;
    }

    public APIResponse<Void> signUp(UserDTO userDTO) {
        APIResponse<Void> apiResponse;
        User user;

        try {
            if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
                throw new APIResponseException("A user with this email already exists", HttpStatus.CONFLICT.value());
            }

            user = userMapper.toModel(userDTO);
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            userRepository.save(user);

            apiResponse = new APIResponse<>(
                    HttpStatus.OK.value(),
                    "Success!"
            );
        } catch (APIResponseException exception) {
            apiResponse = new APIResponse<>(
                    exception.getHttpStatus(),
                    exception.getMessage()
            );
        } catch (Exception exception) {
            apiResponse = new APIResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "There's a server error"
            );
        }

        return apiResponse;
    }

    public APIResponse<String> logIn(LogInDTO logInDTO, HttpServletResponse response) {
        APIResponse<String> apiResponse;
        User user;
        Optional<User> userByEmail;
        String token;
        ResponseCookie cookie;

        try {
            userByEmail = userRepository.findByEmail(logInDTO.getEmail());

            if (userByEmail.isEmpty()) {
                throw new APIResponseException("The user doesn't exist", HttpStatus.NOT_FOUND.value());
            }

            user = userByEmail.get();

            if (!passwordEncoder.matches(logInDTO.getPassword(), user.getPassword())) {
                throw new APIResponseException("The user or the password is incorrect", HttpStatus.UNAUTHORIZED.value());
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            token = authUtil.generateToken(user.getUuid(), user.getEmail(), user.getName());
            cookie = authUtil.generateCookie("auth_user", token);
            response.addHeader("Set-Cookie", cookie.toString());

            apiResponse = new APIResponse<>(
                    HttpStatus.OK.value(),
                    "Success!",
                    token
            );
        } catch (APIResponseException exception) {
            apiResponse = new APIResponse<>(
                    exception.getHttpStatus(),
                    exception.getMessage()
            );
        } catch (Exception exception) {
            apiResponse = new APIResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "There's a server error"
            );
        }

        return apiResponse;
    }

    public APIResponse<Void> isLoggedIn() {
        APIResponse<Void> apiResponse;

        apiResponse = new APIResponse<>(
                HttpStatus.OK.value(),
                "Success!"
        );

        return apiResponse;
    }

    // endregion
}
