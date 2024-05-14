package com.example.loginkotlin.network.retrofit;

import java.io.Serializable;


public class BaseResponse<T> implements Serializable {
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    protected int code;
    protected String message;
    protected T data;

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return code == 200;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "status=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
