package com.alexis.categoryservice.controller;

import com.alexis.categorycommons.entity.Category;
import com.alexis.categoryservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listar")
    public List<Category> getAll() {
        List<Category> categories = categoryService.findAll();
        return categories;
    }

    @GetMapping("/ver/{id}")
    public Category findById(@PathVariable("id") Integer id) {
        Category category = categoryService.findById(id);
        return category;
    }

    @PostMapping("/crear")
    public Category save(@RequestBody Category category) {
        Category categoryNew = categoryService.save(category);

        return categoryNew;
    }

}
