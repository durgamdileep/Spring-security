package com.dileep.JwtAuthenticationAndAuthorization.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JWTService {

    Map<String, Object> claims= new HashMap<>();
    private String MY_SECRET_KEY="";

    public JWTService(){
        try {
            KeyGenerator Key_generator= KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey= Key_generator.generateKey();
            MY_SECRET_KEY= Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * generating the token
     */
    public String generateToken(UserDetails userDetails) {
        claims.put("roles",userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userDetails.getUsername())
                .issuedAt( new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+60*60*30))
                .and()
                .signWith(generateSecretKey())
                .compact();
    }

    /**
     * generating secret key
     */
    private SecretKey generateSecretKey() {
        System.out.println("JWT Secret Key (Base64): " + MY_SECRET_KEY);
       // Base64.getDecoder().decode(MY_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        byte[] bytes= Decoders.BASE64.decode(MY_SECRET_KEY);
        return Keys.hmacShaKeyFor(bytes);
    }


    public String extractUserName(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName= extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public  boolean validateToken2(String token){
        return  !isTokenExpired(token);
    }


    private Claims extractAllClaims(String token){
        try{
            return Jwts.parser()
                    .verifyWith(generateSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid Token...");
        }
    }

    public boolean isTokenExpired(String token) {
        Claims claims = extractAllClaims(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }


    public List<String> extractRoles(String token) {
        Claims claims= extractAllClaims(token);
        return claims.get("roles",List.class);
    }
}
