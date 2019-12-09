package com.mtsealove.github.boxlinker_driver.Restful;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitService {

    @POST("/driver/signUp")
    Call<Res> SignUp(@Body ReqSignUp reqSignUp);

    @POST("/driver/login")
    Call<ResLogin> Login(@Body ReqLogin reqLogin);
}
