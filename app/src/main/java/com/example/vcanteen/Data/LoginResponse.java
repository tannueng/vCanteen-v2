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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @SerializedName("accountType")
    public String accountType;

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
