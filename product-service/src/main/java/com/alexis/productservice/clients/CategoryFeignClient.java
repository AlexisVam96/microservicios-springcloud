package com.alexis.productservice.clients;

import com.alexis.productservice.models.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "category-service")
public interface CategoryFeignClient {

    @GetMapping("/listar")
    public List<Category> findAll();

    @GetMapping("/ver/{id}")
    public Category getById(@PathVariable Integer id);

    @PostMapping("/crear")
    public Category save(@RequestBody Category category);
}
