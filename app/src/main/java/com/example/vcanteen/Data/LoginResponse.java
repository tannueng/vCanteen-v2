package com.example.vcanteen.Data;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("customerId")
    private int customerId;

    @SerializedName("customerSessionToken")
    private String customerSessionToken;

    @SerializedName("accountType")
    public String accountType;


    public String getAccountType() {        return accountType;    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }


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
    @Override
    public String toString() {
        return "TokenResponse{" +
                "customerId='" + customerId + '\'' +
                ", customerSessionToken='" + customerSessionToken + '\'' +
                ", accountType='" + accountType + '\'' +
                '}';
    }
}
