package com.alexis.oauthservice.services;

import brave.Tracer;
import com.alexis.oauthservice.clients.UserFeignClient;
import com.alexis.userservicecommons.models.entity.User;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService, IUserService {

    Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private Tracer tracer;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            User user = userFeignClient.findByUsername(username);

            List<GrantedAuthority> authorities = user.getRoles()
                    .stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                    .peek(simpleGrantedAuthority -> log.info("Role: " + simpleGrantedAuthority.getAuthority()))
                    .collect(Collectors.toList());

            log.info("Usuario autenticado: " + username);

            return new org.springframework.security.core.userdetails.User(user.getUsername(),
                    user.getPassword(), user.getEnabled(), true, true,
                    true, authorities);

        }catch (FeignException e){

            String value = "Error en el login, no existe el usuario '" + username + "' en el sistema";
            log.error(value);

            tracer.currentSpan().tag("error.mensaje", value + ": "+ e.getMessage());
            throw new UsernameNotFoundException("Error en el login, no existe el usuario '" + username + "' en el sistema");
        }
    }

    @Override
    public User findByUsername(String username) {
        return userFeignClient.findByUsername(username);
    }

    @Override
    public User update(User user, Integer id) {
        return userFeignClient.update(user,id);
    }

}
