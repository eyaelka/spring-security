package com.example.demospringsecurity.security;
import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demospringsecurity.models.Student;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/*
 * Filtre d'extraction de l'utilisateurs
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    Student u = null;
    private  AuthenticationManagerBuilder authenticationManagerBuilder;
    public JWTAuthenticationFilter(AuthenticationManagerBuilder authenticationManagerBuilder) {
        super();
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }
    /*
     * Methode d'extraction de l'utilisateur
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            u = new ObjectMapper().readValue(request.getInputStream(), Student.class);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

        return authenticationManagerBuilder.getObject().authenticate(new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        User springUser =(User) authResult.getPrincipal();
        String jwtToken = Jwts.builder()
                .setSubject(springUser.getUsername())
                .setExpiration(new Date(System.currentTimeMillis()+SecurityConstante.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SecurityConstante.SECRET)
                .claim("roles", springUser.getAuthorities())
                .compact();
        response.addHeader(SecurityConstante.HEADER_STRING, SecurityConstante.TOKEN_PREFIX+jwtToken);
    }

}