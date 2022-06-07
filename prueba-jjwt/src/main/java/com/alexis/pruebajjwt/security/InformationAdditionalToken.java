package com.alexis.pruebajjwt.security;

import com.alexis.pruebajjwt.entity.Usuario;
import com.alexis.pruebajjwt.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InformationAdditionalToken implements TokenEnhancer {

    @Autowired
    private UsuarioService userService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        Map<String, Object> info = new HashMap<>();

        Usuario user = userService.findByUsername(oAuth2Authentication.getName());

        info.put("attempts", user.getAttempts());
        info.put("username", user.getUsername() );
        info.put("enabled", user.getEnabled());

        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(info);

        return oAuth2AccessToken;
    }
}
