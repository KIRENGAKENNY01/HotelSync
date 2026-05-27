package com.hotelmanagement.auth.service.impl;

import com.hotelmanagement.auth.dto.JwtResponse;
import com.hotelmanagement.auth.dto.LoginRequest;
import com.hotelmanagement.auth.dto.RegisterRequest;
import com.hotelmanagement.auth.model.entity.User;
import com.hotelmanagement.auth.model.enums.Role;
import com.hotelmanagement.auth.repository.UserRepository;
import com.hotelmanagement.auth.service.AuthService;
import com.hotelmanagement.common.exception.BadRequestException;
import com.hotelmanagement.security.jwt.JwtUtils;
import com.hotelmanagement.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public void registerUser(RegisterRequest signUpRequest) {


        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Error: Email is already in use!");
        }

        // Create new user's account
        Role roleToAssign = Role.CUSTOMER; // Default role
        if (signUpRequest.getRole() != null && signUpRequest.getRole().equalsIgnoreCase("ADMIN")) {
            roleToAssign = Role.ADMIN;
        }

        User user = User.builder()
                .fullName(signUpRequest.getFullName())
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .role(roleToAssign)
                .build();

        userRepository.save(user);
    }

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getFullName(),
                userDetails.getEmail(),
                roles);
    }
}
