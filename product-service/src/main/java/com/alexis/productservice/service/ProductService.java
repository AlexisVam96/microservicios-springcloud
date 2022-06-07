package com.alexis.productservice.service;


import com.alexis.commons.models.entity.Product;
import com.alexis.productservice.clients.CategoryFeignClient;
import com.alexis.productservice.models.Category;
import com.alexis.productservice.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private CategoryFeignClient categoryFeignClient;

    public List<Product> findAll(){
        return productRepository.findAll();
    }

    public Product findById(Integer id){
        return productRepository.findById(id).orElse(null);
    }

    public Product save(Product product){
        return productRepository.save(product);
    }

    public void deleteById(Integer id){
        productRepository.deleteById(id);
    }

    public List<Category> findAllCategories(){
        return categoryFeignClient.findAll();
    }

    public Map<String, Object> getProductsAndCategories(Integer category_id){
        Map<String, Object> result = new HashMap<>();
        List<Product> products = productRepository.findAllByCategoryId(category_id);
        if(products == null){
            result.put("Mensaje", "No existe el producto en la base de datos");
        }else{
            result.put("Producto", products);
        }

        Category category = categoryFeignClient.getById(category_id);

        if(category == null){
            result.put("Mensaje","No existe la categoria en la base de datos");
        }else{
            result.put("Categoria", category);
        }
        return result;
    }

    public Map<String, Object> getAllProductWithCategory () {
        Map<String, Object> result = new HashMap<>();

//        List<Product> products = productRepository.findAll();
//
//        for(Product product: products){
//            result.put(product.getName(), product);
//        }

        result = productRepository.findAll().stream().collect(Collectors.toMap(p->p.getName(), p->p
                , (ant, pre) -> pre ));
        return result;
    }

    public Map<String, Object> getProductWithCategory(Integer product_id){
        Map<String, Object> result = new HashMap<>();
        Product product = null;
        Category category = null;

        try {
            product = findById(product_id);
        }catch(DataAccessException e){
            result.put("mensaje", "No existe el producto en la base de datos");
            result.put("error", "Error en la base de datos: "+ e.getMessage());
            return result;
        }
        result.put("producto", product);

        try {
            category = categoryFeignClient.getById(product.getCategoryId());
        }catch(DataAccessException e){
            result.put("mensaje", "No existe la categoria en la base de datos");
            result.put("error", "Error en la base de datos: "+ e.getMessage());
            return result;
        }
        result.put("categoria", category);
        return result;
    }

    /*public List<Product> findByCategoryId(Integer categoryId){
        return productRepository.findByCategoryId(categoryId);
    }

    public Category getCategories(Integer productId){
        Category category = restTemplate.getForObject("http://localhost:8083/api/categorias/producto/"+productId, Category.class);
        return category;
    }

    public Category saveCategoryNew(Integer productId, Category category){
        category.setProductId(productId);
        Category categoryNew = categoryFeignClient.save(category);
        return categoryNew;
    }
    */
}
