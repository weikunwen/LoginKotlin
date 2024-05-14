package com.example.loginkotlin.util;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewUtils {
    public static final int DRAW_LEFT = 1;
    public static final int DRAW_RIGHT = 2;
    public static final int DRAW_TOP = 3;
    public static final int DRAW_BOTTOM = 4;
    public static final int DRAW_VIEW_TEXTVIEW = 1;

    private ViewUtils(){

    }

    private static long lastClickTime;

    /**
     * 已在 BaseActivity.startActivityForResult 方法中处理重复跳转同一个 Activity 的问题
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static SpannableStringBuilder getStringStarRed(String string) {

        SpannableStringBuilder builder = new SpannableStringBuilder(string);

        if (TextUtils.isEmpty(string) || !string.contains("*")) {
            return builder;
        }
        int index = string.indexOf("*");

        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        builder.setSpan(redSpan, index, index + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }


    /**
     *  * 关键字高亮显示
     *  *
     *  * @param context 上下文
     *  * @param text  需要显示的文字
     *  * @param target 需要高亮的关键字
     *  * @param color  高亮颜色
     *  * @param start  头部增加高亮文字个数
     *  * @param end   尾部增加高亮文字个数
     *  * @return 处理完后的结果
     *  
     */
    public static SpannableString highlight(String text, String target,
                                            String color, int start, int end) {
        SpannableString spannableString = new SpannableString(text);
        if (TextUtils.isEmpty(target) || !text.contains(target)) {
            return spannableString;
        }
        Pattern pattern = Pattern.compile(target);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor(color));
            spannableString.setSpan(span, matcher.start() - start, matcher.end() + end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    public static int getIdentifier(Context mContext, String iconName) {
        try {
            if (isInt(iconName)) {
                return 0;
            }
            return mContext.getResources().getIdentifier(iconName.toLowerCase(Locale.getDefault()), "mipmap", mContext.getPackageName());
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean isInt(String iconName) {
        try {
            return Integer.parseInt(iconName) >= 0 ;
        } catch (Exception e) {
            return false;
        }
    }

    public static Spanned fromHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }
}
