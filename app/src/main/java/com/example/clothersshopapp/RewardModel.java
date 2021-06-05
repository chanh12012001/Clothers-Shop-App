package com.example.clothersshopapp;

public class RewardModel {

    private int iconCoupen;
    private String title;
    private String discountPrice;
    private String coupenBody;
    private String expiryDate;

    public RewardModel(int iconCoupen, String title, String discountPrice, String coupenBody, String expiryDate) {
        this.iconCoupen = iconCoupen;
        this.title = title;
        this.discountPrice = discountPrice;
        this.coupenBody = coupenBody;
        this.expiryDate = expiryDate;
    }

    public int getIconCoupen() {
        return iconCoupen;
    }

    public void setIconCoupen(int iconCoupen) {
        this.iconCoupen = iconCoupen;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getCoupenBody() {
        return coupenBody;
    }

    public void setCoupenBody(String coupenBody) {
        this.coupenBody = coupenBody;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
