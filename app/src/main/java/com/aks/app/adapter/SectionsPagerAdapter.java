package com.aks.app.adapter;

/**
 * Created by Aakash Singh on 21-12-2016.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.aks.app.fragment.PlaceholderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private FragmentManager fragmentManager;
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentManager = fm;
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
//        // getItem is called to instantiate the fragment for the given page.
//        // Return a PlaceholderFragment (defined as a static inner class below).
//        return PlaceholderFragment.newInstance(position, fragmentManager);
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
//        // Show 2 total pages.
//        return 2;
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        switch (position) {
//            case 0:
//                return "ALL CONTACTS";
//            case 1:
//                return "CONTACTS MAP";
//        }
//        return null;

        return fragmentTitleList.get(position);
    }
}
