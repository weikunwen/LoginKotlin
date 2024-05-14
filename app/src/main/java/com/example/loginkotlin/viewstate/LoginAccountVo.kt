package com.example.loginkotlin.viewstate

import com.example.loginkotlin.R


class LoginAccountVo(val loginAccountStr: String, val loginPwdStr: String) {

    val accountLeftImageRes: Int
        get() = if (loginAccountStr.isEmpty()) R.mipmap.login_account_unselected else R.mipmap.login_account_selected

    val accountLineColorRes: Int
        get() = if (loginAccountStr.isEmpty()) R.color.login_edit_unselected else R.color.color_white

    val passwordLeftImageRes: Int
        get() = if (loginPwdStr.isEmpty()) R.mipmap.login_pwd_unselected else R.mipmap.login_pwd_selected

    val passwordLineColorRes: Int
        get() = if (loginPwdStr.isEmpty()) R.color.login_edit_unselected else R.color.color_white

    val loginBtnEnable: Boolean
        get() = loginAccountStr.isNotEmpty() && loginPwdStr.isNotEmpty()
}