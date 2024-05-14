package com.example.loginkotlin.util

import android.app.Activity
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.loginkotlin.R
import com.blankj.utilcode.util.BarUtils

/**
 * 状态栏工具类（适用：minSdkVersion >= 23）
 */
object StatusBarNewUtil {
    /**
     * 设置状态栏黑色字体
     */
    fun setStatusBarDark(activity: Activity) {
        setStatusBarDarkTheme(activity, true)
        setStatusBar(activity)
    }

    /**
     * 设置状态栏深色浅色切换
     * 一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
     * 所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
     * if (!setStatusBarDarkTheme(this, true)) {
     * //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
     * //这样半透明+白=灰, 状态栏的文字能看得清
     * setStatusBarColor(this, getResources().getColor(R.color.status_color));
     * }
     */
    fun setStatusBarDarkTheme(activity: Activity, dark: Boolean) {
        setStatusBarFontIconDark(activity, dark)
    }

    /**
     * 设置 状态栏深色浅色切换(dark:true字体黑色,false白色)
     */
    private fun setStatusBarFontIconDark(activity: Activity, dark: Boolean) {
        WindowCompat.getInsetsController(activity.window, activity.findViewById<FrameLayout>(android.R.id.content)).isAppearanceLightStatusBars = dark
    }

    private fun setStatusBar(activity: Activity) {
        setColor(activity, ContextCompat.getColor(activity, R.color.status_color), 0)  //不加半透明效果
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity       需要设置的activity
     * @param color          状态栏颜色值
     * @param statusBarAlpha 状态栏透明度
     */
    fun setColor(activity: Activity, @ColorInt color: Int, @IntRange(from = 0, to = 255) statusBarAlpha: Int) {
        activity.window.statusBarColor = calculateStatusColor(color, statusBarAlpha)
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private fun calculateStatusColor(@ColorInt color: Int, alpha: Int): Int {
        if (alpha == 0) return color

        val a = 1 - alpha / 255f
        var red = color shr 16 and 0xff
        var green = color shr 8 and 0xff
        var blue = color and 0xff
        red = (red * a + 0.5).toInt()
        green = (green * a + 0.5).toInt()
        blue = (blue * a + 0.5).toInt()
        return 0xff shl 24 or (red shl 16) or (green shl 8) or blue
    }

    /**
     * 为头部是 ImageView 的界面设置状态栏全透明
     *
     * @param activity  需要设置的activity
     */
    fun setTransparentForImageView(activity: Activity) {
        BarUtils.transparentStatusBar(activity)
    }
}