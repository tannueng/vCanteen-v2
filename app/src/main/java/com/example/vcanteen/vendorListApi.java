package com.example.vcanteen;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface vendorListApi {

    @GET("v1/customer-main/main")
    Call<List<vendorList>> getVendorList();
}
