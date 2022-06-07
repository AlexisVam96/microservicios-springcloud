package com.alexis.pruebajjwt.service;


import com.alexis.pruebajjwt.entity.Entidad;
import com.alexis.pruebajjwt.repository.IEntidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntidadService {

    @Autowired
    private IEntidadRepository entidadRepository;

    public List<Entidad> getAll(){
        return entidadRepository.findAll();
    }

    public Entidad getById(Integer id_entidad){
        return entidadRepository.findById(id_entidad).orElse(null);
    }

    public Entidad save(Entidad entidad){
        return entidadRepository.save(entidad);
    }

    public void delete(Integer id_entidad){
        entidadRepository.deleteById(id_entidad);
    }
}
