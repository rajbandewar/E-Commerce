package com.ecommerce.model;

public class OrderedProduct {
    int orderId;
    int productId;
    int quantity;
    double price;
    String description;
    public OrderedProduct(double price, int quantity, int productId, int orderId) {
        this.price = price;
        this.quantity = quantity;
        this.productId = productId;
        this.orderId = orderId;
    }

    public OrderedProduct() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderedProduct{" +
                "orderId=" + orderId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
