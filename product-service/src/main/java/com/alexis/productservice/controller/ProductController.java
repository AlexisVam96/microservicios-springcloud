package com.alexis.productservice.controller;



import com.alexis.commons.models.entity.Product;
import com.alexis.productservice.models.Category;
import com.alexis.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
public class  ProductController {

    @Autowired
    private ProductService productService;

    @Value("${server.port}")
    private Integer port;

    @GetMapping("/listar")
    public ResponseEntity<List<Product>> getAll(){
        List<Product> products = productService.findAll().stream().map( producto ->{
            producto.setPort(port);
            return producto;
        }).collect(Collectors.toList());
        if(products.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/ver/{id}")
    public ResponseEntity<Product> getById(@PathVariable("id") Integer id) throws InterruptedException {

        if(id.equals(12)){
            throw new IllegalStateException("Producto no encontrado!");
        }
        if(id.equals(7)){
            TimeUnit.SECONDS.sleep(6);
        }
        Product product = productService.findById(id);
        //product.setPort(port);

        /*
        try{
            Thread.sleep(2000L);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        */

        //if(product == null)
        //    return ResponseEntity.notFound().build();
        return ResponseEntity.ok(product);
    }

    /*@GetMapping("/productos/categoria/{categoryId}")
    public ResponseEntity<List<Product>> getByCategoryId(@PathVariable("categoryId") Integer categoryId){
        List<Product> products = productService.findByCategoryId(categoryId);
        if(products.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(products);
    }*/

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public Product save(@RequestBody Product product){
        Product productNew = productService.save(product);
        return productNew;
    }

    @PutMapping("/editar/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Product actualizar(@RequestBody Product product, @PathVariable Integer id){
        Product productActualizado = productService.findById(id);
        productActualizado.setName(product.getName());
        productActualizado.setPrice(product.getPrice());
        return productService.save(productActualizado);
    }

    @DeleteMapping("/eliminar/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id){
        productService.deleteById(id);
    }

    @GetMapping("/producto-categoria/{category_id}")
    public ResponseEntity getProductAndCategory(@PathVariable Integer category_id){
        Map<String, Object> result = productService.getProductsAndCategories(category_id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/ver2/{category_id}")
    public ResponseEntity getProductWithCategory(@PathVariable Integer category_id){
        Map<String, Object> result = productService.getProductWithCategory(category_id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/listar2")
    public ResponseEntity getAllProductWithCategory(){
        Map<String, Object> result = productService.getAllProductWithCategory();
        return ResponseEntity.ok(result);
    }

   /* @GetMapping("/productos/categoria/{productId}")
    public ResponseEntity<Category> getCategories(@PathVariable("productId") Integer productId){
        Product product = productService.findById(productId);
        if(product == null)
            return ResponseEntity.notFound().build();
        Category category = productService.getCategories(productId);
        if(category == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(category);

    }

    @PostMapping("/savecategory/{productId}")
    public ResponseEntity<Category> saveCategory(@PathVariable("productId") Integer productId, @RequestBody Category category){
        Category categoryNew = productService.saveCategoryNew(productId, category);
        if(categoryNew == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(categoryNew);
    }*/
}
