package com.alexis.oauthservice.clients;

import com.alexis.userservicecommons.models.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface UserFeignClient {

    @GetMapping("/usuarios/search/buscar")
    public User findByUsername(@RequestParam String username);

    @PutMapping("/usuarios/{id}")
    public User update(@RequestBody User user, @PathVariable Integer id);

}
