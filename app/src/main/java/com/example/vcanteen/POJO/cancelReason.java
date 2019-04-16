package com.example.vcanteen.POJO;

import com.google.gson.annotations.SerializedName;

public class cancelReason {

    @SerializedName("cancelReason")
    public String cancelReason;


    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
