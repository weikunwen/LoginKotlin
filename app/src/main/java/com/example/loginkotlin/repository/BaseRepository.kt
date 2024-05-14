package com.example.loginkotlin.repository

import com.example.loginkotlin.network.exception.ApiException
import com.example.loginkotlin.network.retrofit.BaseResponse

/**
 *Created on 2022/12/7 9:26 上午
 *@author: jun.zhang
 *@description:
 **/
open class BaseRepository {

    fun <T> parseResult(result: BaseResponse<T>):T?{
        if(result.isSuccess){
            return result.data
        }else{
            throw ApiException(result.code,result.message)
        }
    }
}