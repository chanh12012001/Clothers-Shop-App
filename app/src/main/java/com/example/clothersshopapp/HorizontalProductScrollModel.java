package com.example.clothersshopapp;

public class HorizontalProductScrollModel {

    private int productImage;
    private String productName;
    private String productPrice;
    private String productLocation;

    public HorizontalProductScrollModel(int productImage, String productName, String productPrice, String productLocation) {
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productLocation = productLocation;
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductLocation() {
        return productLocation;
    }

    public void setProductLocation(String productLocation) {
        this.productLocation = productLocation;
    }
}
