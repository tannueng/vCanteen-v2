package com.example.vcanteen.POJO;

import com.google.gson.annotations.SerializedName;

public class orderHistory {
    @SerializedName("orderId")
    private int orderId;

    @SerializedName("orderName")
    private String orderName;

    @SerializedName("orderNameExtra")
    private String orderNameExtra;

    @SerializedName("orderPrice")
    private int orderPrice;

    @SerializedName("restaurantName")
    private String restaurantName;

    @SerializedName("orderStatus")
    private String orderStatus;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("hasRated")
    private boolean hasRated;


//    private orderListData data;
//
//    public orderListData getData() {
//        return data;
//    }


    public int getOrderId() {
        return orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public String getOrderNameExtra() {
        return orderNameExtra;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public boolean isHasRated() {
        return hasRated;
    }
}
