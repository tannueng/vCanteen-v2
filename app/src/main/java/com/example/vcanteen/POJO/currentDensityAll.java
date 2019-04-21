package com.example.vcanteen.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class currentDensityAll implements Parcelable {
    @SerializedName("percentDensity")
    public int percentDensity;

    @SerializedName("latestTime")
    public String latestTime;

    @SerializedName("hourlyCrowdStat")
    public ArrayList<hourlyCrowdStat> hourlyCrowdStat;

    protected currentDensityAll(Parcel in) {
        percentDensity = in.readInt();
        latestTime = in.readString();
    }

    public static final Creator<currentDensityAll> CREATOR = new Creator<currentDensityAll>() {
        @Override
        public currentDensityAll createFromParcel(Parcel in) {
            return new currentDensityAll(in);
        }

        @Override
        public currentDensityAll[] newArray(int size) {
            return new currentDensityAll[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(percentDensity);
        dest.writeString(latestTime);
    }

    @Override
    public String toString() {
        return "test1111";
    }
}
