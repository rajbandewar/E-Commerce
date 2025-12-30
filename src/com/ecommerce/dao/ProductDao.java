package com.ecommerce.dao;

import com.ecommerce.model.Product;

import java.util.List;

public interface ProductDao {
    public void addProduct(Product product);

    List<Product> getAllProducts();

    Product getProductById(int id);

    int getQuantity(int id);
}
