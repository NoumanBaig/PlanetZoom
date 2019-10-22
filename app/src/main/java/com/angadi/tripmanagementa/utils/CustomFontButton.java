package com.angadi.tripmanagementa.utils;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by nouman.
 */

public class CustomFontButton extends Button {

    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public CustomFontButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context, attrs);
    }

    public CustomFontButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context, attrs);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {
        int textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);

        Typeface customFont = selectTypeface(context, textStyle);
        setTypeface(customFont);
    }

    private Typeface selectTypeface(Context context, int textStyle) {
        /*
        * information about the TextView textStyle:
        * http://developer.android.com/reference/android/R.styleable.html#TextView_textStyle
        */
        switch (textStyle) {
            case Typeface.NORMAL:
            return Typeface.createFromAsset(getContext().getAssets(),"fonts/Regular.ttf");

            case Typeface.BOLD:
                return Typeface.createFromAsset(getContext().getAssets(),"fonts/Bold.OTF");

            default:
                return Typeface.createFromAsset(getContext().getAssets(),"fonts/Regular.ttf");
        }
    }
}