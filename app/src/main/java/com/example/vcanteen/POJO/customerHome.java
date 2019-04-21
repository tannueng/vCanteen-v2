package com.example.vcanteen.POJO;

import com.google.gson.annotations.SerializedName;

public class customerHome {
    public customerInfo getCustomerInfo() {
        return customerInfo;
    }

    public int getPercentDensity() {
        return percentDensity;
    }

    public recommendationInfo getRecommendationInfo() {
        return recommendationInfo;
    }

    @SerializedName("customer")
    public customerInfo customerInfo;

    @SerializedName("percentDensity")
    public int percentDensity;

    @SerializedName("recommend")
    public recommendationInfo recommendationInfo;
}
