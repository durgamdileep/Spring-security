package com.dileep.BasicAuthenticationAndAuthorization.Configuartion;

import com.dileep.BasicAuthenticationAndAuthorization.Entity.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {
    private final String userName;
    private final String password;
    private final String roles;

    MyUserDetails(UserInfo userInfo){
        userName=userInfo.getUsername();
        password=userInfo.getPassword();
        roles= userInfo.getRoles();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  Arrays.stream(roles.split(","))
                .map(String::trim)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

}
