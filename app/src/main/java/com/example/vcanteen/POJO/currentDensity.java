package com.example.vcanteen.POJO;

import com.google.gson.annotations.SerializedName;

public class currentDensity {
    @SerializedName("percentDensity")
    public int percentDensity;


    @SerializedName("latestTime")
    public String latestTime;

    public int getPercentDensity() {
        return percentDensity;
    }

    public void setPercentDensity(int percentDensity) {
        this.percentDensity = percentDensity;
    }

    public String getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(String latestTime) {
        this.latestTime = latestTime;
    }
}
