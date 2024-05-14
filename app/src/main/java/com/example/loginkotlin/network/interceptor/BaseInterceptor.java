package com.example.loginkotlin.network.interceptor;

import java.io.IOException;
import java.util.Map;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class BaseInterceptor implements Interceptor {


    private Map<String, String> headers;

    public BaseInterceptor() {
    }

    public BaseInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originRequest = chain.request();
        Request.Builder builder = originRequest.newBuilder();

//        builder.addHeader("USER_TOKEN", BuProcessor.getInstance().isLogin() ?
//                BuProcessor.getInstance().getUserInfo().getToken() : "");
//        builder.addHeader("USER_DEVICE_ID", UUIDCache.INSTANCE.getUuid());
//        builder.addHeader("APP_VER", BuildConfig.VERSION_NAME);
//        builder.addHeader("PLAT_TYPE", "android");
//        //builder.addHeader("Connection", "keep-alive");
//        //android.os.Build.MODEL + "|" + android.os.Build.VERSION.RELEASE
//        builder.addHeader("PLAT_INFO",  Build.MANUFACTURER + "_" + Build.MODEL + "|" + Build.VERSION.RELEASE);
//        if (headers != null && headers.size() > 0) {
//            Set<String> keys = headers.keySet();
//            for (String headerKey : keys) {
//                builder.addHeader(headerKey, headers.get(headerKey)).build();
//            }
//        }
//
//        //接口URL
//        String requestUrl = originRequest.url().toString();
//        long timeStamp = System.currentTimeMillis() + BuProcessor.getInstance().getTimeDifference();
//        builder.addHeader("EV_TS", String.valueOf(timeStamp));
//        String signString = SHAUtil.getSignString(requestUrl, timeStamp, UUIDCache.INSTANCE.getUuid());
//        builder.addHeader("EV_SIGN", SHAUtil.encode(signString));
//        EvLog.e(signString);
//
//        LocationCity locationCity = BuProcessor.getInstance().getLocationCity();
//        if (locationCity != null) {
//            builder.addHeader("LAT", locationCity.getLat() + "");
//            builder.addHeader("LNG", locationCity.getLng() + "");
//            builder.addHeader("CITY_CODE", locationCity.getCityCode());
//        }
        originRequest = builder.build();
        return chain.proceed(originRequest);
    }

}