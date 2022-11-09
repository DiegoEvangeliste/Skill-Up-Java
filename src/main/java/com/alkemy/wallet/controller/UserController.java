package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.UserRequestDTO;
import com.alkemy.wallet.dto.UserResponseDTO;
import com.alkemy.wallet.model.AuthenticationRequest;
import com.alkemy.wallet.service.IUserAuthenticationService;
import com.alkemy.wallet.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final IUserService userService;

    private final IUserAuthenticationService userAuthenticationService;


    @PostMapping("/auth/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO user) {
        //final String jwt = jwtTokenUtil.generateToken(user.getEmail());
        return userService.createUser(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        return userAuthenticationService.createAuthenticationToken(authenticationRequest);
    }
}
