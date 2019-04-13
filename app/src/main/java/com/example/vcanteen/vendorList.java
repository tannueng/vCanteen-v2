package com.example.vcanteen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class vendorList {

    @SerializedName("vendorId")
    @Expose
    private int vendorId;
    @SerializedName("restaurantName")
    @Expose
    private String restaurantName;
    @SerializedName("restaurantNumber")
    @Expose
    private int restaurantNumber;
    @SerializedName("vendorImage")
    @Expose
    private String vendorImage;
    @SerializedName("vendorStatus")
    @Expose
    private String vendorStatus;
    public vendorList(int vendorId, String restaurantName, int restaurantNumber,String vendorImage, String vendorStatus) {
        super();
        this.vendorId = vendorId;
        this.restaurantName = restaurantName;
        this.restaurantNumber = restaurantNumber;
        this.vendorImage = vendorImage;
        this.vendorStatus = vendorStatus;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public int getRestaurantNumber() {
        return restaurantNumber;
    }

    public void setRestaurantNumber(int restaurantNumber) {
        this.restaurantNumber = restaurantNumber;
    }

    public String getVendorImage() {
        return vendorImage;
    }

    public void setVendorImage(String vendorImage) {
        this.vendorImage = vendorImage;
    }

    public String getVendorStatus() {
        return vendorStatus;
    }

    public void setVendorStatus(String vendorStatus) {
        this.vendorStatus = vendorStatus;
    }



}
