package com.alexis.productservice.repository;


import com.alexis.commons.models.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {

    public Product findByCategoryId(Integer category_id);

    public List<Product> findAllByCategoryId(Integer category_id);

}
