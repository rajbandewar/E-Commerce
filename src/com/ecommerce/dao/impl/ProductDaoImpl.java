package com.ecommerce.dao.impl;

import com.ecommerce.dao.ProductDao;
import com.ecommerce.exception.InvalidInputException;
import com.ecommerce.exception.SQLException;
import com.ecommerce.model.Product;
import com.ecommerce.util.BDConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    @Override
    public void addProduct(Product product) {
        String query = "INSERT INTO products" +
                "(product_id, product_name, description, price, quantity)" +
                "VALUES(?, ?, ?, ?, ?)";
        try (Connection con = BDConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, product.getProductId());
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getDescription());
            ps.setDouble(4, product.getPrice());
            ps.setInt(5, product.getQuantity());
            ps.executeUpdate();

        } catch (Exception e) {
            //System.out.println("Failed to insert user " + e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";
        try (Connection con = BDConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product(rs.getInt("product_id"),
                        rs.getString("product_name"), rs.getString("description"),
                        rs.getDouble("price"), rs.getInt("quantity"));
                products.add(product);
            }
            return products;
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public Product getProductById(int id) {
        Product product = null;
        String query = "SELECT * FROM products WHERE product_id=?";
        try (Connection con = BDConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                 product = new Product(rs.getInt("product_id"),
                        rs.getString("product_name"), rs.getString("description"),
                        rs.getDouble("price"), rs.getInt("quantity"));

            }
            return product;
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public int getQuantity(int id) {

        String query = "SELECT quantity FROM products where product_id=?";
        try (Connection con = BDConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantity");
            } else {
                throw new InvalidInputException("Product not found");
            }
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }

    }

    @Override
    public void updateQuantityAfterProductSold(Connection con,int productId,int soldQuantity) {
        String query = "UPDATE products SET quantity = quantity - ? WHERE product_id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, soldQuantity);
            ps.setInt(2, productId);
            ps.executeUpdate();

        } catch (Exception e) {
            //System.out.println("Failed to insert user " + e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }
}
