package com.ecommerce.dao.impl;

import com.ecommerce.dao.UserDao;
import com.ecommerce.exception.SQLException;
import com.ecommerce.model.User;
import com.ecommerce.util.BDConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    public static final String QUERY_USER_REG = "INSERT INTO users" +
            "(user_id, first_name, last_name, username, password, city, email, mobile, role)" +
            "VALUES(0, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String QUERY_LOGIN = "SELECT *FROM users WHERE username=? AND password=?";

    public static final String QUERY_GET_USER_BY_USERNAME = "SELECT *FROM users WHERE username=?";
    public static final String QUERY_GET_USERS = "SELECT *FROM users";

    @Override
    public void registerUser(User user) {
        try (Connection con = BDConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(QUERY_USER_REG)) {

            ps.setString(1, user.getFirst_name());
            ps.setString(2, user.getLast_name());
            ps.setString(3, user.getUserName());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getCity());
            ps.setString(6, user.getEmail());
            ps.setString(7, user.getMobile());
            ps.setInt(8, user.getRole());
            ps.executeUpdate();

        } catch (Exception e) {
            //System.out.println("Failed to insert user " + e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public User doLogin(String userName, String password) {
        try (Connection con = BDConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(QUERY_LOGIN)) {
            ps.setString(1, userName);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFirst_name(rs.getString("first_name"));
                user.setLast_name(rs.getString("last_name"));
                user.setUserName(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setCity(rs.getString("city"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getInt("role"));
                user.setMobile(rs.getString("mobile"));
                //System.out.println("logged in user +"+user);
                return user;
            } else {
                // System.out.println("Login failed");
            }

        } catch (Exception e) {
            System.out.println("Failed to get user " + e.getMessage());
            throw new SQLException(e.getMessage());
        }
        return null;
    }

    @Override
    public User getUserByUserName(String userName) {
        try (Connection con = BDConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(QUERY_GET_USER_BY_USERNAME)) {
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFirst_name(rs.getString("first_name"));
                user.setLast_name(rs.getString("last_name"));
                user.setUserName(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setCity(rs.getString("city"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getInt("role"));
                user.setMobile(rs.getString("mobile"));
                //System.out.println("User +"+user);
                return user;
            } else {
                // System.out.println("No User");
            }
        } catch (Exception e) {
            System.out.println("Failed to get user " + e.getMessage());
            throw new SQLException(e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection con = BDConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(QUERY_GET_USERS)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFirst_name(rs.getString("first_name"));
                user.setLast_name(rs.getString("last_name"));
                user.setUserName(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setCity(rs.getString("city"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getInt("role"));
                user.setMobile(rs.getString("mobile"));
                //System.out.println("User +"+user);
                users.add(user) ;
            }
        } catch (Exception e) {
            System.out.println("Failed to get user " + e.getMessage());
            throw new SQLException(e.getMessage());
        }
        return users;
    }


}
