package com.example.loginkotlin.network.function;

import com.example.loginkotlin.network.exception.ExceptionEngine;
import com.example.loginkotlin.network.retrofit.BaseResponse;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * http结果处理函数
 *
 */
public class HttpResultFunction<T> implements Function<Throwable, Observable<BaseResponse<T>>> {
   /* @Override
    public Observable<T> apply(@NonNull Throwable throwable) throws Exception {
        //打印具体错误
        EvLog.e("HttpResultFunction:" + throwable);
        return Observable.error(ExceptionEngine.handleException(throwable));
    }*/

    @Override
    public Observable<BaseResponse<T>> apply(Throwable throwable){
        return Observable.error(ExceptionEngine.handleException(throwable));
    }
}
