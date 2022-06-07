package com.alexis.categoryservice.service;

import com.alexis.categorycommons.entity.Category;
import com.alexis.categoryservice.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private ICategoryRepository categoryRepository;

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    public Category findById(Integer id){
        return categoryRepository.findById(id).orElse(null);
    }

    public Category save(Category cliente){
        return categoryRepository.save(cliente);
    }


}
