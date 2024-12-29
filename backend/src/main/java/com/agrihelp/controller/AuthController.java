package com.agrihelp.controller;

import com.agrihelp.common.Result;
import com.agrihelp.dto.LoginRequest;
import com.agrihelp.dto.RegisterRequest;
import com.agrihelp.entity.User;
import com.agrihelp.service.UserService;
import com.agrihelp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        
        return Result.success(response);
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);
        return Result.success();
    }
}
