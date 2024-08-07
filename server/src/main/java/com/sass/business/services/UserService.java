package com.sass.business.services;

import com.sass.business.dtos.APIResponse;
import com.sass.business.dtos.users.LogInDTO;
import com.sass.business.dtos.users.UserDTO;
import com.sass.business.exceptions.APIResponseException;
import com.sass.business.mappers.UserMapper;
import com.sass.business.models.User;
import com.sass.business.others.UuidConverterUtil;
import com.sass.business.repositories.UserRepository;
import com.sass.business.others.AuthUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    // region INJECTED DEPENDENCIES

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtil authUtil;
    private final UuidConverterUtil uuidConverterUtil;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            AuthUtil authUtil,
            UuidConverterUtil uuidConverterUtil
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authUtil = authUtil;
        this.uuidConverterUtil = uuidConverterUtil;
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

    public APIResponse<UserDTO> getUser(UUID uuid) {
        APIResponse<UserDTO> apiResponse;
        Optional<User> userById;
        UserDTO userDTO;

        try {
            userById = userRepository.findById(uuidConverterUtil.uuidToBytes(uuid));

            if (userById.isEmpty()) {
                throw new APIResponseException("User not found", HttpStatus.NOT_FOUND.value());
            }

            userDTO = userMapper.toDTO(userById.get());

            apiResponse = new APIResponse<>(
                    HttpStatus.OK.value(),
                    "Success!",
                    userDTO
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
                throw new APIResponseException("email_exist", HttpStatus.CONFLICT.value());
            }
            
            user = userMapper.toModel(userDTO);
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            userRepository.save(user);

            apiResponse = new APIResponse<>(
                    HttpStatus.CREATED.value(),
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

    public APIResponse<Void> updateUser(String token, UserDTO userDTO) {
        APIResponse<Void> apiResponse;
        UUID uuid;
        Optional<User> userById;
        User user;

        try {
            uuid = UUID.fromString(authUtil.extractClaim(token, "uuid"));
            userById = userRepository.findById(uuidConverterUtil.uuidToBytes(uuid));
            userDTO.setEmail(userById.get().getEmail());
            userDTO.setPassword(userById.get().getPassword());

            user = userMapper.toModel(userById.get(), userDTO);
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

    public APIResponse<Void> deleteUser(String token) {
        APIResponse<Void> apiResponse;
        UUID uuid;
        Optional<User> userById;

        try {
            uuid = UUID.fromString(authUtil.extractClaim(token, "uuid"));
            userRepository.deleteById(uuidConverterUtil.uuidToBytes(uuid));

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

    public APIResponse<String> logIn(HttpServletResponse response, LogInDTO logInDTO) {
        Map<String, Object> claims;
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

            claims = new HashMap<>();
            claims.put("uuid", uuidConverterUtil.binaryToUuid(user.getUuid()));
            claims.put("email", user.getEmail());
            claims.put("name", user.getName());
            claims.put("first_surname", user.getFirstSurname());
            claims.put("last_surname", user.getLastSurname());

            token = authUtil.generateToken(claims, user.getEmail());
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
