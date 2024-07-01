package com.sass.business.controllers;

import com.sass.business.dtos.APIResponse;
import com.sass.business.dtos.users.LogInDTO;
import com.sass.business.dtos.users.UserDTO;
import com.sass.business.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {
    // region INJECTED DEPENDENCIES

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // endregion

    // region REQUEST METHODS

    // region GET - GETUSERS
    @Operation(summary = "Get list of users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok status")
    })
    @GetMapping("/")
    public ResponseEntity<APIResponse<List<UserDTO>>> getUsers() {
        APIResponse<List<UserDTO>> apiResponse = userService.getUsers();
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
    }
    // endregion

    // region POST - SIGNUP
    @Operation(summary = "Sign up/create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created status")
    })
    @PostMapping("/signup")
    public ResponseEntity<APIResponse<Void>> signUp(
            @RequestBody UserDTO userDTO
    ) {
        APIResponse<Void> apiResponse = userService.signUp(userDTO);
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
    }
    // endregion

    // region PUT - UPDATEUSER
    @Operation(summary = "Update user details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok status")
    })
    @PutMapping("/")
    public ResponseEntity<APIResponse<Void>> updateUser(
            @RequestHeader("Authorization") String authorization,
            @RequestBody UserDTO userDTO
    ) {
        APIResponse<Void> apiResponse = userService.updateUser(authorization.substring(7), userDTO);
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
    }
    // endregion

    // region DELETE - DELETEUSER
    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content status")
    })
    @DeleteMapping("/")
    public ResponseEntity<APIResponse<Void>> deleteUser(
            @RequestHeader("Authorization") String authorization
    ) {
        APIResponse<Void> apiResponse = userService.deleteUser(authorization.substring(7));
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
    }
    // endregion

    // region POST - LOGIN
    @Operation(summary = "Log in a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok status")
    })
    @PostMapping("login")
    public ResponseEntity<APIResponse<String>> logIn(
            HttpServletResponse response,
            @RequestBody LogInDTO logInDTO
    ) {
        APIResponse<String> apiResponse = userService.logIn(response, logInDTO);
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
    }
    // endregion

    // region GET - ISLOGGEDIN
    @Operation(summary = "Gets if user is logged in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok status")
    })
    @GetMapping("is-logged")
    public ResponseEntity<APIResponse<Void>> isLoggedIn() {
        APIResponse<Void> apiResponse = userService.isLoggedIn();
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(apiResponse.getStatus()));
    }
    // endregion

    // endregion
}
