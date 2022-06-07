package com.alexis.clienteservice.controller;

import com.alexis.clienteservice.entity.Cliente;
import com.alexis.clienteservice.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> findAll(){
        List<Cliente> clientes = clienteService.findAll();
        if(clientes.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<Cliente> findById(@PathVariable("id") Integer id){
        Cliente cliente = clienteService.findById(id);
        if(cliente == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(cliente);
    }

    @PostMapping("/clientes")
    public ResponseEntity<Cliente> save(@RequestBody Cliente cliente){
        Cliente clienteNew = clienteService.save(cliente);
        if(clienteNew == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(clienteNew);
    }

}
