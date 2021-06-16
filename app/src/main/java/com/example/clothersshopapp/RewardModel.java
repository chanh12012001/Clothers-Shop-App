package com.example.clothersshopapp;

public class RewardModel {

    private int iconCoupen;
    private String title;
    private String coupenBody;
    private String expiryDate;

    public RewardModel(int iconCoupen, String title, String coupenBody, String expiryDate) {
        this.iconCoupen = iconCoupen;
        this.title = title;
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
