package com.ecommerce.logic;

import com.ecommerce.dao.ProductDao;
import com.ecommerce.dao.impl.ProductDaoImpl;
import com.ecommerce.exception.InvalidInputException;
import com.ecommerce.model.Product;

import java.util.List;

public class ProductLogic {
    ProductDao productDao = new ProductDaoImpl();

    public void addProduct(Product product) {
        productDao.addProduct(product);
    }

    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    public int getQuantity(int productId) {
        if (productId <= 0) {
            throw new InvalidInputException("Id can not be zero");
        }
        return productDao.getQuantity(productId);
    }


}
