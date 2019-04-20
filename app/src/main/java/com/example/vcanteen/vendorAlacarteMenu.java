package com.example.vcanteen;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class vendorAlacarteMenu implements Parcelable {
    protected vendorAlacarteMenu(Parcel in) {
        hasCombination = in.readByte() != 0;
        minCombinationPrice = in.readInt();
    }

    public vendorAlacarteMenu() {

    }
    public static final Creator<vendorAlacarteMenu> CREATOR = new Creator<vendorAlacarteMenu>() {
        @Override
        public vendorAlacarteMenu createFromParcel(Parcel in) {
            return new vendorAlacarteMenu(in);
        }

        @Override
        public vendorAlacarteMenu[] newArray(int size) {
            return new vendorAlacarteMenu[size];
        }
    };

    public vendorInfo getVendorInfo() {
        return vendorInfo;
    }

    public boolean isHasCombination() {
        return hasCombination;
    }

    public int getMinCombinationPrice() {
        return minCombinationPrice;
    }

//    public String getVendorImage() {
//        return vendorImage;
//    }

    @SerializedName("vendor")
    public vendorInfo vendorInfo;
    @SerializedName("availableList")
    public ArrayList<availableList> availableList;
    @SerializedName("soldOutList")
    public ArrayList<soldOutList> soldOutList;
    @SerializedName("hasCombination")
    public boolean hasCombination;
    @SerializedName("minCombinationPrice")
    public int minCombinationPrice;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (hasCombination ? 1 : 0));
        dest.writeInt(minCombinationPrice);
    }
//    @SerializedName("vendorImage")
//    public String vendorImage;

}
