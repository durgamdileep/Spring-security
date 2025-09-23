package com.dileep.BasicAuthenticationAndAuthorization.Configuartion;

import com.dileep.BasicAuthenticationAndAuthorization.Entity.UserInfo;
import com.dileep.BasicAuthenticationAndAuthorization.Repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Inside MyUserDetailsService class : "+ username);
        UserInfo userInfo= userInfoRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User is not found with : "+ username));
        return new MyUserDetails(userInfo);
    }
}
