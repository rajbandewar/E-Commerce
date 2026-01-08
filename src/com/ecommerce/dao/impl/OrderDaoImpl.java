package com.ecommerce.dao.impl;

import com.ecommerce.dao.OrderDao;
import com.ecommerce.exception.SQLException;
import com.ecommerce.model.OrderedProduct;
import com.ecommerce.util.BDConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    @Override
    public void orderProduct(int userId, OrderedProduct orderedProduct) {
        String queryInsertOrder = "INSERT INTO orders" +
                "(total_bill, user_id)" +
                "VALUES" +
                "(?, ?);";
        try (Connection con = BDConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(queryInsertOrder, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDouble(1, orderedProduct.getQuantity() * orderedProduct.getPrice());
            ps.setInt(2, userId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int orderId = rs.getInt(1);
                orderedProduct.setOrderId(orderId);
                insertOrderedProduct(con, orderedProduct);
            } else {
                System.out.println(" Order id not found");
            }

        } catch (Exception e) {
            System.out.println("Failed to insert order " + e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public void orderProducts(int userId, double totalBill, List<OrderedProduct> orderedProducts) {
        String queryInsertOrder = "INSERT INTO orders" +
                "(total_bill, user_id)" +
                "VALUES" +
                "(?, ?);";
        //orderedProducts.stream().mapToInt((product)->{return product.getPrice()*product.getQuantity();}).sum()
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = BDConfig.getConnection();
            ps = con.prepareStatement(queryInsertOrder, Statement.RETURN_GENERATED_KEYS);
            con.setAutoCommit(false);
            ps.setDouble(1, totalBill);
            ps.setInt(2, userId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int orderId = rs.getInt(1);
                for (OrderedProduct op : orderedProducts) {
                    op.setOrderId(orderId);
                    insertOrderedProduct(con, op);
                }
            } else {
                System.out.println(" Order id not found");
            }
            con.commit();
        } catch (Exception e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (java.sql.SQLException ex) {
                e.printStackTrace();
            }

            throw new SQLException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    if (ps != null)
                        ps.close();
                    con.close();
                }
            } catch (java.sql.SQLException e) {
                System.out.println("Failed to order");
                e.printStackTrace();
            }
        }
    }

    void insertOrderedProduct(Connection con, OrderedProduct orderedProducts) {
        String query = "INSERT INTO ordered_products" +
                "(order_id, product_id, quantity)" +
                "VALUES" +
                "(?, ?, ?);";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, orderedProducts.getOrderId());
            ps.setInt(2, orderedProducts.getProductId());
            ps.setInt(3, orderedProducts.getQuantity());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }


    @Override
    public List<OrderedProduct> getOrderHistoryByUserName(String userName) {
        String query = "SELECT o.order_id,total_bill," +
                "product_name,description,op.quantity,p.product_id  from orders as o " +
                "JOIN users u On o.user_id = u.user_id " +
                "JOIN ordered_products op On op.order_id = o.order_id " +
                "JOIN products p on op.product_id = p.product_id " +
                "WHERE u.username =?";
        List<OrderedProduct> orderedProducts = new ArrayList<>();
        try (Connection con = BDConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, userName);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderedProduct product = new OrderedProduct();
                product.setDescription(rs.getString("description"));
                product.setProductId(rs.getInt("product_id"));
                product.setQuantity(rs.getInt("quantity"));
                orderedProducts.add(product);
            }

        } catch (Exception e) {
            System.out.println("Failed to insert order " + e.getMessage());
            throw new SQLException(e.getMessage());
        }
        return orderedProducts;
    }
}
