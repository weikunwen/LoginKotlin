package com.example.loginkotlin.repository

import com.example.loginkotlin.entity.UserInfo
import com.example.loginkotlin.network.retrofit.Api
import com.example.loginkotlin.network.retrofit.BaseResponse
import com.example.loginkotlin.network.retrofit.HttpRequest

import io.reactivex.Observable

class LoginAccountRepository : BaseRepository() {

    fun login(phone: String, pwd: String, loginType: String): Observable<BaseResponse<UserInfo?>> {
        val map: MutableMap<String, Any> = HashMap()
        map["loginId"] = phone
        map["password"] = pwd
        map["loginType"] = loginType
        return Api.getDefault().login(HttpRequest.getJsonRequestBody(map))
    }

    fun agreePrivate(agreementType: String, checkAgreement: Int): Observable<BaseResponse<Any?>> {
        val map: MutableMap<String, Any> = java.util.HashMap()
        map["agreementType"] = agreementType
        map["checkAgreement"] = checkAgreement
        return Api.getDefault().onAgreePrivate(HttpRequest.getJsonRequestBody(map))
    }
}