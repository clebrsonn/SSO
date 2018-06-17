package com.api.mv.sso.service;

import com.api.mv.sso.model.User;
import com.api.mv.sso.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class MyUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(), user.getPwd(), getGrantedAuthorities(user)))
                .orElseThrow(() -> new UsernameNotFoundException("User identified by email: " + email + " Not found"));
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    //Lista com as permissões dos usuários cadastrados
    private Collection<? extends GrantedAuthority> getGrantedAuthorities(User user) {
        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
//ForEach igual ao de baixo
        user.getRoles().forEach(role -> (grantedAuthorities).add(new SimpleGrantedAuthority(role.getRole())));
//        for (Role role : user.getRoles()) {
//            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRole());
//            grantedAuthorities.add(grantedAuthority);
//        }

        return grantedAuthorities;
    }
}

