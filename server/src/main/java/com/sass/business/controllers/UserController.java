package com.sass.business.controllers;

import com.sass.business.dtos.APIResponse;
import com.sass.business.dtos.users.LogInDTO;
import com.sass.business.dtos.users.UserDTO;
import com.sass.business.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {
    // region INJECTED DEPENDENCIES

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // endregion

    // region REQUEST METHODS

    // region GET - GETUSERS
    @Operation(summary = "Get all users", security = @SecurityRequirement(name = "JWT"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users list received correctly"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @GetMapping
    public ResponseEntity<APIResponse<List<UserDTO>>> getUsers(
            @RequestHeader(value = "Authorization") String authorizationHeader,
            @RequestHeader(value = "Content-Type", defaultValue = "application/json") String contentTypeHeader
    ) {
        APIResponse<List<UserDTO>> apiResponse = userService.getUsers();
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
    }
    // endregion

    // region POST - SIGNUP
    @Operation(summary = "Sign up/create a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user created successfully"),
            @ApiResponse(responseCode = "400", description = "invalid request")
    })
    @PostMapping("signup")
    public ResponseEntity<APIResponse<Void>> signUp(
            @RequestBody UserDTO userDTO
    ) {
        APIResponse<Void> apiResponse = userService.signUp(userDTO);
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
    }
    // endregion

    // region POST - LOGIN
    @Operation(summary = "Log in a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user logged in successfully"),
            @ApiResponse(responseCode = "400", description = "invalid request")
    })
    @PostMapping("login")
    public ResponseEntity<APIResponse<String>> logIn(
            @RequestBody LogInDTO logInDTO,
            HttpServletResponse response
    ) {
        APIResponse<String> apiResponse = userService.logIn(logInDTO, response);
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
    }
    // endregion

    // region GET - ISLOGGEDIN
    @Operation(summary = "Gets if user is logged in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user is logged in"),
            @ApiResponse(responseCode = "400", description = "invalid request")
    })
    @GetMapping("is-logged")
    public ResponseEntity<APIResponse<Void>> isLoggedIn() {
        APIResponse<Void> apiResponse = userService.isLoggedIn();
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
    }
    // endregion

    // endregion
}
