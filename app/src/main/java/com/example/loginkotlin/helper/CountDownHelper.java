package com.example.loginkotlin.helper;

import java.util.concurrent.TimeUnit;

import com.example.loginkotlin.R;
import com.example.loginkotlin.global.ContextHolder;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CountDownHelper {

    private Disposable mDisposableGlobal;
    private Disposable mDisposableModifyMobile;

    private ICountDownCallBack globalCountDownCallBack;
    private ICountDownCallBack modifyMobileCountDownCallBack;

    private static volatile CountDownHelper mInstance;

    private CountDownHelper() {
    }

    public static CountDownHelper getInstance() {
        if (mInstance == null) {
            synchronized (CountDownHelper.class) {
                if (mInstance == null) {
                    mInstance = new CountDownHelper();
                }
            }
        }
        return mInstance;
    }

    public boolean checkGlobalCountDowning(ICountDownCallBack globalCountDownCallBack) {
        this.globalCountDownCallBack = globalCountDownCallBack;
        if (mDisposableGlobal != null) {
            return true;
        }
        return false;
    }

    public void removeGlobalCountDownCallBack() {
        this.globalCountDownCallBack = null;
    }

    public boolean checkModifyMobileCountDowning(ICountDownCallBack modifyMobileCountDownCallBack) {
        this.modifyMobileCountDownCallBack = modifyMobileCountDownCallBack;
        if (mDisposableModifyMobile != null) {
            return true;
        }
        return false;
    }

    public void removeModifyMobileCountDownCallBack() {
        this.modifyMobileCountDownCallBack = null;
    }


    public void startGlobalCountDown(ICountDownCallBack globalCountDownCallBack) {
        this.globalCountDownCallBack = globalCountDownCallBack;
        if (mDisposableGlobal != null) {
            return;
        }

        // 倒计时 60s
        mDisposableGlobal = Flowable.intervalRange(0, 60, 0, 1, TimeUnit.SECONDS)
                .onBackpressureBuffer().onBackpressureDrop()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> {
                    if (CountDownHelper.this.globalCountDownCallBack != null) {
                        CountDownHelper.this.globalCountDownCallBack.setVerifyCodeBtnEnabled(false);
                        CountDownHelper.this.globalCountDownCallBack.setVerifyCodeText(String.valueOf(60 - aLong) + "S");
                    }
                })
                .doOnComplete(() -> {
                    if (CountDownHelper.this.globalCountDownCallBack != null) {
                        CountDownHelper.this.globalCountDownCallBack.setVerifyCodeBtnEnabled(true);
                        CountDownHelper.this.globalCountDownCallBack.setVerifyCodeText(ContextHolder.INSTANCE.getContext().getString(R.string.text_again_get_verifyCode));
                    }
                    if (mDisposableGlobal != null && !mDisposableGlobal.isDisposed()) {
                        //停止倒计时
                        mDisposableGlobal.dispose();
                    }
                    CountDownHelper.this.globalCountDownCallBack = null;
                    mDisposableGlobal = null;
                })
                .subscribe();
    }

    public void startModifyMobileCountDown(ICountDownCallBack modifyMobileCountDownCallBack) {
        this.modifyMobileCountDownCallBack = modifyMobileCountDownCallBack;
        if (mDisposableModifyMobile != null) {
            return;
        }

        // 倒计时 10s
        mDisposableModifyMobile = Flowable.intervalRange(0, 60, 0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> {
                    if (CountDownHelper.this.modifyMobileCountDownCallBack != null) {
                        CountDownHelper.this.modifyMobileCountDownCallBack.setVerifyCodeBtnEnabled(false);
                        CountDownHelper.this.modifyMobileCountDownCallBack.setVerifyCodeText(String.valueOf(60 - aLong) + "秒");
                    }
                })
                .doOnComplete(() -> {
                    if (CountDownHelper.this.modifyMobileCountDownCallBack != null) {
                        CountDownHelper.this.modifyMobileCountDownCallBack.setVerifyCodeBtnEnabled(true);
                        CountDownHelper.this.modifyMobileCountDownCallBack.setVerifyCodeText(ContextHolder.INSTANCE.getContext().getString(R.string.text_again_get_verifyCode));
                    }
                    if (mDisposableModifyMobile != null && !mDisposableModifyMobile.isDisposed()) {
                        //停止倒计时
                        mDisposableModifyMobile.dispose();
                    }
                    CountDownHelper.this.modifyMobileCountDownCallBack = null;
                    mDisposableModifyMobile = null;
                })
                .subscribe();
    }


    public void disposeModifyMobile() {
        if (mDisposableModifyMobile != null && !mDisposableModifyMobile.isDisposed()) {
            //停止倒计时
            mDisposableModifyMobile.dispose();
        }
        CountDownHelper.this.modifyMobileCountDownCallBack = null;
        mDisposableModifyMobile = null;
    }

    public void disposeGlobal() {
        if (mDisposableGlobal != null && !mDisposableGlobal.isDisposed()) {
            //停止倒计时
            mDisposableGlobal.dispose();
        }
        CountDownHelper.this.globalCountDownCallBack = null;
        mDisposableGlobal = null;
    }


    public interface ICountDownCallBack {
        void setVerifyCodeBtnEnabled(boolean enable);

        void setVerifyCodeText(String text);
    }
}
