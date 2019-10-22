package com.angadi.tripmanagementa.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public abstract class AbstractBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        initView();
//        bindViewActions();
    }

    /**
     * use this function to initialize all the ui components
     */
//    public abstract void initView();

    /**
     * use this function to bind the ui components with event handlers
     */
//    public abstract void bindViewActions();

    /**
     * hide keyboard
     */
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * hide keyboard
     *
     * @param view
     */

    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * network availability check
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    protected void showLoadingLayout(View loadingLayout) {
        loadingLayout.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    protected void hideLoadingLayout(View loadingLayout) {
        loadingLayout.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    /**
     * adds fragment to the specified layout_ID
     */

    public void addFragment(Fragment fragment, int layoutId, Bundle bundle, String tag) {
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.add(layoutId, fragment);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    public void addFragment(Fragment fragment, int layoutId, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.add(layoutId, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public void addFragment(Fragment fragment, int layoutId, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.add(layoutId, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    /**
     * adds fragment to the specified layout_ID without adding it to the back stack
     */

    public void addFragmentWithoutBackStack(Fragment fragment, int layout_ID, Bundle bundle, String tag) {
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.add(layout_ID, fragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * replaces ApiCall fragment to the specified layout_ID
     */
    public void replaceFragment(Fragment fragment, int layoutId) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(layoutId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * replaces ApiCall fragment to the specified layout_ID
     */
    public void replaceFragmentWithBackStack(Fragment fragment, int layoutId, Bundle bundle, String tag) {
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(layoutId, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    /**
     * replaces ApiCall fragment to the specified layout_ID
     */
    public void replaceFragmentWithBackStack(Fragment fragment, int layoutId) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(layoutId, fragment);
        transaction.commit();
    }


    public void replaceFragmentwithoutbackStack(Fragment fragment, int layoutId) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(layoutId, fragment);
        transaction.commit();
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /**
     * replaces ApiCall fragment to the specified layout_ID without adding it to the backstack
     */

    public void replaceFragment(Fragment fragment, int layoutId,
                                Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(layoutId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * To replace ApiCall fragment with tag using SupportFragmentManager.
     *
     * @param fragment - Fragment to replace.
     * @param layoutId - Layout to where fragment replacing.
     * @param tag      - Tag name for fragment and back stack entry.
     */
    public void replaceFragment(Fragment fragment, int layoutId,
                                String tag) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(layoutId, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    /**
     * clears the current fragment stack
     */
    public void clearStack() {
        FragmentManager fm = this.getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    /**
     * pops till the fragment specified in the tag
     */
    public void popBackStack(String tag) {
        FragmentManager fm = this.getSupportFragmentManager();
        fm.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * pops the current fragment
     */
    public void popBackStack() {
        FragmentManager fm = this.getSupportFragmentManager();
        fm.popBackStack();
    }

}
