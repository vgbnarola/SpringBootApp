package com.narola.controller;

import com.narola.dto.UserDto;
import com.narola.entity.User;
import com.narola.exception.ErrorResponse;
import com.narola.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
@SecurityRequirement(name = "tokenapi")
//@Hidden
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getusers")
    @Tag(name = "03.Get User", description = "The API for Fetch User.")
    @Operation(summary = "Fetch List of All User.",
            responses = {@ApiResponse(responseCode = "200", content = @Content(mediaType = "*/*", array = @ArraySchema(schema = @Schema(implementation = User.class)))),
                    @ApiResponse(responseCode = "400", content = @Content(mediaType = "*/*", array = @ArraySchema(schema = @Schema(implementation = String.class))))})
    public List<User> getUsersList() {
        return userService.getUsers();
    }

    @GetMapping("/getuser/{userName}")
    @Tag(name = "03.Get User", description = "The API for Fetch User.")
    @Operation(summary = "Get user by User Name",
            responses = {@ApiResponse(responseCode = "200", content = @Content(mediaType = "*/*", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "404", content = @Content(mediaType = "*/*", schema = @Schema(implementation = String.class)))})
    public ResponseEntity<?> getUserByName(@PathVariable String userName) {
        User user = userService.getUserByName(userName);
        if (user != null) {
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{userName}")
    @Tag(name = "04.User Operation", description = "The API for User Operation.")
    @Operation(summary = "Update user by User Name",
            responses = {@ApiResponse(responseCode = "200", content = @Content(mediaType = "*/*", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "404", content = @Content(mediaType = "*/*", schema = @Schema(implementation = String.class)))})
    public ResponseEntity<?> updateUserByName(@PathVariable String userName, @RequestBody UserDto userDto) {
        User user = userService.getUserByName(userName);
        if (user != null) {
            user = userService.updateUser(userName, userDto);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{userName}")
    @Tag(name = "04.User Operation", description = "The API for User Operation.")
    @Operation(summary = "Delete user by User Name",
            responses = {@ApiResponse(responseCode = "200", content = @Content(mediaType = "*/*", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "404", content = @Content(mediaType = "*/*", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "*/*", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "*/*", schema = @Schema(implementation = ErrorResponse.class)))})
    @Hidden
    public ResponseEntity<?> deleteUserByName(@Parameter(description = "Provide User name", required = true) @PathVariable String userName) {
        User user = userService.getUserByName(userName);
        if (user != null) {
            userService.deleteUserByName(userName);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
        }
    }
}
