package com.example.loginkotlin.repository

import android.text.TextUtils
import com.example.loginkotlin.global.ContextHolder.context
import com.example.loginkotlin.entity.UserInfo
import com.example.loginkotlin.entity.VerifyCodeBean
import com.example.loginkotlin.network.retrofit.Api
import com.example.loginkotlin.network.retrofit.BaseResponse
import com.example.loginkotlin.network.retrofit.HttpRequest
import com.example.loginkotlin.repository.BaseRepository

import io.reactivex.Observable

class LoginPhoneRepository : BaseRepository() {

    fun login(phone: String, pwd: String, loginType: String): Observable<BaseResponse<UserInfo?>> {
        val map: MutableMap<String, Any> = HashMap()
        map["loginId"] = phone
        map["password"] = pwd
        map["loginType"] = loginType
        return Api.getDefault().login(HttpRequest.getJsonRequestBody(map))
    }

    fun getVerifyCode(phone: String, codeType: Int): Observable<BaseResponse<VerifyCodeBean?>> {
        val map: MutableMap<String, Any> = java.util.HashMap()
        map["codeType"] = codeType.toString()
        map["mobile"] = phone
        return Api.getDefault().getVerifyCode(HttpRequest.getJsonRequestBody(map))
    }

    fun authLogin(token: String): Observable<BaseResponse<UserInfo?>> {
        val map: MutableMap<String, Any> = java.util.HashMap()
        map["loginToken"] = token
        return Api.getDefault().authLogin(HttpRequest.getJsonRequestBody(map))
    }

    fun agreePrivate(agreementType: String, checkAgreement: Int): Observable<BaseResponse<Any?>> {
        val map: MutableMap<String, Any> = java.util.HashMap()
        map["agreementType"] = agreementType
        map["checkAgreement"] = checkAgreement
        return Api.getDefault().onAgreePrivate(HttpRequest.getJsonRequestBody(map))
    }
}