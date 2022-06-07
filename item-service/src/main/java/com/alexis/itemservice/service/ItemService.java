package com.alexis.itemservice.service;

import com.alexis.commons.models.entity.Product;
import com.alexis.itemservice.models.Item;

import java.util.List;

public interface ItemService {

    public List<Item> getAll();

    public Item findById(Integer id, Integer quantity);

    public Product save(Product product);

    public Product update(Product product, Integer id);

    public void delete(Integer id);
}
