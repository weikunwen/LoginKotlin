package com.example.loginkotlin.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel<STATE,ACTION>(state: STATE) : ViewModel() {
    private val _state = MutableStateFlow(state)
    val state: StateFlow<STATE> = _state.asStateFlow()

    /**
     * 定义SharedFlow，配置相关参数，解决StateFlow丢失和粘性值的问题（https://blog.csdn.net/lbs458499563/article/details/126449605）
     *
     * replay 相当于粘性数据,为0 代表不重放，也就是没有粘性
     * extraBufferCapacity 接受的慢时候,发送的缓存大小
     * onBufferOverflow 缓冲区溢出规则：
     * SUSPEND: 挂起
     * DROP_OLDEST: 移除旧的值
     * DROP_LATEST: 移除新的值
     */
    private val _sharedFlow = MutableSharedFlow<STATE>(replay = 0, extraBufferCapacity = 20, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val sharedFlow: SharedFlow<STATE> = _sharedFlow

    private val userIntent = MutableSharedFlow<ACTION>()

    init {
        viewModelScope.launch {
            userIntent.collect { viewAction ->
                handleActions(viewAction)
            }
        }
    }
    fun emitState(state: STATE) {
        viewModelScope.launch {
            Timber.d("emitState launch is invoked.$state")
            _state.emit(state)
        }
    }

    fun emitSharedFlow(state: STATE) {
        viewModelScope.launch {
            Timber.d("emitSharedFlow launch is invoked.$state")
            _sharedFlow.emit(state)
        }
    }

    /**
     * 根据具体action进行对应处理
     */
    abstract fun handleActions(viewAction: ACTION)

    fun dispatchAction(viewAction: ACTION) {
        viewModelScope.launch {
            userIntent.emit(viewAction)
        }
    }
}