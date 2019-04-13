package com.example.vcanteen.POJO;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import  com.example.vcanteen.food;

public class menuExtra {

    @SerializedName("food")
    public food food;


    @SerializedName("extraList")
    public ArrayList<extraList> extraList;
}
