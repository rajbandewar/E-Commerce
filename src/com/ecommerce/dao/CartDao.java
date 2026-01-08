package com.ecommerce.dao;

import com.ecommerce.model.CartItem;

import java.util.List;

public interface CartDao {

     public void addProductToCart(int userId, int productId, int quantity);

     public List<CartItem> getCartItems(int userId);

}
