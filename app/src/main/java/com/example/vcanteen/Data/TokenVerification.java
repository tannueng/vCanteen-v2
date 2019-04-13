package com.example.vcanteen.Data;

import com.google.gson.annotations.SerializedName;

public class TokenVerification {

    @SerializedName("expired")
    private boolean isExpired;

    public boolean isExpired() {
        return isExpired;
    }
}
