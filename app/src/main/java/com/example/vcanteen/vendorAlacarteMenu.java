package com.example.vcanteen;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class vendorAlacarteMenu {
    public vendorInfo getVendorInfo() {
        return vendorInfo;
    }

    public boolean isHasCombination() {
        return hasCombination;
    }

    public int getMinCombinationPrice() {
        return minCombinationPrice;
    }

//    public String getVendorImage() {
//        return vendorImage;
//    }

    @SerializedName("vendor")
    public vendorInfo vendorInfo;
    @SerializedName("availableList")
    public ArrayList<availableList> availableList;
    @SerializedName("soldOutList")
    public ArrayList<soldOutList> soldOutList;
    @SerializedName("hasCombination")
    public boolean hasCombination;
    @SerializedName("minCombinationPrice")
    public int minCombinationPrice;
//    @SerializedName("vendorImage")
//    public String vendorImage;

}
