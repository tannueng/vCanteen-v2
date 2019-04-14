package com.example.vcanteen;

import com.example.vcanteen.POJO.menuExtra;
import com.example.vcanteen.POJO.newOrder;
import com.example.vcanteen.POJO.orderHistory;
import com.example.vcanteen.POJO.orderProgress;
import com.example.vcanteen.POJO.orderStatus;
import com.example.vcanteen.POJO.paymentMethod;
import com.example.vcanteen.POJO.pickupSlot;
import com.example.vcanteen.Data.Customers;
import com.example.vcanteen.Data.RecoverPass;
import com.example.vcanteen.Data.ResetPass;
import com.example.vcanteen.Data.Token;
import com.example.vcanteen.Data.TokenResponse;
import com.example.vcanteen.Data.TokenVerification;
import com.example.vcanteen.POJO.vendorCombinationMenu;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Body;

public interface JsonPlaceHolderApi {

    @GET("v1/orders/customers/{customerId}/history")
    Call<List<orderHistory>> getHistory(@Path("customerId") int customerId);

    @GET("v1/orders/customers/{customerId}/in-progress")
    Call<List<orderProgress>> getProgress(@Path("customerId") int customerId);

    @GET("/v1/orders/{orderId}/slot")
    Call<pickupSlot> getPickupSlot(@Path("orderId") int orderId);

    @PUT("v1/orders/{orderId}/status/collected")
    Call<orderStatus> putOrderStatus(@Path("orderId") int orderId);

    @POST("v1/user-authentication/customer/check/token")
    Call<TokenResponse> createCustomer(@Body Customers customers);

    @POST("v1/user-authentication/customer/verify/token")
    Call<TokenVerification> verifyToken(@Body Token token);

    @PUT("v1/user-authentication/customer/password/recover")
    Call<Void> recoverPass(@Body RecoverPass email);

    @PUT("v1/user-authentication/customer/password/change")
    Call<Void> resetPass(@Body ResetPass object);

    @PUT("v1/user-authentication/customer/verify/email")
    Call<Void> verifyEmail(@Body RecoverPass recoverPass);

    @GET("v1/orders/{vendorId}/menu")
    Call<vendorAlacarteMenu> getVendorAlacarte(@Path("vendorId") int vendorId);

    @GET("v1/orders/{vendorId}/combination")
    Call<vendorCombinationMenu> getVendorCombination(@Path("vendorId") int vendorId);

    @GET("v1/orders/{customerId}/payment-method")
    Call<paymentMethod> getPaymentMethod(@Path("customerId") int customerId);

    @POST("v1/orders/new")
    Call<newOrder> postOrder(@Body newOrder newOrder);

    @GET("v1/orders/{vendorId}/menu/{foodId}")
    Call<menuExtra> getMenuExtra(@Path("vendorId") int vendorId, @Path("foodId") int foodId);

}