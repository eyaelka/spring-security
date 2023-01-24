package com.example.demospringsecurity.security;

import java.util.ArrayList;
import java.util.Collection;

import com.example.demospringsecurity.models.Student;
import com.example.demospringsecurity.services.interfaces.StudentInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImplementation implements UserDetailsService{

    @Autowired
    private StudentInterface studentInterface;
    @Override

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student utilisateur = studentInterface.findStudentByUsername(username);
        if(utilisateur == null) throw  new UsernameNotFoundException("Student n'existe pas");

        //Preparer les roles de l'utilisateur sous forme de collection d'objets compressible par spring security
        Collection<GrantedAuthority> authorisations = new ArrayList<GrantedAuthority>();
        /*studentInterface.getRolesOfUser(utilisateur).forEach(r->{
            authorisations.add(new SimpleGrantedAuthority(r));
        });

         */
        authorisations.add(new SimpleGrantedAuthority(utilisateur.getRole()));
        return new User(utilisateur.getUsername(),utilisateur.getPassword(),authorisations);
    }

}
