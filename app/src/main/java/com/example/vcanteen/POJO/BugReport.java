package com.example.vcanteen.POJO;

import com.google.gson.annotations.SerializedName;

public class BugReport {
    @SerializedName("customerId")
    public int customerId;
    @SerializedName("message")
    public String message;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
