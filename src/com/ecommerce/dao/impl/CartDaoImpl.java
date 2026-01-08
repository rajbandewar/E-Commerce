package com.ecommerce.dao.impl;

import com.ecommerce.dao.CartDao;
import com.ecommerce.exception.SQLException;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.util.BDConfig;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CartDaoImpl implements CartDao {


    @Override
    public void addProductToCart(int userId, int productId, int quantity) {
        String query = "INSERT INTO cart_item" +
                "(user_id, product_id, quantity)" +
                "VALUES(?,?,?);";
        try (Connection con = BDConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public List<CartItem> getCartItems(int userId) {

        List<CartItem> cartItems = new ArrayList<>();
        String query = "SELECT * FROM cart_item ci JOIN products p ON ci.product_id = p.product_id  where ci.user_id =?";

        try (Connection con = BDConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                Product product = new Product(rs.getInt("product_id"),
                        rs.getString("product_name"), rs.getString("description"),
                        rs.getDouble("price"), rs.getInt("p.quantity"));
                CartItem cartItem = new CartItem(rs.getInt("cart_id"), userId, product, rs.getInt("ci.quantity"));
                cartItems.add(cartItem);
            }

            return cartItems;
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }

    }

    @Override
    public void EmptyCartAfterPurchased(Connection con, int userId) {
        String query = "DELETE FROM cart_item WHERE user_id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }
}
