package com.dileep.JwtAuthenticationAndAuthorization.Configuration;

import com.dileep.JwtAuthenticationAndAuthorization.Configuration.SecurityClass.MyUserDetailsService;
import com.dileep.JwtAuthenticationAndAuthorization.Filters.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder encoder(){
        return  new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService(){
        return  new MyUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder encoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        // authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return authProvider;
    }
    /**
     *  AuthenticationConfiguration internally creates a DaoAuthenticationProvider
     *
     *  Spring Boot auto-configures the AuthenticationManager by looking for:
     *     A UserDetailsService bean (which you provided),
     *     A PasswordEncoder bean (you also provided),
     *     And then automatically sets those on the DaoAuthenticationProvider.
     *
     * You don't need to set it manually.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

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
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
