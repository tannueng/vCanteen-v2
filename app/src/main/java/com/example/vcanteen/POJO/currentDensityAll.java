package com.example.vcanteen.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class currentDensityAll {
    @SerializedName("percentDensity")
    public int percentDensity;

    @SerializedName("latestTime")
    public String latestTime;

    @SerializedName("hourlyCrowdStat")
    public ArrayList<hourlyCrowdStat> hourlyCrowdStat;

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

    public ArrayList<com.example.vcanteen.POJO.hourlyCrowdStat> getHourlyCrowdStat() {
        return hourlyCrowdStat;
    }

    public void setHourlyCrowdStat(ArrayList<com.example.vcanteen.POJO.hourlyCrowdStat> hourlyCrowdStat) {
        this.hourlyCrowdStat = hourlyCrowdStat;
    }
}
