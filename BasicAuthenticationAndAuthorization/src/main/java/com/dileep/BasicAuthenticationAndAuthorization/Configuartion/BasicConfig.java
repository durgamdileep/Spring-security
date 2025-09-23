package com.dileep.BasicAuthenticationAndAuthorization.Configuartion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authorization.AuthorizationDecision;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class BasicConfig {

    /**
     * Fetching UserInfo by UserName from Database and Mapping into UserDetails
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return new MyUserDetailsService();
    }

     /**
     * Authentication by using daoAuthenticationProvider which is default by spring Security
     */
    @Bean
    public AuthenticationManager authenticationManager() throws AuthenticationException {
        System.out.println("Custom AuthenticationManager bean loaded");
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(encoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        return  new ProviderManager(List.of(daoAuthenticationProvider));
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * URL-based Authorization
     * need work on csrf and cors in url- based authorization
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf ->csrf.disable())

                .authorizeHttpRequests(auth ->{
                    auth
                        // recommended format
                        .requestMatchers(HttpMethod.POST, "/api/products").hasAuthority("PRODUCT_CREATE")
                        .requestMatchers(HttpMethod.PUT, "/api/products/update/*").hasAuthority("PRODUCT_UPDATE")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/delete/*").hasAuthority("PRODUCT_DELETE")
                            // .requestMatchers(HttpMethod.GET,"/api/products").hasAuthority("PRODUCT_VIEW")

                        .requestMatchers(HttpMethod.GET, "/api/products/getProduct/*")
                        .access((authentication, context) -> {
                            boolean hasView = authentication.get().getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("PRODUCT_VIEW"));
                            boolean hasUserRole = authentication.get().getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER_SERVICE"));
                            return hasView || hasUserRole
                                    ? new AuthorizationDecision(true)
                                    : new AuthorizationDecision(false);
                        })

                            // normal way not recommended
                            // .requestMatchers("/api/products","/api/products/update/*","/api/products/delete/*").hasRole("admin")
                            // .requestMatchers("/api/products/getProduct/*").hasRole("user")
                         .requestMatchers("/api/User/**").permitAll()
                         .anyRequest().authenticated();
                })
                //  session Management
                .sessionManagement(session->{
                    session.maximumSessions(1);
                })
                // exception
                .formLogin(form -> form
                        .failureHandler((request, response, exception) -> {
                            Throwable cause = exception;
                            while (cause.getCause() != null && cause != cause.getCause()) {
                                cause = cause.getCause();
                            }
                            if (cause instanceof UsernameNotFoundException) {
                                System.out.println("Custom message: " + cause.getMessage());
                            } else {
                                System.out.println("Login failed: " + exception.getMessage());
                            }
                        })
                );
        return http.build();
    }
}
