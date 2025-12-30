package com.ecommerce.logic;

import com.ecommerce.dao.UserDao;
import com.ecommerce.dao.impl.UserDaoImpl;
import com.ecommerce.model.User;
import com.ecommerce.exception.InvalidInputException;

import java.util.List;

public class UserLogic {

    private UserDao userDao = new UserDaoImpl();

    public void register(User user) throws InvalidInputException {

        if (userDao.getUserByUserName(user.getUserName()) != null) {
            throw new InvalidInputException("Username already exists");
        }

        userDao.registerUser(user);
    }

    public User login(String username, String password)
            throws InvalidInputException {

        if (username == null || password == null) {
            throw new InvalidInputException("Username and password required");
        }

        User user = userDao.doLogin(username, password);
        if (user == null) {
            throw new InvalidInputException("Invalid credentials");
        }
        return user;
    }

    public List<User> getAllUsers() {
           return userDao.getAllUsers();
    }
}
