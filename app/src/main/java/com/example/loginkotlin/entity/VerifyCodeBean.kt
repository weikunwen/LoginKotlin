package com.example.loginkotlin.entity

import java.io.Serializable

/**
 * Created by chao.dong on 2018/8/29.
 * 验证码
 */
class VerifyCodeBean : Serializable {
    /**
     * mobile : 15021585823
     * code : 183586
     * codeType : 1
     * action : 注册
     */
    var mobile: String = ""
    var code: String = ""
    var codeType = 0
    var action: String = ""
}