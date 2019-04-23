package com.example.vcanteen.Data;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @SerializedName("customerId")
    private int customerId;

    @SerializedName("customerSessionToken")
    private String customerSessionToken;

    public LoginResponse(String customerSessionToken, int customerId) {
        this.customerSessionToken = customerSessionToken;
        this.customerId = customerId;
    }

    public String getCustomerSessionToken() {
        return customerSessionToken;
    }

    public void setCustomerSessionToken(String customerSessionToken) {
        this.customerSessionToken = customerSessionToken;
    }
}
