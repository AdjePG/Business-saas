package com.sass.business.controllers;

import com.sass.business.dtos.UserDTO;
import com.sass.business.models.User;
import com.sass.business.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
            @ApiResponse(responseCode = "200", description = "users list received correctly"),
            @ApiResponse(responseCode = "400", description = "invalid request")
    })
    @GetMapping()
    public List<User> getUsers() {
        return userService.getUsers();
    }

    /*@Operation(summary = "Create a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user created successfully"),
            @ApiResponse(responseCode = "400", description = "invalid request")
    })
    @PostMapping()
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO newUser = userService.createUser(userDTO);
        return ResponseEntity.ok(newUser);
    }*/
}
