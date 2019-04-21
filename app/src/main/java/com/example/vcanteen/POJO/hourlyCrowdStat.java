package com.example.vcanteen.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class hourlyCrowdStat implements Parcelable {

    @SerializedName("hourOfDay")
    String hourOfDay;
    @SerializedName("crowdStat")
    int crowdStat;

    public hourlyCrowdStat(String hourOfDay, int crowdStat) {
        this.hourOfDay = hourOfDay;
        this.crowdStat = crowdStat;
    }

    protected hourlyCrowdStat(Parcel in) {
        hourOfDay = in.readString();
        crowdStat = in.readInt();
    }

    public static final Creator<hourlyCrowdStat> CREATOR = new Creator<hourlyCrowdStat>() {
        @Override
        public hourlyCrowdStat createFromParcel(Parcel in) {
            return new hourlyCrowdStat(in);
        }

        @Override
        public hourlyCrowdStat[] newArray(int size) {
            return new hourlyCrowdStat[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hourOfDay);
        dest.writeInt(crowdStat);
    }
}
