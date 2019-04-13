package com.example.vcanteen;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class order implements Parcelable {

    String orderName;
    //For combination, combination_base comes before combination_main
    //for the same food_type, sort alphabetically
    //ask for the code of forming orderName from BE
    String orderNameExtra;
    int orderPrice;
    //food foodList[];
    ArrayList<food> foodList;

    public order(String orderName, String orderNameExtra, int orderPrice, ArrayList<food> foodList) {
        this.orderName = orderName;
        this.orderNameExtra = orderNameExtra;
        this.orderPrice = orderPrice;
        this.foodList = foodList;
    }

    protected order(Parcel in) {
        orderName = in.readString();
        orderNameExtra = in.readString();
        orderPrice = in.readInt();
        foodList = in.readArrayList(food.class.getClassLoader());
    }

    public static final Creator<order> CREATOR = new Creator<order>() {
        @Override
        public order createFromParcel(Parcel in) {
            return new order(in);
        }

        @Override
        public order[] newArray(int size) {
            return new order[size];
        }
    };

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderNameExtra() {
        return orderNameExtra;
    }

    public void setOrderNameExtra(String orderNameExtra) {
        this.orderNameExtra = orderNameExtra;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public ArrayList<food> getFoodList() {
        return foodList;
    }

    public void setFoodList(ArrayList<food> foodList) {
        this.foodList = foodList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderName);
        dest.writeString(orderNameExtra);
        dest.writeInt(orderPrice);
        dest.writeList(foodList);
    }

    @Override
    public String toString() {
        return  "{"+
                "\"orderName\": "+"\""+orderName+"\""+
                ", \"orderNameExtra\": "+"\""+orderNameExtra+"\""+
                ", \"orderPrice\": "+orderPrice+
                ", \"foodList\": "+foodList.toString()+
                "}"

                ;
    }
}
