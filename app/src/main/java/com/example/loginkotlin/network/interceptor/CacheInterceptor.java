package com.example.loginkotlin.network.interceptor;

import com.blankj.utilcode.util.NetworkUtils;

import java.io.IOException;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CacheInterceptor implements Interceptor {

    //@Headers("Cache-Control:public,max-age=120")
    // 通过添加 @Headers("Cache-Control: max-age=120") 进行设置。
    // 添加了Cache-Control 的请求，retrofit 会默认缓存该请求的返回数据一般来说，这种方法是针对特定的API进行设置。

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (NetworkUtils.isConnected()) {
            Response response = chain.proceed(request);
            // 缓存时间 0为不使用缓存
            int maxAge = 0;
           // String cacheControl = request.cacheControl().toString();
//            EvLog.e("CacheInterceptor 60s load cahe" + cacheControl);
            return response.newBuilder()
                    .removeHeader("Pragma")//// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Response response = chain.proceed(request);
            //set cahe times is 3 days
            int maxStale = 60 * 60 * 24 * 3;
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
    }
}
