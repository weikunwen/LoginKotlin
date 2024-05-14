package com.example.loginkotlin.util;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.blankj.utilcode.util.KeyboardUtils;

public final class KeyBoardUtils {

    private static volatile KeyBoardUtils mInstance;

    public static KeyBoardUtils getInstance() {
        if (mInstance == null) {
            synchronized (KeyBoardUtils.class) {
                if (mInstance == null) {
                    mInstance = new KeyBoardUtils();
                }
            }
        }
        return mInstance;
    }

    public void showSoftInput(final Activity activity) {
        showSoftInput(activity, InputMethodManager.SHOW_IMPLICIT);
    }

    private void showSoftInput(final Activity activity, final int flags) {
        View view = activity.getCurrentFocus();
        showSoftInput(view, flags);
    }

    public void showSoftInput(final View view) {
        showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public boolean isSoftInputVisible(final Activity activity) {
        return KeyboardUtils.isSoftInputVisible(activity);
    }

    public void showSoftInput(final View view, final int flags) {
        KeyboardUtils.showSoftInput(view,flags);
    }

    public void hideSoftInput(final Activity activity) {
        KeyboardUtils.hideSoftInput(activity);
    }

    public void hideSoftInput(final View view) {
        KeyboardUtils.hideSoftInput(view);
    }

    /**
     * Fix the leaks of soft input.
     * @param context The context.
     */
    public void fixSoftInputLeaks(final Context context) {
        KeyboardUtils.fixSoftInputLeaks((Activity) context);
    }
}
