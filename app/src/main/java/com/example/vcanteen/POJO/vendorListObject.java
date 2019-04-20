package com.example.vcanteen.POJO;

import com.example.vcanteen.vendorList;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class vendorListObject {
    public ArrayList<com.example.vcanteen.vendorList> getVendorList() {
        return vendorList;
    }

    public void setVendorList(ArrayList<com.example.vcanteen.vendorList> vendorList) {
        this.vendorList = vendorList;
    }

    @SerializedName("vendorList")
    public ArrayList<com.example.vcanteen.vendorList> vendorList;
}
