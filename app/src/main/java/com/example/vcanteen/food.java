package com.example.vcanteen;

import android.os.Parcel;
import android.os.Parcelable;

import io.reactivex.annotations.Nullable;

public class food implements Parcelable {
    int foodId;
    String foodName;
    int foodPrice;
    String foodType;

    public food(int foodId, String foodName, int foodPrice, @Nullable String foodType) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodType = foodType;
    }

    @Override
    public String toString() {
        return "{ \"foodId\": "+"\""+foodId+"\""+
                ", \"foodName\": "+"\""+foodName+"\""+
                ", \"foodPrice\": "+"\""+foodPrice+"\""+
                ", \"foodType\": "+"\""+foodType+"\""+
                "}";
    }

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

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

////// MORE INFO ABT Parcel: https://coderwall.com/p/vfbing/passing-objects-between-activities-in-android

    /**
     * Use when reconstructing User object from parcel
     * This will be used only by the 'CREATOR'
     * @param in a parcel to read this object
     */
    public food(Parcel in) {
        foodId = in.readInt();
        foodName = in.readString();
        foodPrice = in.readInt();
        foodType = in.readString();
    }

    /**
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays
     *
     * If you don’t do that, Android framework will through exception
     * Parcelable protocol requires a Parcelable.Creator object called CREATOR
     */
    public static final Creator<food> CREATOR = new Creator<food>() {
        @Override
        public food createFromParcel(Parcel in) {
            return new food(in);
        }

        @Override
        public food[] newArray(int size) {
            return new food[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Actual object serialization happens here, Write object content
     * to parcel one by one, reading should be done according to this write order
     * @param dest parcel
     * @param flags Additional flags about how the object should be written
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(foodId);
        dest.writeString(foodName);
        dest.writeInt(foodPrice);
        dest.writeString(foodType);
    }


}
//old one without parcel

//package com.example.vcanteen;
//
//public class food {
//    int foodId;
//    String foodName;
//    int foodPrice;
//    String foodType;
//
//    public food(int foodId, String foodName, int foodPrice, String foodType) {
//        this.foodId = foodId;
//        this.foodName = foodName;
//        this.foodPrice = foodPrice;
//        this.foodType = foodType;
//    }
//
//    public int getFoodId() {
//        return foodId;
//    }
//
//    public void setFoodId(int foodId) {
//        this.foodId = foodId;
//    }
//
//    public String getFoodName() {
//        return foodName;
//    }
//
//    public void setFoodName(String foodName) {
//        this.foodName = foodName;
//    }
//
//    public int getFoodPrice() {
//        return foodPrice;
//    }
//
//    public void setFoodPrice(int foodPrice) {
//        this.foodPrice = foodPrice;
//    }
//
//    public String getFoodType() {
//        return foodType;
//    }
//
//    public void setFoodType(String foodType) {
//        this.foodType = foodType;
//    }
//
//
//}

