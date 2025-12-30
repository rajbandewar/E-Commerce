package com.ecommerce.dao;

import com.ecommerce.model.User;

import java.util.List;

public interface UserDao {
    void registerUser(User user);
    User doLogin(String userName,String password);
    User getUserByUserName(String userName);

    List<User> getAllUsers();
}
