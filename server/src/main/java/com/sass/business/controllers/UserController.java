package com.sass.business.controllers;

import com.sass.business.dtos.users.LogInDTO;
import com.sass.business.dtos.users.UserDTO;
import com.sass.business.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users list received correctly"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Sign up/create a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user created successfully"),
            @ApiResponse(responseCode = "400", description = "invalid request")
    })
    @PostMapping("signup")
    public ResponseEntity<UserDTO> createUser(
            @RequestBody UserDTO userDTO
    ) {
        UserDTO newUser = userService.createUser(userDTO);
        return ResponseEntity.ok(newUser);
    }

    @Operation(summary = "Log in a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user logged in successfully"),
            @ApiResponse(responseCode = "400", description = "invalid request")
    })
    @PostMapping("login")
    public ResponseEntity<String> logInUser(
            @RequestBody LogInDTO logInDTO
    ) {
        String jwt = userService.logIn(logInDTO);
        return ResponseEntity.ok(jwt);
    }

    /*@Operation(summary = "Log out a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user logged out successfully"),
            @ApiResponse(responseCode = "400", description = "invalid request")
    })
    @PostMapping("logout")
    public ResponseEntity<UserDTO> logOutUser(
            @RequestBody UserDTO userDTO
    ) {
        UserDTO newUser = userService.logOut(userDTO);
        return ResponseEntity.ok(newUser);
    }*/
}
