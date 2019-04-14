package com.example.vcanteen.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class vendorCombinationMenu {
    @SerializedName("baseList")
    public ArrayList<baseList> baseList;

    @SerializedName("mainList")
    public ArrayList<mainList> mainList;

    @SerializedName("extraList")
    public ArrayList<extraList> extraList;
}
