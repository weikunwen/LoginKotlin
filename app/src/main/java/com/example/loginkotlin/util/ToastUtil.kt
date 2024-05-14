package com.example.loginkotlin.util

import com.example.loginkotlin.R
import com.example.loginkotlin.global.ContextHolder.context
import com.blankj.utilcode.util.ToastUtils

object ToastUtil {
    fun showShortToast(msg: String?) {
        if (msg.isNullOrEmpty()) return
        ToastUtils.getDefaultMaker().setBgColor(context.getColor(R.color.toast_bg))
        ToastUtils.getDefaultMaker().setTextColor(context.getColor(R.color.white))
        ToastUtils.showShort(msg)
    }
}