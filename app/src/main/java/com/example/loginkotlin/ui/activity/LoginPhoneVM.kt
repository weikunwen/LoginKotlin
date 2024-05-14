package com.example.loginkotlin.ui.activity

import android.util.ArrayMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loginkotlin.network.observer.HttpRxObservableKt
import com.example.loginkotlin.network.observer.HttpRxObserverKt
import com.example.loginkotlin.repository.LoginPhoneRepository
import com.example.loginkotlin.base.BaseViewModel
import com.example.loginkotlin.entity.UserInfo
import com.example.loginkotlin.entity.VerifyCodeBean
import com.example.loginkotlin.network.exception.ApiException
import com.example.loginkotlin.util.ToastUtil
import timber.log.Timber

class LoginPhoneVM(loginState: LoginPhoneState) : BaseViewModel<LoginPhoneState, LoginPhoneAction>(loginState) {

    companion object {
        class ViewModelFactory(private val loginState: LoginPhoneState) : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(LoginPhoneVM::class.java)) {
                    return LoginPhoneVM(loginState) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    private val loginPhoneRepository: LoginPhoneRepository by lazy { LoginPhoneRepository() }

    override fun handleActions(viewAction: LoginPhoneAction) {
        Timber.i("handleActions: The viewAction is: " + viewAction::class.qualifiedName)
        when(viewAction) {
            is LoginPhoneAction.ManualLoginAction -> manualLogin(viewAction.phone, viewAction.pwd, viewAction.loginType)
            is LoginPhoneAction.GetVerifyCodeAction -> getVerifyCode(viewAction.phone, viewAction.codeType)
            is LoginPhoneAction.AuthLoginAction -> authLogin(viewAction.token)
            is LoginPhoneAction.AgreePrivateAction -> agreePrivate(viewAction.agreementType, viewAction.checkAgreement)
        }
    }

    private fun manualLogin(phone: String, pwd: String, loginType: String) {
        HttpRxObservableKt.getObservableOnMain(loginPhoneRepository.login(phone, pwd, loginType))  //回调到主线程，销毁dialog
            ?.subscribe(object : HttpRxObserverKt<UserInfo?>() {
                override fun _onError(e: ApiException?) {
                    e?.let { emitSharedFlow(LoginPhoneState.ManualLoginFailState(it.code, it.msg)) }
                    val eventData: MutableMap<String, String> = ArrayMap(2)
                    eventData["result"] = "failure"
                    eventData["loginType"] = loginType
                }

                override fun _onSuccess(data: UserInfo?) {
                    data?.apply {
                        emitSharedFlow(LoginPhoneState.LoginSuccessState)

                        val eventData: MutableMap<String, String> = ArrayMap(2)
                        eventData["result"] = "success"
                        eventData["loginType"] = loginType
                    } ?: apply {
                        ToastUtil.showShortToast("登录失败")
                    }
                }
            })
    }

    private fun getVerifyCode(phone: String, codeType: Int) {
        HttpRxObservableKt.getObservableOnMain(loginPhoneRepository.getVerifyCode(phone, codeType))
            ?.subscribe(object : HttpRxObserverKt<VerifyCodeBean?>() {
                override fun _onError(e: ApiException?) {
                    e?.let { ToastUtil.showShortToast(it.msg) }
                }

                override fun _onSuccess(data: VerifyCodeBean?) {
                    emitSharedFlow(LoginPhoneState.GetVerifyCodeSuccessState(codeType))
//                    DialogManager.dismissLoadingDialog()  //dialog销毁放在显示键盘操作后面
                }
            })
    }

    private fun authLogin(token: String) {
        HttpRxObservableKt.getObservableOnMain(loginPhoneRepository.authLogin(token))
            ?.subscribe(object : HttpRxObserverKt<UserInfo?>() {
                override fun _onError(e: ApiException?) {
                    e?.let { emitSharedFlow(LoginPhoneState.AuthLoginFailState(it.code, it.msg)) }
                    val eventData: MutableMap<String, String> = HashMap()
                    eventData["result"] = "failure"
                    eventData["loginType"] = "quickLogin"
                }

                override fun _onSuccess(data: UserInfo?) {
                    data?.apply {
                        emitSharedFlow(LoginPhoneState.LoginSuccessState)

                        val eventData: MutableMap<String, String> = ArrayMap(2)
                        eventData["result"] = "success"
                        eventData["loginType"] = "quickLogin"
                    } ?: apply { ToastUtil.showShortToast("登录失败") }
                }
            })
    }

    private fun agreePrivate(agreementType: String, checkAgreement: Int) {
        HttpRxObservableKt.getObservableOnMain(loginPhoneRepository.agreePrivate(agreementType, checkAgreement))
            ?.subscribe(object : HttpRxObserverKt<Any?>() {
                override fun _onError(e: ApiException?) {
                    Timber.i("agreePrivate: _onError: The e?.msg is: " + e?.msg)
                    emitSharedFlow(LoginPhoneState.AgreePrivateFail)
                }

                override fun _onSuccess(data: Any?) {
                    Timber.i("agreePrivate: _onSuccess: agree private")
                    emitSharedFlow(LoginPhoneState.AgreePrivateSuccess)
                }
            })
    }
}

sealed class LoginPhoneAction {
    /**
     * 手动输入登录
     */
    data class ManualLoginAction(val phone: String, val pwd: String, val loginType: String) : LoginPhoneAction()

    /**
     * 获取验证码
     */
    data class GetVerifyCodeAction(val phone: String, val codeType: Int) : LoginPhoneAction()

    /**
     * 自动登录
     */
    data class AuthLoginAction(val token: String) : LoginPhoneAction()

    /**
     * 同意隐私政策
     */
    data class AgreePrivateAction(val agreementType: String, val checkAgreement: Int) : LoginPhoneAction()
}

sealed class LoginPhoneState {
    data object InitialState : LoginPhoneState()

    data class ManualLoginFailState(val code: Int, val errorMsg: String) : LoginPhoneState()  //手动登录失败

    data class AuthLoginFailState(val code: Int, val errorMsg: String) : LoginPhoneState()   //自动登录失败

    data object LoginSuccessState : LoginPhoneState()  //登录成功

    data class GetVerifyCodeSuccessState(val codeType: Int) : LoginPhoneState()

    data object AgreePrivateSuccess : LoginPhoneState()

    data object AgreePrivateFail : LoginPhoneState()
}