package com.example.vcanteen;

public class orderListData {
    public String orderId;
    public String orderPrice;
    public String orderName;
    public String orderNameExtra;
    public String vendorName;
    public String orderDate;
    public String orderStatus;
    public int type;

    public static final int COOKING_TYPE = 0;
    public static final int DONE_TYPE = 1;

    orderListData(String orderId, String orderPrice, String orderName, String orderNameExtra, String vendorName, String orderDate, String orderStatus, int type) {
        this.orderId = orderId;
        this.orderPrice = orderPrice;
        this.orderName = orderName;
        this.orderNameExtra = orderNameExtra;
        this.vendorName = vendorName;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderNameExtra() {
        return orderNameExtra;
    }

    public void setOrderNameExtra(String orderNameExtra) {
        this.orderNameExtra = orderNameExtra;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }




//    orderListData()
}
