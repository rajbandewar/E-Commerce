package com.ecommerce.dao;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;

import java.util.List;

public interface CartDao {
    // int createCart(int userId);
    // void addProductToCart(int userId);

     public void addProductToCart(int userId, int productId, int quantity);

     public List<CartItem> getCartItems(int userId);

}
