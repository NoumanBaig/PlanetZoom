package com.angadi.tripmanagementa.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.snackbar.Snackbar;

public class ShowSnackBar {
    public static void show(Context context, String message, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
