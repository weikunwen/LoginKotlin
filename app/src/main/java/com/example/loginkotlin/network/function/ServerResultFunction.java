package com.example.loginkotlin.network.function;

import android.text.TextUtils;

import com.example.loginkotlin.network.exception.ServerException;
import com.example.loginkotlin.network.retrofit.BaseResponse;

import io.reactivex.functions.Function;

/**
 * 服务器结果处理函数
 *
 */
public class ServerResultFunction<T> implements Function<BaseResponse<T>, BaseResponse<T>> {

    @Override
    public BaseResponse<T> apply(BaseResponse<T> tBaseResponse){

        if (!tBaseResponse.isSuccess()) {
            if (TextUtils.isEmpty(tBaseResponse.getMessage())){
                tBaseResponse.setMessage("message = null , code = "+tBaseResponse.getCode());
            }
            throw new ServerException(tBaseResponse.getCode(), tBaseResponse.getMessage());
        }
        return tBaseResponse;
    }




 /*   @Override
    public Object apply(@NonNull HttpResponse response) throws Exception {
        //打印服务器回传结果
        LogUtils.e(response.toString());
        if (!response.isSuccess()) {
            throw new ServerException(response.getCode(), response.getMsg());
        }
        return new Gson().toJson(response.getResult());
    }*/
}
