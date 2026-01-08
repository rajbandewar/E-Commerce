package com.ecommerce.logic;

import com.ecommerce.dao.CartDao;
import com.ecommerce.dao.OrderDao;
import com.ecommerce.dao.ProductDao;
import com.ecommerce.dao.impl.CartDaoImpl;
import com.ecommerce.dao.impl.OrderDaoImpl;
import com.ecommerce.dao.impl.ProductDaoImpl;
import com.ecommerce.exception.InvalidInputException;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.OrderedProduct;
import com.ecommerce.model.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderLogic {
    OrderDao orderDao = new OrderDaoImpl();
    ProductDao productDao=new ProductDaoImpl();
    CartDao cartDao =new CartDaoImpl();
   public void orderProduct(int userId,int productId,int quantity) {
       Product product = productDao.getProductById(productId);
       if (product.getQuantity() < quantity){
           throw new InvalidInputException("Only "+product.getQuantity() +" "+ product.getProductName() +" Available ");
       }
       orderDao.orderProduct(userId,new OrderedProduct(product.getPrice(),quantity,productId,0) );
    }

    void orderProducts(int userId, List<OrderedProduct> orderedProducts) {
        double totalBill = orderedProducts.stream()
                .mapToDouble((product) -> product.getPrice() * product.getQuantity())
                .sum();
        orderDao.orderProducts(userId, totalBill, orderedProducts);
    }

    public void orderCart(int userId){
        List<OrderedProduct> orderedProducts =new ArrayList<>();
        List<CartItem> cartItems = cartDao.getCartItems(userId);
        if (cartItems.isEmpty()) {
            throw new InvalidInputException("No items in cart");
        }
        for(CartItem ci:cartItems){
            if (ci.getProduct().getQuantity() < ci.getQuantity()) {
                throw new InvalidInputException("Only "+ci.getProduct().getQuantity() +" "+ ci.getProduct().getProductName() +" Available ");
            }
            orderedProducts.add(new OrderedProduct(ci.getProduct().getPrice(),ci.getQuantity(),ci.getProduct().getProductId(),0));
        }
        orderProducts(userId,orderedProducts);
    }

    public List<OrderedProduct> getUserOrderHistory(String userName){
       return orderDao.getOrderHistoryByUserName(userName);
    }
}
