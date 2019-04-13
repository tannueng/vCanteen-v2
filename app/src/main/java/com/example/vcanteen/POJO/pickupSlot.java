package com.example.vcanteen.POJO;

import com.google.gson.annotations.SerializedName;

public class pickupSlot {

    @SerializedName("slotID")
    private int pickupSlot;

    public int getPickupSlot() {
        return pickupSlot;
    }
}
