package com.example.clothersshopapp;

public class HorizontalProductScrollModel {

    private String productID;
    private String productImage;
    private String productName;
    private String productPrice;
    private String productLocation;

    public HorizontalProductScrollModel(String productID,String productImage, String productName, String productPrice, String productLocation) {
        this.productID = productID;
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productLocation = productLocation;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
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
