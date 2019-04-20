package com.example.vcanteen;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class availableList {

    int foodId;
    String foodName;
    int foodPrice;
    String foodCategory;

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }


    /*@Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(foodId);
        dest.writeInt(foodPrice);
        dest.writeString(foodName);
    }
    public static final Creator<availableList> CREATOR = new Creator<availableList>() {
        @Override
        public availableList createFromParcel(Parcel in) {
            return new availableList();
        }

        @Override
        public availableList[] newArray(int size) {
            return new availableList[size];
        }
    };*/
}

