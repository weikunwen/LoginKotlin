package com.example.loginkotlin.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.loginkotlin.viewstate.LoginAccountVo
import com.example.loginkotlin.Listener.SoftKeyBoardListener
import com.example.loginkotlin.R
import com.example.loginkotlin.databinding.ActivityLoginAccountBinding
import com.example.loginkotlin.enums.LoginType
import com.example.loginkotlin.util.KeyBoardUtils
import com.example.loginkotlin.util.StatusBarNewUtil
import com.example.loginkotlin.util.ToastUtil
import com.example.loginkotlin.util.ViewUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginAccountActivity : AppCompatActivity(), SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
    private lateinit var uiUpdatesJob: Job

    private lateinit var mBinding: ActivityLoginAccountBinding

    private lateinit var loginAccountVM: LoginAccountVM

    private var isDestroyed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginAccountBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        loginAccountVM = ViewModelProvider(this,
            LoginAccountVM.Companion.ViewModelFactory(LoginAccountState.InitialState)
        )[LoginAccountVM::class.java]

        StatusBarNewUtil.setTransparentForImageView(this)
        registerUIStateCallback()

        initView()
        initListener()
    }

    private fun initView() {
        mBinding.titleBar.titleMiddleText.text = ""
        mBinding.loginAccountVo = LoginAccountVo("", "")
    }

    private fun initListener() {
        mBinding.titleBar.titleLeftBtn.setOnClickListener { clickLeftBtn() }
        mBinding.loginAccountSubmit.setOnClickListener { loginAccount() }
        mBinding.loginAccountAgreement1.setOnClickListener { clickAgreement1() }
        mBinding.loginAccountAgreement2.setOnClickListener { clickAgreement2() }

        mBinding.loginAccountNum.addTextChangedListener(afterTextChanged = { updateLoginVo() })
        mBinding.loginAccountPwd.addTextChangedListener(afterTextChanged = { updateLoginVo() })
        SoftKeyBoardListener.setListener(this, this)
    }

    private fun updateLoginVo() {
        mBinding.loginAccountVo = LoginAccountVo(mBinding.loginAccountNum.text.toString().trim(), mBinding.loginAccountPwd.text.toString().trim())
    }

    private fun registerUIStateCallback() {
        uiUpdatesJob = lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                loginAccountVM.sharedFlow.collect { state ->
                    Timber.i( "registerUIStateCallback: The state.javaClass.name is: " + state.javaClass.name)
                    when (state) {
                        is LoginAccountState.LoginSuccessState -> showLoginSuccess()
                        is LoginAccountState.ManualLoginFailState -> showManualLoginFail(state.code, state.errorMsg)
                        is LoginAccountState.AgreePrivateSuccess -> showAgreePrivateSuccess()
                        is LoginAccountState.AgreePrivateFail -> showAgreePrivateFail()
                        else -> {}
                    }
                }
            }
        }
    }

    private fun showLoginSuccess() {
        val intent = Intent()
        intent.putExtra("isLoginAccountSuccess", true)
        this@LoginAccountActivity.setResult(RESULT_OK, intent)
//        loginAccountVM.dispatchAction(LoginAccountAction.AgreePrivateAction("PRIVACY", 1))
    }

    private fun showManualLoginFail(code: Int, errorMsg: String) {
    }

    private fun showAgreePrivateSuccess() {
    }

    private fun showAgreePrivateFail() {
    }


    private fun clickLeftBtn() {
    }

    private fun loginAccount() {
        if (ViewUtils.isFastDoubleClick()) return
        KeyBoardUtils.getInstance().hideSoftInput(this)

        val account: String = mBinding.loginAccountNum.text.toString().trim()
        if (TextUtils.isEmpty(account)) {
            ToastUtil.showShortToast(getString(R.string.text_inputPhone))
            return
        }

        val pwd: String = mBinding.loginAccountPwd.text.toString().trim()
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.showShortToast(getString(R.string.text_inputPwd))
            return
        }

        if (!mBinding.loginAccountWriteAccount.isChecked) {
            ToastUtil.showShortToast(getString(R.string.text_checkProtocol))
            return
        }
        //登录
//        loginAccountVM.dispatchAction(LoginAccountAction.ManualLoginAction(account, pwd, LoginType.ORIGIN.value))
    }

    private fun clickAgreement1() {
        if (ViewUtils.isFastDoubleClick()) return
//        WebUiHelper.getInstance().toWebEchargeUsage(this@LoginAccountActivity)
    }

    private fun clickAgreement2() {
        if (ViewUtils.isFastDoubleClick()) return
//        WebUiHelper.getInstance().toWebPrivacyAgreement(this@LoginAccountActivity)
    }

    override fun onDestroy() {
        super.onDestroy()
        isDestroyed = true
        KeyBoardUtils.getInstance().hideSoftInput(this)
        KeyBoardUtils.getInstance().fixSoftInputLeaks(this)
        uiUpdatesJob.cancel()
        mBinding.unbind()
    }

    override fun keyBoardShow(height: Int) {
        if (!isDestroyed) {
            mBinding.loginAccountLogo.visibility = View.GONE
        }
    }

    override fun keyBoardHide(height: Int) {
        if (!isDestroyed) {
            mBinding.loginAccountLogo.visibility = View.VISIBLE
        }
    }
}