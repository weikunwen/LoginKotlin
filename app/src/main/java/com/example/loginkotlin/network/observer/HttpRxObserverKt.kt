package com.example.loginkotlin.network.observer

import com.example.loginkotlin.network.retrofit.RxDisposeManager
import com.example.loginkotlin.network.exception.ApiException
import com.example.loginkotlin.network.exception.ExceptionEngine
import com.example.loginkotlin.network.retrofit.BaseResponse
import com.example.loginkotlin.network.retrofit.HttpRequestListener
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 * 适用Retrofit网络请求Observer(监听者)
 * 备注:
 * 1.重写onSubscribe，添加请求标识
 * 2.重写onError，封装错误/异常处理，移除请求
 * 3.重写onNext，移除请求
 * 4.重写cancel，取消请求
 */
abstract class HttpRxObserverKt<T> protected constructor() : Observer<BaseResponse<T>>,
    HttpRequestListener {
    private val mTag = "${System.currentTimeMillis()}${Math.random()}"  //请求标识

    init {
        Timber.d("----------mTag:${mTag}")
    }

    override fun onError(e: Throwable) {
        RxDisposeManager.remove(mTag)

        if (e is ApiException) {
            when (e.code) {
                //登录过期
                401, 411, 412 -> {
//                    BuProcessor.getInstance().clearUser()
//                    EventBus.getDefault().post(BusEventData(EventBusCode.TOKEN_ERROR, e.msg))
                }
                1501 -> {
//                    EventBus.getDefault()
//                        .post(BusEventData(EventBusCode.ACCOUNT_CANCELLATION_APPLY))
                }
                406 -> {  //签名验证失败，签名过期
//                    val message = e.msg
//                    message?.takeIf {  message.contains("#") }
//                        ?.let {
//                                message.split("#".toRegex()).dropLastWhile { it.isEmpty() }
//                                    .toTypedArray().also { it1 ->
//                                e.msg = it1[0]
//                                it1.takeIf { it1.size> 1 }?.let {
//                                    BuProcessor.getInstance().setTimeDifference(it1[1])
//                                }
//                            }
//                        }
                }
            }
            _onError(e)
        } else {
            _onError(ApiException(e, ExceptionEngine.ERROR.UNKNOWN))
        }
    }


    override fun onComplete() {
        RxDisposeManager.remove(mTag)
    }

    override fun onNext(tBaseResponse: BaseResponse<T>) {
//        MainHelper.getInstance().sendNetWorkChangeEvent(true)
        _onSuccess(tBaseResponse.data)
    }

    override fun onSubscribe(d: Disposable) {
        RxDisposeManager.add(mTag, d)
    }
    override fun cancel() {
        RxDisposeManager.remove(mTag)
    }

    /**
     * 错误/异常回调
     */
    protected abstract fun _onError(e: ApiException?)

    /**
     * 成功回调
     */
    protected abstract fun _onSuccess(data: T?)
}