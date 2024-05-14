package com.example.loginkotlin.util.bindingAdapter;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class ImageViewAttrAdapter {

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, int resId) {
        view.setImageResource(resId);
    }
}