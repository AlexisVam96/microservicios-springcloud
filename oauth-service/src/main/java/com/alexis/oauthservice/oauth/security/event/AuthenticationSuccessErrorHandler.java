package com.alexis.oauthservice.oauth.security.event;

import brave.Tracer;
import com.alexis.oauthservice.services.IUserService;
import com.alexis.userservicecommons.models.entity.User;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

    @Autowired
    private IUserService userService;

    @Autowired
    private Tracer tracer;

    Logger log = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);

    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {

        //if(authentication.getDetails() instanceof WebAuthenticationDetails){
        if(authentication.getName().equalsIgnoreCase("frontendapp")){
            return ;
        }

        UserDetails user = (UserDetails) authentication.getPrincipal();
        System.out.println("Success Login: " + user.getUsername());
        log.info("Success Login: " + user.getUsername());

        User userCommons = userService.findByUsername(authentication.getName());

        if(userCommons.getAttempts() != null && userCommons.getAttempts() > 0){
            userCommons.setAttempts(0);
            userCommons.setEnabled(true);
            userService.update(userCommons,userCommons.getId());
        }

    }

    @Override
    public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
        String mensaje = "Error en el login: "+ exception.getMessage();
        log.error(mensaje);
        System.out.println(mensaje);

        try {

            StringBuilder errors = new StringBuilder();

            errors.append(mensaje);

            User user = userService.findByUsername(authentication.getName());
            if(user.getAttempts() == null){
                user.setAttempts(0);
            }

            log.info("Intentos actual es de: " + user.getAttempts());

            user.setAttempts(user.getAttempts()+1);

            log.info("Intentos despues es de: " + user.getAttempts());

            errors.append(" - Intentos de login: "+ user.getAttempts());

            if(user.getAttempts() >= 3){
                String errorMaxIntentos = String.format("El usuarios %s des-habilitado por tres intentos", user.getUsername());
                log.error(errorMaxIntentos);
                errors.append(" - " + errorMaxIntentos);
                user.setEnabled(false);
            }   

            userService.update(user, user.getId());

            tracer.currentSpan().tag("error.mensaje", errors.toString());

        }catch (FeignException e){
            log.error(String.format("El usuario %s no existe en el sistema", authentication.getName()));
        }

    }
}
