package com.shop.eleg.controllers;

import com.shop.eleg.domain.User;
import com.shop.eleg.dto.request.LoginRequestDto;
import com.shop.eleg.dto.request.SignupRequestDto;
import com.shop.eleg.dto.response.LoginResponseDto;
import com.shop.eleg.dto.response.MessageResponse;
import com.shop.eleg.dto.response.SignupResponseDto;
import com.shop.eleg.security.jwt.JwtTokenProvider;
import com.shop.eleg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginDto) {
        try {
            String username = loginDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, loginDto.getPassword()));
            User user = userService.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            String token = jwtTokenProvider.createToken(username, user.getRoles());


            return ResponseEntity.ok(new LoginResponseDto(
                    username,
                    "Bearer_",
                    token
            ));

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto requestDto) {
        if (userService.findByUsername(requestDto.getUsername()) != null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(
                            "User with username: " + requestDto.getUsername()) + " is already registered"
                    );
        }

        //TODO: Check exist by email

        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(requestDto.getPassword());

        User result = userService.register(user);

        return ResponseEntity.ok(new SignupResponseDto(
                result.getUsername(),
                result.getFirstName(),
                result.getLastName(),
                result.getEmail()
        ));
    }
}
