package com.alexis.userservice.models.repository;


import com.alexis.userservicecommons.models.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "usuarios")
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    @RestResource(path = "/buscar")
    public User findByUsername(@Param("username") String username);
}
