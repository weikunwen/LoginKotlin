package com.example.loginkotlin.network.observer

import com.example.loginkotlin.network.function.HttpResultFunction
import com.example.loginkotlin.network.function.ServerResultFunction
import com.example.loginkotlin.network.retrofit.BaseResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 适用Retrofit网络请求Observable(被监听者)
 */
object HttpRxObservableKt {
    /**
     * 获取被监听者
     * 备注:网络请求Observable构建
     * data:网络请求参数
     */
    fun <T> getObservable(apiObservable: Observable<BaseResponse<T>>?): Observable<BaseResponse<T>>? {
        return apiObservable?.map<BaseResponse<T>>(ServerResultFunction())
            ?.onErrorResumeNext(HttpResultFunction())
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(Schedulers.io())
    }

    /**
     * observeOn主线程
     */
    fun <T> getObservableOnMain(apiObservable: Observable<BaseResponse<T>>?): Observable<BaseResponse<T>>? {
        return apiObservable?.map<BaseResponse<T>>(ServerResultFunction())
            ?.onErrorResumeNext(HttpResultFunction())
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
    }
}