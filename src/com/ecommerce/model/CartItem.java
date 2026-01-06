package com.ecommerce.model;

public class CartItem {
    int cartItemId;
    int userId;
    Product product;
    int quantity;

    public CartItem(int cartItemId, int userId, Product product, int quantity) {
        this.cartItemId = cartItemId;
        this.userId = userId;
        this.product = product;
        this.quantity = quantity;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartItemId=" + cartItemId +
                ", cartId=" + userId +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
