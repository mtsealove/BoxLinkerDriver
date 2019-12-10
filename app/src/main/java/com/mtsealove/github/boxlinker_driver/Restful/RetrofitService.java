package com.mtsealove.github.boxlinker_driver.Restful;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface RetrofitService {

    @POST("/driver/signUp")
    Call<Res> SignUp(@Body ReqSignUp reqSignUp);

    @POST("/driver/login")
    Call<ResLogin> Login(@Body ReqLogin reqLogin);

    @GET("/driver/Get/newOrderList")
    Call<List<ResOrderSm>> GetNewOrders(@Query("Latitude") double latitude, @Query("Longitude") double longitude);

    @POST("/driver/takeOrder")
    Call<Res> TakeOrder(@Body ReqTakeOrder reqTakeOrder);

    @GET("/driver/Get/myOrderList")
    Call<List<ResOrder>> GetMyOrders(@Query("Phone") String phone);

    @POST("/driver/Update/Status")
    Call<Res> UpdateStatus(@Body ReqOrderStatus reqOrderStatus);

    @POST("/driver/Update/Location")
    Call<Res> UpdateLocation(@Body ReqLatLng reqLatLng);

    @GET("/driver/Get/recent")
    Call<ResRecent> GetRecent(@Query("phone") String phone);

    @GET("/driver/Get/Last")
    Call<List<ResOrder>> GetLastOrders(@Query("Phone") String phone);
}
