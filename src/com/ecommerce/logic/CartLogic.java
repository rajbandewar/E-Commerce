package com.ecommerce.logic;

import com.ecommerce.dao.CartDao;
import com.ecommerce.dao.ProductDao;
import com.ecommerce.dao.impl.CartDaoImpl;
import com.ecommerce.dao.impl.ProductDaoImpl;
import com.ecommerce.exception.InvalidInputException;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;

import java.util.List;

public class CartLogic {
    CartDao cartDao = new CartDaoImpl();
    ProductDao productDao = new ProductDaoImpl();

    public void addProductToCart(int userId, int productId, int quantity) {
        Product product = productDao.getProductById(productId);
        if (product == null) {
            throw new InvalidInputException("Invalid Product ID");
        }
        if (product.getQuantity() < quantity) {
            throw new InvalidInputException("Only "+product.getQuantity()
                    +" "+ product.getProductName() +" Available ");
        }
        cartDao.addProductToCart(userId, productId, quantity);
    }

    public List<CartItem> getCartItems(int userId) {
        return cartDao.getCartItems(userId);
    }
}
