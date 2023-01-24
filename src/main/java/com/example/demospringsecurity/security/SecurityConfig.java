package com.example.demospringsecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    public AuthenticationManagerBuilder authenticationManagerBuilder;
    SecurityConfig(AuthenticationManagerBuilder authenticationManagerBuilder){
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    //http configure security
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        //http.formLogin();

        //definir son propre formulaire d'authentification
        //http.formLogin().loginPage("/myLoginForm.html");

        //Desactivation de la session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //donner la permission à tous les utilisateurs de s'authentifier ou de s'enregistrer
        http.authorizeRequests().antMatchers("/login/**","/student/save").permitAll();

        //donner la permission qu'à l'admin d'enregistrer une application
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/students").hasAuthority("student");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new JWTAuthenticationFilter(authenticationManagerBuilder));//this.authenticationManager()));
        http.addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}