package com.alexis.pruebajjwt.repository;

import com.alexis.pruebajjwt.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

    public Usuario findByUsername(String username);
}
