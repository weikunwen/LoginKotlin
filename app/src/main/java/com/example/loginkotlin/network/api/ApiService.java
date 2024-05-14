package com.example.loginkotlin.network.api;

import com.example.loginkotlin.entity.UserInfo;
import com.example.loginkotlin.entity.VerifyCodeBean;
import com.example.loginkotlin.network.retrofit.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("my/user/login")
    Observable<BaseResponse<UserInfo>> login(
            @Body RequestBody body);

    @POST("my/user/authlogin")
    Observable<BaseResponse<UserInfo>> authLogin(
            @Body RequestBody body);

    @POST("my/smscode/send")
    Observable<BaseResponse<VerifyCodeBean>> getVerifyCode(@Body RequestBody body);

    @POST("/user/agreement/check")//同意隐私协议
    Observable<BaseResponse<Object>> onAgreePrivate(@Body RequestBody body);
}