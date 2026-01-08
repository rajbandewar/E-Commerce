package com.ecommerce.dao;

import com.ecommerce.model.OrderedProduct;

import java.util.HashMap;
import java.util.List;

public interface OrderDao {
    void orderProduct(int userId, OrderedProduct orderedProduct);

    void orderProducts(int userId,double totalBill,List<OrderedProduct> orderedProducts);

   // HashMap<Integer, List<OrderedProduct>> getOrderHistory(int userId);

    List<OrderedProduct> getOrderHistoryByUserName(String userName);
}
