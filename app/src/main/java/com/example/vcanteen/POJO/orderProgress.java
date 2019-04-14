package com.example.vcanteen.POJO;

import com.google.gson.annotations.SerializedName;

public class orderProgress {
    @SerializedName("orderId")
    private int orderId;

    @SerializedName("orderName")
    private String orderName;

    @SerializedName("orderNameExtra")
    private String orderNameExtra;

    @SerializedName("foodImage")
    private String foodImage;

    @SerializedName("orderPrice")
    private int orderPrice;

    @SerializedName("restaurantName")
    private String restaurantName;

    @SerializedName("restaurantNumber")
    private int restaurantNumber; //number or id

    @SerializedName("orderStatus")
    private String orderStatus;

    @SerializedName("createdAt")
    private String createdAt;


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

    public String getFoodImage() {
        return foodImage;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public int getRestaurantNumber() {
        return restaurantNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
