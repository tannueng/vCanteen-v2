package com.example.vcanteen.POJO;

import com.google.gson.annotations.SerializedName;

public class orderStatus {
    @SerializedName("orderStatus")
    private String orderStatus;

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public orderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
