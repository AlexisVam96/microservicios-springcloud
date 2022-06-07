package com.alexis.itemservice.service;

import com.alexis.commons.models.entity.Product;
import com.alexis.itemservice.clients.ProductClientsRest;
import com.alexis.itemservice.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("serviceFeign")
@Primary
public class ItemServiceFeign implements ItemService {

    @Autowired
    private ProductClientsRest productClientsRest;

    public List<Item> getAll(){
        return productClientsRest.getAll().stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
    }

    public Item findById(Integer id, Integer quantity){
        return new Item(productClientsRest.fingById(id), quantity);
    }

    @Override
    public Product save(Product product) {
        return productClientsRest.crear(product);
    }

    @Override
    public Product update(Product product, Integer id) {
        return productClientsRest.editar(product, id);
    }

    @Override
    public void delete(Integer id) {
        productClientsRest.eliminar(id);
    }
}
