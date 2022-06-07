package com.alexis.itemservice.clients;

import com.alexis.commons.models.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@FeignClient(name = "producto-service", url = "http://localhost:8001", primary = false)
//USANDO RIBBON
@FeignClient(name = "product-service")
public interface ProductClientsRest {

    @GetMapping("/listar")
    public List<Product> getAll();

    @GetMapping("/ver/{id}")
    public Product fingById(@PathVariable Integer id);

    @PostMapping("/crear")
    public Product crear(@RequestBody Product product);

    @PutMapping("/editar/{id}")
    public Product editar(@RequestBody Product product, @PathVariable Integer id);

     @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Integer id);
}
