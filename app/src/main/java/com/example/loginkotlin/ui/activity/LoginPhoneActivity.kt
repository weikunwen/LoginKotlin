package com.example.loginkotlin.ui.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.loginkotlin.viewstate.LoginPhoneVo
import com.example.loginkotlin.Listener.SoftKeyBoardListener
import com.example.loginkotlin.R
import com.example.loginkotlin.databinding.ActivityLoginPhoneBinding
import com.example.loginkotlin.enums.LoginType
import com.example.loginkotlin.helper.CountDownHelper
import com.example.loginkotlin.util.KeyBoardUtils
import com.example.loginkotlin.util.StatusBarNewUtil
import com.example.loginkotlin.util.ToastUtil
import com.example.loginkotlin.util.ViewUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginPhoneActivity : AppCompatActivity(), CountDownHelper.ICountDownCallBack, SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
    private lateinit var uiUpdatesJob: Job

    private lateinit var mBinding: ActivityLoginPhoneBinding

    private lateinit var loginPhoneVM: LoginPhoneVM

    private lateinit var launchLoginAccount: ActivityResultLauncher<Intent>

    private lateinit var launchFindPwd: ActivityResultLauncher<Intent>

    private var cancelAuthDialog: Dialog? = null

    private var isDestroyed = false

    private var loginPhoneType = LoginType.MOBILE.value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginPhoneBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        loginPhoneVM = ViewModelProvider(this, LoginPhoneVM.Companion.ViewModelFactory(LoginPhoneState.InitialState))[LoginPhoneVM::class.java]

        StatusBarNewUtil.setTransparentForImageView(this)
        registerUIStateCallback()

        initView()
        initData()
        initListener()

        launchLoginAccount = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val isLoginAccountSuccess = it.data?.getBooleanExtra("isLoginAccountSuccess", false) ?: false
                if (isLoginAccountSuccess) {
                    this@LoginPhoneActivity.finish()
                }
            }
        }

        launchFindPwd = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val isCountDowning = CountDownHelper.getInstance().checkGlobalCountDowning(this@LoginPhoneActivity)
            if (isCountDowning) {
                setVerifyCodeBtnEnabled(false)
                setVerifyCodeText("")
            } else {
                setVerifyCodeBtnEnabled(true)
                setVerifyCodeText(getString(R.string.text_verifyCode))
            }
        }
    }

    private fun initView() {
        val isCountDowning = CountDownHelper.getInstance().checkGlobalCountDowning(this)
        if (isCountDowning) {
            setVerifyCodeBtnEnabled(false)
            setVerifyCodeText("")
        }

        mBinding.titleBar.titleMiddleText.text = ""
        mBinding.titleBar.titleLeftBtn.setImageResource(R.mipmap.arrow_bottom_white)
        mBinding.loginCall.text = ViewUtils.fromHtml(getString(R.string.login_find_not_verify_code))
    }

    private fun initData() {
        mBinding.loginPhoneVo = LoginPhoneVo(loginPhoneType, "", "", "", mBinding.loginGetVerifyCode.text.toString())
    }

    private fun initListener() {
        mBinding.titleBar.titleLeftBtn.setOnClickListener { clickLeftBtn() }
        mBinding.loginAccountLogin.setOnClickListener { clickRegister() }
        mBinding.loginAccount.setOnClickListener { clickLoginAccount() }
        mBinding.loginGetVerifyCode.setOnClickListener { clickGetVerifyCode() }
        mBinding.loginCall.setOnClickListener { clickCall() }
        mBinding.loginForgetPwd.setOnClickListener { findPwd() }
        mBinding.loginSubmit.setOnClickListener { loginAccount() }
        mBinding.loginAgreement1.setOnClickListener { clickAgreement1() }
        mBinding.loginAgreement2.setOnClickListener { clickAgreement2() }

        mBinding.loginNum.addTextChangedListener(afterTextChanged = { updateLoginVo() })
        mBinding.loginPwd.addTextChangedListener(afterTextChanged = { updateLoginVo() })
        mBinding.loginCode.addTextChangedListener(afterTextChanged = { updateLoginVo() })
        SoftKeyBoardListener.setListener(this, this)
    }

    override fun onStart() {
        super.onStart()
    }

    private fun toggleLoginType(currentLoginType: String) {
        if (LoginType.MCODE.value == currentLoginType) {
            //修改当前状态为手机号+密码登录
            loginPhoneType = LoginType.MOBILE.value
        } else {
            //修改当前状态为手机号+验证码登录
            loginPhoneType = LoginType.MCODE.value
        }
        updateLoginVo()

        KeyBoardUtils.getInstance().hideSoftInput(this)
    }

    private fun updateLoginVo() {
        mBinding.loginPhoneVo = LoginPhoneVo(loginPhoneType, mBinding.loginNum.text.toString().trim(), mBinding.loginPwd.text.toString().trim(),
            mBinding.loginCode.text.toString().trim(), mBinding.loginGetVerifyCode.text.toString())
    }

    private fun registerUIStateCallback() {
        uiUpdatesJob = lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                loginPhoneVM.sharedFlow.collect { state ->
                    Timber.i( "registerUIStateCallback: The state.javaClass.name is: " + state.javaClass.name)
                    when (state) {
                        is LoginPhoneState.LoginSuccessState -> {
                            CountDownHelper.getInstance().disposeGlobal()
//                            loginPhoneVM.dispatchAction(LoginPhoneAction.AgreePrivateAction("PRIVACY", 1))
                        }
                        is LoginPhoneState.ManualLoginFailState -> showManualLoginFail(state.code, state.errorMsg)
                        is LoginPhoneState.AuthLoginFailState -> showAuthLoginFail(state.code, state.errorMsg)
                        is LoginPhoneState.GetVerifyCodeSuccessState -> showGetVerifyCodeSuccess(state.codeType)
                        is LoginPhoneState.AgreePrivateSuccess -> showAgreePrivateSuccess()
                        is LoginPhoneState.AgreePrivateFail -> showAgreePrivateFail()
                        else -> {}
                    }
                }
            }
        }
    }

    private fun showManualLoginFail(code: Int, errorMsg: String) {
    }

    private fun showAuthLoginFail(code: Int, errorMsg: String) {
    }

    private fun showGetVerifyCodeSuccess(codeType: Int) {
    }

    private fun showAgreePrivateSuccess() {
    }

    private fun showAgreePrivateFail() {
        this@LoginPhoneActivity.finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, R.anim.activity_bottom_out)
    }

    private fun clickLeftBtn() {
        if (ViewUtils.isFastDoubleClick()) return

        this@LoginPhoneActivity.finish()
        overridePendingTransition(0, R.anim.activity_bottom_out)
    }

    private fun clickRegister() {
        if (ViewUtils.isFastDoubleClick()) return

        val it = Intent(this, LoginAccountActivity::class.java)
        launchLoginAccount.launch(it)
    }

    private fun clickLoginAccount() {
        if (ViewUtils.isFastDoubleClick()) return
        toggleLoginType(loginPhoneType)
    }

    private fun clickGetVerifyCode() {
        if (ViewUtils.isFastDoubleClick()) return
//        loginPhoneVM.dispatchAction(LoginPhoneAction.GetVerifyCodeAction(mBinding.loginNum.text.toString().trim(), 1))
    }

    private fun clickCall() {
        if (ViewUtils.isFastDoubleClick()) return
        if (mBinding.loginNum.text.length != 11) {
            ToastUtil.showShortToast(getString(R.string.text_hint_phone))
            return
        }
//        loginPhoneVM.dispatchAction(LoginPhoneAction.GetVerifyCodeAction(mBinding.loginNum.text.toString().trim(), 2))
    }

    private fun findPwd() {
        if (ViewUtils.isFastDoubleClick()) {
            return
        }
    }

    private fun loginAccount() {
        if (ViewUtils.isFastDoubleClick()) return
        KeyBoardUtils.getInstance().hideSoftInput(this)

        val phone: String = mBinding.loginNum.text.toString().trim()
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showShortToast(getString(R.string.text_inputPhone))
            return
        }

        val pwd: String = mBinding.loginPwd.text.toString().trim()
        if (LoginType.MOBILE.value == loginPhoneType && TextUtils.isEmpty(pwd)) {
            ToastUtil.showShortToast(getString(R.string.text_inputPwd))
            return
        }
        val code: String = mBinding.loginCode.text.toString().trim()
        if (LoginType.MCODE.value == loginPhoneType && TextUtils.isEmpty(code)) {
            ToastUtil.showShortToast(getString(R.string.text_inputVerifyCode))
            return
        }

        if (!mBinding.loginWriteAccount.isChecked) {
            ToastUtil.showShortToast(getString(R.string.text_checkProtocol))
            return
        }
        //登录
        loginPhoneVM.dispatchAction(LoginPhoneAction.ManualLoginAction(phone, if (LoginType.MCODE.value == loginPhoneType) code else pwd, loginPhoneType))
    }

    private fun clickAgreement1() {
        if (ViewUtils.isFastDoubleClick()) return
//        WebUiHelper.getInstance().toWebEchargeUsage(this@LoginPhoneActivity)
    }

    private fun clickAgreement2() {
        if (ViewUtils.isFastDoubleClick()) return
//        WebUiHelper.getInstance().toWebPrivacyAgreement(this@LoginPhoneActivity)
    }

    override fun setVerifyCodeBtnEnabled(enable: Boolean) {
        mBinding.loginGetVerifyCode.isEnabled = enable
    }

    override fun setVerifyCodeText(text: String) {
        mBinding.loginGetVerifyCode.text = text
    }

    override fun keyBoardShow(height: Int) {
        if (!isDestroyed) {
            mBinding.loginLogo.visibility = View.GONE
            mBinding.loginAgreementLayout.visibility = View.GONE
        }
    }

    override fun keyBoardHide(height: Int) {
        if (!isDestroyed) {
            mBinding.loginLogo.visibility = View.VISIBLE
            mBinding.loginAgreementLayout.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isDestroyed = true
        CountDownHelper.getInstance().removeGlobalCountDownCallBack()
        KeyBoardUtils.getInstance().hideSoftInput(this)
        KeyBoardUtils.getInstance().fixSoftInputLeaks(this)
        cancelAuthDialog?.takeIf { it.isShowing }?.apply { this.dismiss() }
        uiUpdatesJob.cancel()
        mBinding.unbind()
    }
}