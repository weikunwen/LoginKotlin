package com.example.loginkotlin.enums

enum class LoginType(val value:String) {
    // 登录类型， mobile 手机号 + 密码、mcode手机号 + 验证码、origin 原始账户
    MOBILE  ("mobile"), MCODE( "mcode"), ORIGIN ("origin")
}