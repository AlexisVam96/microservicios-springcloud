package com.alexis.oauthservice.services;

import com.alexis.userservicecommons.models.entity.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface IUserService {

    public User findByUsername(String username);

    public User update(User user,Integer id);
}
