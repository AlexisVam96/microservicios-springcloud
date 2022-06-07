package com.alexis.pruebajjwt.controller;


import com.alexis.pruebajjwt.entity.Entidad;
import com.alexis.pruebajjwt.service.EntidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"*"})
public class EntidadController extends ResponseEntityExceptionHandler {

    @Autowired
    private EntidadService entidadService;

    @GetMapping("/entidades")
    public List<Entidad> getAll(){
        return entidadService.getAll();
    }

    @GetMapping("/entidades-hateos/{id_entidad}")
    public EntityModel<Entidad> getEntidadById(@PathVariable Integer id_entidad){
        Entidad entidad = entidadService.getById(id_entidad);
        EntityModel<Entidad> resource = EntityModel.of(entidad);
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAll())
                .withRel("entidades"))
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllEntidades())
                        .withRel("entidades2"));
        return resource;
    }

    @GetMapping("/entidades2")
    public ResponseEntity<Map<String, Object>> getAllEntidades(){
        Map<String, Object> response = new HashMap<>();

        List<Entidad> entidades = entidadService.getAll();

        if(entidades.isEmpty()){
            response.put("mensaje", "No existen elementos de Entidad en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
        }

        response.put("entidades", entidades);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @GetMapping("/entidades/{id_entidad}")
    public ResponseEntity<?> getById(@PathVariable Integer id_entidad){

        Entidad entidad = null;
        Map<String, Object> response = new HashMap<>();

        try {
            entidad = entidadService.getById(id_entidad);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(entidad == null) {
            response.put("mensaje", "La entidad con id: ".concat(id_entidad.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(entidad, HttpStatus.OK);
    }

    @PostMapping("/entidades/crear")
    public ResponseEntity<?> create(@Valid @RequestBody Entidad entidad, BindingResult result) {
        Entidad entidadNew = null;
        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }try {
            entidadNew = entidadService.save(entidad);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            //response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "La entidad '" + entidadNew.getNombre_comercial() +"' ha sido creado con éxito!" );
        response.put("entidad", entidadNew);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/entidades/editar/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Entidad entidad, BindingResult result, @PathVariable Integer id) {
        Entidad entidadActual = entidadService.getById(id);
        Map<String, Object> response = new HashMap<>();
        Entidad entidadUpdate = null;
        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map( err -> "El campo '" + err.getField() + "' "+ err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if(entidadActual == null) {
            response.put("mensaje", "Error, no se pudo editar, la Entidad con id: ".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }try {
            entidadActual.setDocumento(entidad.getDocumento());
            entidadActual.setNro_documento(entidad.getNro_documento());
            entidadActual.setRazon_social(entidad.getRazon_social());
            entidadActual.setNombre_comercial(entidad.getNombre_comercial());
            entidadActual.setContribuyente(entidad.getContribuyente());
            entidadActual.setDireccion(entidad.getDireccion());
            entidadActual.setTelefono(entidad.getTelefono());
            entidadActual.setEstado(entidad.getEstado());
            entidadUpdate = entidadService.save(entidadActual);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar la Entidad con id: "+ id +"  en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El  '"+entidadUpdate.getNombre_comercial()+"' ha sido actualizado con éxito!" );
        response.put("entidad", entidadUpdate);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/entidades/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            entidadService.delete(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar la entidad con id: "+id+" de la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "La entidad fue eliminado con éxito!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
