package com.example.loginkotlin.ui.activity

import android.util.ArrayMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loginkotlin.network.observer.HttpRxObservableKt
import com.example.loginkotlin.network.observer.HttpRxObserverKt
import com.example.loginkotlin.repository.LoginAccountRepository
import com.example.loginkotlin.base.BaseViewModel
import com.example.loginkotlin.entity.UserInfo
import com.example.loginkotlin.network.exception.ApiException
import com.example.loginkotlin.util.ToastUtil
import timber.log.Timber

class LoginAccountVM(loginAccountState: LoginAccountState)
    : BaseViewModel<LoginAccountState, LoginAccountAction>(loginAccountState) {
    companion object {
        class ViewModelFactory(private val loginState: LoginAccountState) : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(LoginAccountVM::class.java)) {
                    return LoginAccountVM(loginState) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    private val loginAccountRepository: LoginAccountRepository by lazy {
        LoginAccountRepository()
    }

    override fun handleActions(viewAction: LoginAccountAction) {
        Timber.i("handleActions: The viewAction is: " + viewAction::class.qualifiedName)
        when(viewAction) {
            is LoginAccountAction.ManualLoginAction -> manualLogin(viewAction.phone, viewAction.pwd, viewAction.loginType)
            is LoginAccountAction.AgreePrivateAction -> agreePrivate(viewAction.agreementType, viewAction.checkAgreement)
        }
    }

    private fun manualLogin(phone: String, pwd: String, loginType: String) {
        HttpRxObservableKt.getObservableOnMain(loginAccountRepository.login(phone, pwd, loginType))  //回调到主线程，销毁dialog
            ?.subscribe(object : HttpRxObserverKt<UserInfo?>() {
                override fun _onError(e: ApiException?) {
                    e?.let {
                        emitSharedFlow(LoginAccountState.ManualLoginFailState(it.code, it.msg))
                    }
                    val eventData: MutableMap<String, String> = ArrayMap(2)
                    eventData["result"] = "failure"
                    eventData["loginType"] = loginType
                }

                override fun _onSuccess(data: UserInfo?) {
                    data?.apply {
                        emitSharedFlow(LoginAccountState.LoginSuccessState)

                        val eventData: MutableMap<String, String> = ArrayMap(2)
                        eventData["result"] = "success"
                        eventData["loginType"] = loginType
                    } ?: apply {
                        ToastUtil.showShortToast("登录失败")
                    }
                }
            })
    }

    private fun agreePrivate(agreementType: String, checkAgreement: Int) {
        HttpRxObservableKt.getObservableOnMain(loginAccountRepository.agreePrivate(agreementType, checkAgreement))
            ?.subscribe(object : HttpRxObserverKt<Any?>() {
                override fun _onError(e: ApiException?) {
                    Timber.i("agreePrivate: _onError: The e?.msg is: " + e?.msg)
                    emitSharedFlow(LoginAccountState.AgreePrivateFail)
                }

                override fun _onSuccess(data: Any?) {
                    Timber.i("agreePrivate: _onSuccess: agree private")
                    emitSharedFlow(LoginAccountState.AgreePrivateSuccess)
                }
            })
    }
}

sealed class LoginAccountAction {
    /**
     * 手动输入登录
     */
    data class ManualLoginAction(val phone: String, val pwd: String, val loginType: String) : LoginAccountAction()

    /**
     * 同意隐私政策
     */
    data class AgreePrivateAction(val agreementType: String, val checkAgreement: Int) : LoginAccountAction()
}

sealed class LoginAccountState {
    object InitialState : LoginAccountState()

    data class ManualLoginFailState(val code: Int, val errorMsg: String) : LoginAccountState()  //手动登录失败

    object LoginSuccessState : LoginAccountState()  //登录成功

    object AgreePrivateSuccess : LoginAccountState()

    object AgreePrivateFail : LoginAccountState()
}