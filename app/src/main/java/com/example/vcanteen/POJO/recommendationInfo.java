package com.example.vcanteen.POJO;

public class recommendationInfo {

    private int vendorId;
    private String vendorImage;
    private String restaurantName;
    private String foodImage;
    private String foodName;

    public int getRecVendorId() {
        return vendorId;
    }

    public void setRecVendorId(int recVendorId) {
        this.vendorId = recVendorId;
    }

    public String getRecVendorImage() {
        return vendorImage;
    }

    public void setRecVendorImage(String recVendorImage) {
        this.vendorImage = recVendorImage;
    }

    public String getRecRestaurantName() {
        return restaurantName;
    }

    public void setRecRestuarantName(String recRestuarantName) {
        this.restaurantName = recRestuarantName;
    }

    public String getRecFoodImage() {
        return foodImage;
    }

    public void setRecFoodImage(String recFoodImage) {
        this.foodImage = recFoodImage;
    }

    public String getRecFoodName() {
        return foodName;
    }

    public void setRecFoodName(String foodName) {
        this.foodName = foodName;
    }
}

