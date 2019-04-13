package com.example.vcanteen.Data;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class TokenResponse {

    @SerializedName("customer_id")
    @Nullable
    private int custID;
    @SerializedName("token")
    @Nullable
    private String token;
    @SerializedName("status")
    private String status;

    public int getCustID() {
        return custID;
    }

    public String getToken() {
        return token;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "TokenResponse{" +
                "custID='" + custID + '\'' +
                ", token='" + token + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
