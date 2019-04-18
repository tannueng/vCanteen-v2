package com.example.vcanteen.POJO;

import com.google.gson.annotations.SerializedName;

public class customerHome {
    public customerInfo getCustomerInfo(){
        return customerInfo;
    }

    @SerializedName("customer")
    public customerInfo customerInfo;

}
