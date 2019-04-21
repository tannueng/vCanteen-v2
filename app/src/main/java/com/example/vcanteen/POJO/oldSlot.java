package com.example.vcanteen.POJO;

import com.google.gson.annotations.SerializedName;

public class oldSlot {
    public int getOldSlot() {
        return oldSlot;
    }

    public void setOldSlot(int oldSlot) {
        this.oldSlot = oldSlot;
    }

    @SerializedName("slotId")
    public int oldSlot;
}
