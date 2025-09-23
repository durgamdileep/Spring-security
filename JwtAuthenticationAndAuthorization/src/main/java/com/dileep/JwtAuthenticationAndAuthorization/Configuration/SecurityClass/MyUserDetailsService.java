package com.dileep.JwtAuthenticationAndAuthorization.Configuration.SecurityClass;

import com.dileep.JwtAuthenticationAndAuthorization.Entity.UserInfo;
import com.dileep.JwtAuthenticationAndAuthorization.Repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private  UserInfoRepository userInfoRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo= userInfoRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User is not found with : "+ username));
        return new MyUserDetails(userInfo);
    }
}
