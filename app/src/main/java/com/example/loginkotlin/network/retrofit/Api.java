package com.example.loginkotlin.network.retrofit;


import android.util.SparseArray;

import com.blankj.utilcode.util.PathUtils;
import com.example.loginkotlin.network.api.ApiService;
import com.example.loginkotlin.network.interceptor.BaseInterceptor;
import com.example.loginkotlin.network.interceptor.CacheInterceptor;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.net.Proxy;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.loginkotlin.BuildConfig;

public class Api {

    public static final int HOST_TYPE_NORMAL = 1;
    public static final int HOST_TYPE_MONTHLY = 2;//用户已登录且token不是空就使用
    public static final int HOST_TYPE_SZSERVER = 3;

    //读超时长，单位：秒
    public static final int READ_TIME_OUT = 10;
    //连接时长，单位：秒
    public static final int CONNECT_TIME_OUT = 10;
    //同一host并发数
    public static final int MAX_REQUESTS_PER_HOST = 64;

    private Retrofit retrofit;
    private ApiService apiService;
    private OkHttpClient okHttpClient;
    private static SparseArray<Api> sRetrofitManager = new SparseArray<>(2);

    /*************************缓存设置*********************/
/*
   1. noCache 不使用缓存，全部走网络

    2. noStore 不使用缓存，也不存储缓存

    3. onlyIfCached 只使用缓存

    4. maxAge 设置最大失效时间，失效则不使用 需要服务器配合

    5. maxStale 设置最大失效时间，失效则不使用 需要服务器配合 感觉这两个类似 还没怎么弄清楚，清楚的同学欢迎留言

    6. minFresh 设置有效时间，依旧如上

    7. FORCE_NETWORK 只走网络

    8. FORCE_CACHE 只走缓存*/

    /**
     * 设缓存有效期为两天
     */
//    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    public Api() {
        this(HOST_TYPE_NORMAL, null);
    }

    //构造方法私有
    public Api(int hostType, Map<String, String> headers) {
        //开启Log
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //缓存
        File cacheFile = new File(PathUtils.getExternalAppDocumentsPath() + File.separator + "cache" + File.separator, "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 10L); //100Mb

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)//默认重试一次，若需要重试N次，则要实现拦截器。
                .addInterceptor(new CacheInterceptor())
                .addNetworkInterceptor(new CacheInterceptor())//网络拦截器，在网络畅通的时候会调用
                .addNetworkInterceptor(new StethoInterceptor())//抓包
                .addInterceptor(logInterceptor)
                .addInterceptor(new BaseInterceptor())
                .cache(cache);

        if (!BuildConfig.DEBUG) {
            builder.proxy(Proxy.NO_PROXY);
        } else {
            //允许访问所有服务器
            builder.hostnameVerifier((String hostname, SSLSession session) -> true);
        }

        okHttpClient = builder.build();
        okHttpClient.dispatcher().setMaxRequestsPerHost(MAX_REQUESTS_PER_HOST);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))//设置数据解析器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BuildConfig.host)
                .build();
        apiService = retrofit.create(ApiService.class);
    }


    public static ApiService getDefault(int hostType) {
        return getDefault(hostType, null);
    }

    public static ApiService getDefault() {
        return getDefault(Api.HOST_TYPE_NORMAL, null);
    }

    public static ApiService getDefault(int hostType, Map<String, String> headers) {
        Api retrofitManager = sRetrofitManager.get(hostType);
        synchronized (Api.class) {
            if (retrofitManager == null) {
                retrofitManager = new Api(hostType, headers);
                sRetrofitManager.put(hostType, retrofitManager);
            }
        }
        return retrofitManager.apiService;
    }

}