package com.ecommerce.logic;

import com.ecommerce.dao.CartDao;
import com.ecommerce.dao.ProductDao;
import com.ecommerce.dao.impl.CartDaoImpl;
import com.ecommerce.dao.impl.ProductDaoImpl;
import com.ecommerce.model.CartItem;

import java.util.List;

public class CartLogic {
    CartDao cartDao = new CartDaoImpl();

    public void addProductToCart(int userId, int productId, int quantity) {
        cartDao.addProductToCart(userId,productId,quantity);

    }

    public List<CartItem> getCartItems(int userId){
        return cartDao.getCartItems(userId);
    }
}
