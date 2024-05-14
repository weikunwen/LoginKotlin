package com.example.loginkotlin

import android.app.Application
import com.blankj.utilcode.util.ProcessUtils
import com.example.loginkotlin.global.ContextHolder
import me.jessyan.autosize.AutoSize
import timber.log.Timber
import kotlin.system.measureTimeMillis

/**
 * author : dong.chao
 * time : 2022/11/3
 * desc : BaseApplication
 */
class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.d("-------MainApp onCreate")

        val timeCost = measureTimeMillis {
            //主进程
            val processName = ProcessUtils.getCurrentProcessName()
            Timber.d("------CurrentProcessName：${processName}")
                Timber.d("--------MainApp initAppForMainProcess")
                AutoSize.initCompatMultiProcess(this@MainApp)
        }

        ContextHolder.context = applicationContext
        Timber.d("---------MainApp onCreate 耗时: ${timeCost}ms")
    }
}