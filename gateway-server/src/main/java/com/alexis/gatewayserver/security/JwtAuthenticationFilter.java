package com.alexis.gatewayserver.security;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Autowired
    private ReactiveAuthenticationManager authenticationManager;

    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .map(token -> token.replace("Bearer ", ""))
                .flatMap(token -> authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(null, token)))
                .flatMap(authentication -> chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication)))
                .onErrorResume(JwtException.class, e -> {
                    System.out.println("Jwt Exception: " + e);
                    byte[] bytes = e.getMessage().getBytes();
                    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                    return Mono.fromRunnable(() -> {
                        ServerHttpResponse response = exchange.getResponse();
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    }).then(exchange.getResponse().writeWith(Flux.just(buffer)));

                })
                .onErrorResume(AuthenticationException.class, e -> {
                    System.out.println("Authentication Exception: " + e);
                    byte[] bytes = e.getMessage().getBytes();
                    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

                    return Mono.fromRunnable(() -> {
                        ServerHttpResponse response = exchange.getResponse();
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    }).then(exchange.getResponse().writeWith(Flux.just(buffer)));
                });
    }

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//                //justOrEmpty -> guardamos o convertimos el token que recibimos de la cabecera http
//        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
//                //filtramos el token con las iniciales Bearer
//                .filter(authHeader -> authHeader.startsWith("Bearer "))
//                //En caso no contenga el Bearer no hacemos nada
//                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
//                //si contiene el Bearer modificamos el String quitando el Beare del token
//                .map(token -> token.replace("Bearer ", ""))
//                //flatMap -> Convertimos el flujo en otro flujo inyectando el token en el autheticationManager
//                .flatMap(token -> authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(null, token)))
//                //Emite el authentication utilizamos otrol filtro pasando las credenciales del authentication
//                .flatMap(authentication -> chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication)));
//    }

}
