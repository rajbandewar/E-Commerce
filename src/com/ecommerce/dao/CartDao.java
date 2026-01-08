package com.ecommerce.dao;

import com.ecommerce.model.CartItem;

import java.sql.Connection;
import java.util.List;

public interface CartDao {

    void addProductToCart(int userId, int productId, int quantity);

    List<CartItem> getCartItems(int userId);

    void EmptyCartAfterPurchased(Connection con, int userId);
}
