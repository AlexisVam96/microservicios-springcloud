package com.alexis.pruebajjwt.repository;


import com.alexis.pruebajjwt.entity.Entidad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEntidadRepository extends JpaRepository<Entidad, Integer> {
}
