package com.example.loginkotlin.network.exception;

/**
 * 自定义服务器错误
 */
public class ServerException extends RuntimeException {
    private final  int code;
    private final String msg;

    public ServerException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
