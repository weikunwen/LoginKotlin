package com.example.loginkotlin.network.retrofit

import androidx.collection.ArrayMap
import io.reactivex.disposables.Disposable
import java.util.Collections

/**
 * RxJavaAction管理实现类
 */
object RxDisposeManager {
    //处理请求列表
    private val mMaps = Collections.synchronizedMap(ArrayMap<String, Disposable>())

    fun add(tag: String, disposable: Disposable) {
        mMaps[tag] = disposable
//        Timber.d("-------------add key:${tag}, maps:${mMaps}")
    }

    fun remove(tag: String) {
//        Timber.d("-------------remove key:${tag}, maps:${mMaps}")
        mMaps.takeIf { mMaps.containsKey(tag) }.apply {
            mMaps[tag]?.let {
                dispose(it)
                mMaps.remove(tag)}
        }
    }

    fun dispose(disposable: Disposable?) {
        //disposable添加判空校验，防止CaptureActivity传入参数为null.
        disposable?.takeIf { !it.isDisposed }.apply { this?.dispose() }
    }

}