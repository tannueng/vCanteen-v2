package com.example.vcanteen.POJO;

import com.example.vcanteen.food;
import com.example.vcanteen.order;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class newOrder {

    @SerializedName("customerId")
    @Expose
    public int customerId;

    @SerializedName("vendorId")
    @Expose
    public int vendorId;

    @SerializedName("orderList")
    @Expose
    public ArrayList<order> order;

    @SerializedName("totalPrice")
    @Expose
    public int totalPrice;

//    @SerializedName("createdAt")
//    @Expose
//    public String createdAt;

    @SerializedName("customerMoneyAccountId")
    @Expose
    public int customerMoneyAccountId;

    @Override
    public String toString() {
        return "{" +
                "\"customerId\": " + customerId +
                ", \"vendorId\": " + vendorId +
                ", \"orderList\": " + order.toString() +
                ", \"totalPrice\": " + totalPrice +
//                ", \"createdAt\": " + createdAt +
                ", \"customerMoneyAccountId\": " + customerMoneyAccountId +
                '}';
    }

}
