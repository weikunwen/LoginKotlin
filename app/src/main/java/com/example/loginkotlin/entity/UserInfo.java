package com.example.loginkotlin.entity;

import java.io.Serializable;

public class UserInfo implements Serializable {

    private long userId;
    private String nickName;
    private String expiredTime;
    private String token;
    private int bindMobile;
    private String privateCode;
    private String regChannel;//注册渠道
    private int isBlock;//是否是黑名单用户
    private String regCity;//注册城市
    private int isInitPassword;//是否需要初始化密码

    private int isNewCreated;//是否新注册

    public void setIsNewCreated(int isNewCreated) {
        this.isNewCreated = isNewCreated;
    }

    public int getIsNewCreated() {
        return isNewCreated;
    }

    public void setIsInitPassword(int isInitPassword) {
        this.isInitPassword = isInitPassword;
    }

    public int getIsInitPassword() {
        return isInitPassword;
    }

    public void setBindMobile(int bindMobile) {
        this.bindMobile = bindMobile;
    }

    public int getBindMobile() {
        return bindMobile;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPrivateCode() {
        return privateCode;
    }

    public void setPrivateCode(String privateCode) {
        this.privateCode = privateCode;
    }

    public String getRegChannel() {
        return regChannel;
    }

    public void setRegChannel(String regChannel) {
        this.regChannel = regChannel;
    }

    public int getIsBlock() {
        return isBlock;
    }

    public void setIsBlock(int isBlock) {
        this.isBlock = isBlock;
    }

    public String getRegCity() {
        return regCity;
    }

    public void setRegCity(String regCity) {
        this.regCity = regCity;
    }
}

