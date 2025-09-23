package com.dileep.JwtAuthenticationAndAuthorization.Controller;


import com.dileep.JwtAuthenticationAndAuthorization.DTO.AuthRequest;
import com.dileep.JwtAuthenticationAndAuthorization.DTO.SignupRequest;
import com.dileep.JwtAuthenticationAndAuthorization.Service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/User")
public class UserController {

    @Autowired
    UserInfoService userInfoService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(userInfoService.saveUser(request));
    }

    @PostMapping("/login")
    public String generateToken(@RequestBody  AuthRequest authRequest){
        return  userInfoService.getToken(authRequest);
    }

}
