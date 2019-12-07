package com.angadi.tripmanagementa.adapters;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.angadi.tripmanagementa.R;
import com.angadi.tripmanagementa.activities.ui.main.SBMFragment;
import com.angadi.tripmanagementa.activities.ui.main.WSMFragment;
import com.angadi.tripmanagementa.fragments.ReceivedFragment;
import com.angadi.tripmanagementa.fragments.ScannedFragment;
import com.angadi.tripmanagementa.fragments.SharedFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ECardPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_ecard_1, R.string.tab_ecard_2, R.string.tab_ecard_3};
    private final Context mContext;
    private Fragment[] childFragments;
    public ECardPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        childFragments = new Fragment[] {
                new ScannedFragment(),
                new ReceivedFragment(),
                new SharedFragment()
        };
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a SBMFragment (defined as a static inner class below).
        Log.e("position-->",""+position);
        return childFragments[position];
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return childFragments.length;
    }
}