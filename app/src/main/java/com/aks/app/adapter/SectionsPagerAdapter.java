package com.aks.app.adapter;

/**
 * Created by Aakash Singh on 21-12-2016.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.aks.app.fragment.PlaceholderFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private FragmentManager fragmentManager;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position, fragmentManager);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "ALL CONTACTS";
            case 1:
                return "CONTACTS MAP";
        }
        return null;
    }
}
