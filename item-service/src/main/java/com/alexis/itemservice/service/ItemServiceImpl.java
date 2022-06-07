package com.alexis.itemservice.service;

import com.alexis.commons.models.entity.Product;
import com.alexis.itemservice.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("serviceRestTemplate")
public class ItemServiceImpl implements ItemService{

    @Autowired
    private RestTemplate restTemplate;

    public List<Item> getAll(){
        List<Product> products = Arrays.asList(restTemplate.getForObject("http://localhost:8001/listar",Product[].class) );

        //CAMBIAR LA LISTA DE PRODUCTOS A LISTA DE ITEM
        return products.stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
    }

    public Item findById(Integer id, Integer quantity){
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("id", id.toString());
        Product product = restTemplate.getForObject("http://localhost:8001/ver/{id}",Product.class, pathVariables) ;
        return new Item(product, quantity);
    }

    @Override
    public Product save(Product product) {
        HttpEntity<Product> body = new HttpEntity<>(product);
        ResponseEntity<Product> response = restTemplate.exchange("http://localhost:8001/crear", HttpMethod.POST, body, Product.class);
        Product productResponse = response.getBody();
        return productResponse;
    }

    @Override
    public Product update(Product product, Integer id) {
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("id", id.toString());

        HttpEntity<Product> body = new HttpEntity<>(product);
        ResponseEntity<Product> response = restTemplate.exchange("http://localhost:8001/editar/{id}", HttpMethod.PUT, body, Product.class, pathVariables);
        Product productResponse = response.getBody();
        return productResponse;
    }

    @Override
    public void delete(Integer id) {
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("id", id.toString());
        restTemplate.delete("http://localhost:8001/eliminar/{id}", pathVariables);
    }
}
