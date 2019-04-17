package com.example.vcanteen.POJO;

import com.google.gson.annotations.SerializedName;

public class hourlyCrowdStat {

    @SerializedName("hourOfDay")
    String hourOfDay;
    @SerializedName("crowdStat")
    int crowdStat;

    public hourlyCrowdStat(String hourOfDay, int crowdStat) {
        this.hourOfDay = hourOfDay;
        this.crowdStat = crowdStat;
    }

    public String getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(String hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public int getCrowdStat() {
        return crowdStat;
    }

    public void setCrowdStat(int crowdStat) {
        this.crowdStat = crowdStat;
    }
}
