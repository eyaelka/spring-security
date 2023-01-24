package com.example.demospringsecurity.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
/*
 * Filtre qui s'exécute à chaque requet pour intercepter et voire si l'utilisteur est autoriser.
 */

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //preparer les entête à autoriser
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers",
                "Origin, Accept, X-Requested-With, Content-Type, "
                        + "Access-Control-Request-Method, Access-Control-Request-Headers,Authorization");
        response.addHeader("Access-Control-Expose-Headers",
                "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, Authorization");
        //si demande demande des entête à authoriser
        if(request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else {
            //recuperer l'entête du token
            String jwt = request.getHeader(SecurityConstante.HEADER_STRING);
            if(jwt == null || !jwt.startsWith(SecurityConstante.TOKEN_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            try {
                Claims claims=Jwts.parser()//recuperer les revendication
                        .setSigningKey(SecurityConstante.SECRET)
                        .parseClaimsJws(jwt.replace(SecurityConstante.TOKEN_PREFIX,""))
                        .getBody();

                String useremail =claims.getSubject();//lecture de l'email de l'utilisateur qui a envoyer la requet
                // recuperer les roles de l'utilisations
                ArrayList<Map<String, String>> roles=(ArrayList<Map<String, String>>) claims.get("roles");
                Collection<GrantedAuthority> authorities=new ArrayList<>();
                roles.forEach(r->{
                    authorities.add(new SimpleGrantedAuthority(r.get("authority")));
                });

                //formuler l'utilisateur authentifié
                UsernamePasswordAuthenticationToken utilisateurAuthentifier=
                        new UsernamePasswordAuthenticationToken(useremail, null,authorities);
                // Passer cet utilisateur au contexte de spring security.
                SecurityContextHolder.getContext().setAuthentication(utilisateurAuthentifier);
                filterChain.doFilter(request, response);
            }catch (Exception e) {
                filterChain.doFilter(request, response);
                return;
            }
        }

    }

}
