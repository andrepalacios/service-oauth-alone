package com.andre.servicio.app.oauth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andre.servicio.app.oauth.clients.UserFeignClient;
import com.andre.servicio.app.oauth.models.entity.UserApi;

@Service
public class UserService  implements UserDetailsService{
	
    private Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
    private UserFeignClient client;

	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserApi user = client.findByUsername(username);
        if(user == null) {
            log.error("username "+username+" doesn't exist in the system");
            throw new UsernameNotFoundException("error in login, username doesn't exist in the system");
        }
        
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getNombre()))
                .peek(authority -> log.info("Role: " +authority.getAuthority()))
                .collect(Collectors.toList());
        
        log.info("Usuario autenticado:" +username);
        
        return new User(user.getUsername(), user.getPassword(), user.getEnabled(),
                true, true, true, authorities);

	}

}
