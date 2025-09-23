package com.dileep.JwtAuthenticationAndAuthorization.Configuration.SecurityClass;

import com.dileep.JwtAuthenticationAndAuthorization.Entity.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {
    private final String userName;
    private final String password;
    private final String roles;
    public MyUserDetails(UserInfo userInfo) {
        userName=userInfo.getUsername();
        password= userInfo.getPassword();
        roles= userInfo.getRoles();
    }

    public MyUserDetails(String username, String s, List<String> roles) {
        this.userName=username;
        this.password=s;
        this.roles=roles.stream()
                .map((role)-> role.trim())
                .collect(Collectors.joining(","));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(roles.split(","))
                .map((role)->{
                    role=role.trim();
                    return new SimpleGrantedAuthority(role);
                })
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
