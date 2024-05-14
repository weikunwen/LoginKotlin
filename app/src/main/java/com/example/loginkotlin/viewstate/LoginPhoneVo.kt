package com.example.loginkotlin.viewstate

import android.view.View
import com.example.loginkotlin.R
import com.example.loginkotlin.enums.LoginType
import com.example.loginkotlin.global.ContextHolder

class LoginPhoneVo(private val loginType: String, private val loginPhoneStr: String,
                   private val loginPwdStr: String, private val loginCodeStr: String, private val verifyCodeTag: String) {
    val loginAccountStr = loginPhoneStr

    val accountLeftImageRes: Int
        get() = if (loginPhoneStr.isEmpty()) R.mipmap.login_phone_unselected else R.mipmap.login_phone_selected

    val accountLineColorRes: Int
        get() = if (loginPhoneStr.isEmpty()) R.color.login_edit_unselected else R.color.color_white

    val isShowPasswordLayout: Int   //手机号 + 密码登录
        get() = if (loginType == LoginType.MOBILE.value) View.VISIBLE else View.GONE

    val passwordLeftImageRes: Int
        get() = if (loginPwdStr.isEmpty()) R.mipmap.login_pwd_unselected else R.mipmap.login_pwd_selected

    val passwordLineColorRes: Int
        get() = if (loginPwdStr.isEmpty()) R.color.login_edit_unselected else R.color.color_white

    val isShowCodeLayout: Int   //验证码登录
        get() = if (loginType == LoginType.MCODE.value) View.VISIBLE else View.GONE

    val codeLeftImageRes: Int
        get() = if (loginCodeStr.isEmpty()) R.mipmap.login_reg_verify_code_unselected else R.mipmap.login_reg_verify_code_selected

    val codeLineColorRes: Int
        get() = if (loginCodeStr.isEmpty()) R.color.login_edit_unselected else R.color.color_white

    val isShowLoginCall: Int
        get() = if (loginType == LoginType.MCODE.value) View.VISIBLE else View.INVISIBLE

    val getVerifyCodeEnable: Boolean
        get() {
            var verifyCodeEnable = false
            if (loginPhoneStr.length == 11) {
                if (verifyCodeTag.contains(ContextHolder.context.getString(R.string.text_verifyCode))
                    || verifyCodeTag.contains(ContextHolder.context.getString(R.string.text_again_get_verifyCode))) {
                    verifyCodeEnable = true
                }
            }
            return verifyCodeEnable
        }

    val loginBtnEnable: Boolean
        get() {
            return when (loginType) {
                LoginType.MCODE.value -> loginCodeStr.isNotEmpty()
                LoginType.MOBILE.value -> loginPwdStr.isNotEmpty()
                else -> false
            } && loginPhoneStr.isNotEmpty()
        }

    val switchLoginTypeStr: String
        get() = if (loginType == LoginType.MCODE.value) ContextHolder.context.getString(R.string.login_phone) else ContextHolder.context.getString(R.string.login_mcode)
}