package com.dileep.JwtAuthenticationAndAuthorization.Service;


import com.dileep.JwtAuthenticationAndAuthorization.DTO.AuthRequest;
import com.dileep.JwtAuthenticationAndAuthorization.DTO.SignupRequest;
import com.dileep.JwtAuthenticationAndAuthorization.Entity.UserInfo;
import com.dileep.JwtAuthenticationAndAuthorization.Repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private  JWTService jwtService;

    // Save user
    public String saveUser(SignupRequest request) {
        if (userInfoRepository.findByUsername(request.getUsername()).isPresent()) {
            return "Username already exists";
        }
        else {
            UserInfo userInfo= UserInfo.builder()
                    .username(request.getUsername())
                    .password(encoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .roles(request.getRoles())
                            .build();

            userInfoRepository.save(userInfo);
        }
        return request.getUsername()+ " successfully register";
    }

    // Get all users
    public List<UserInfo> getAllUsers() {
        return userInfoRepository.findAll();
    }

    // Get user by ID
    public Optional<UserInfo> getUserById(Long id) {
        return userInfoRepository.findById(id);
    }

    // Get user by username
    public Optional<UserInfo> getUserByUsername(String username) {
        return userInfoRepository.findByUsername(username);
    }

    // Update user
    public UserInfo updateUser(Long id, UserInfo updatedUser) {
        return userInfoRepository.findById(id).map(user -> {
            UserInfo updatedUserInfo= UserInfo.builder()
                    .username(updatedUser.getUsername())
                    .password(updatedUser.getPassword())
                    .email(updatedUser.getEmail())
                    .roles(updatedUser.getRoles())
                            .build();
            return userInfoRepository.save(updatedUserInfo);
        }).orElse(null);
    }

    // Delete user
    public void deleteUser(Long id) {
        userInfoRepository.deleteById(id);
    }


    public String getToken(AuthRequest authRequest) {
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
        if(authentication.isAuthenticated())
           return jwtService.generateToken((UserDetails) authentication.getPrincipal());
        return "fail";
    }
}

