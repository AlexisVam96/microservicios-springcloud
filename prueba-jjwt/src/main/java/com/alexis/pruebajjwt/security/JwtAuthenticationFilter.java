package com.alexis.pruebajjwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Autowired
    private ReactiveAuthenticationManager authenticationManager;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                //justOrEmpty -> guardamos o convertimos el token que recibimos de la cabecera http
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                //filtramos el token con las iniciales Bearer
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                //En caso no contenga el Bearer no hacemos nada
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                //si contiene el Bearer modificamos el String quitando el Beare del token
                .map(token -> token.replace("Bearer ", ""))
                //flatMap -> Convertimos el flujo en otro flujo inyectando el token en el autheticationManager
                .flatMap(token -> authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(null, token)))
                //Emite el authentication utilizamos otrol filtro pasando las credenciales del authentication
                .flatMap(authentication -> chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication)));
    }

}
