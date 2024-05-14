package com.example.loginkotlin.network.exception;

import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.HttpException;


/**
 * 错误/异常处理工具
 */
public class ExceptionEngine {

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {

            HttpException httpExc = (HttpException) e;
            ex = new ApiException(e, ERROR.HTTP_ERROR);
            switch (httpExc.code()) {
                case GATEWAY_TIMEOUT:
                    ex.setMsg(NetworkUtils.isConnected() ?
                            "网络请求超时，请稍后重试" : "网络不可用，请检查网络设置");
                    break;
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.setMsg("服务器异常，请稍后重试");
                    break;
            }
            return ex;
        } else if (e instanceof NoRouteToHostException || e instanceof UnknownHostException) {
            ex = new ApiException(e, ERROR.NETWORK_ERROR);
            ex.setMsg("网络不可用，请检查网络设置");
            return ex;
        } else if (e instanceof ServerException) {    //服务器返回的错误
            ServerException serverExc = (ServerException) e;
            ex = new ApiException(serverExc, serverExc.getCode());
            ex.setMsg(serverExc.getMsg());
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException
                || e instanceof MalformedJsonException) {  //解析数据错误
            ex = new ApiException(e, ERROR.PARSE_ERROR);
            ex.setMsg("解析错误");
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ApiException(e, ERROR.SSL_ERROR);
            ex.setMsg("证书验证失败");
            return ex;
        } else if (e instanceof ConnectException) {//连接网络错误
            ex = new ApiException(e, ERROR.NETWORK_ERROR);
            ex.setMsg("网络错误，请检查网络设置");
            return ex;
        } else if (e instanceof SocketTimeoutException) {//网络超时
            ex = new ApiException(e, ERROR.TIMEOUT_ERROR);
            ex.setMsg("网络超时");
            return ex;
        } else {  //未知错误
            ex = new ApiException(e, ERROR.UNKNOWN);
            ex.setMsg("网络不可用，请检查网络设置");
            return ex;
        }
    }

    /**
     * 约定异常
     */
    public class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORK_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;

        /**
         * 返回数据错误
         */
        public static final int DATA_ERROR = 1007;

    }

}
