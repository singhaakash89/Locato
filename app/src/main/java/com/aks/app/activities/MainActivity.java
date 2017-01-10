package com.aks.app.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aks.app.Logger.Logger;
import com.aks.app.R;
import com.aks.app.adapter.RecyclerAdapter;
import com.aks.app.adapter.SectionsPagerAdapter;
import com.aks.app.database.sharedPreference.SharedPreferenceManager;
import com.aks.app.fragment.PlaceholderFragment;
import com.aks.app.json_parser.AsyncTaskHandler;
import com.aks.app.supervisor.SupervisorApplication;
import com.aks.app.toast_manager.ToastManager;

import static com.aks.app.fragment.PlaceholderFragment.createMarkerList;


public class MainActivity extends AppCompatActivity {

    private static final int EXIT_TIME_OUT = 2000;
    private int backPressCount = 0;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private static final String hasContactFetched = "hasContactFetched";

    private static final String updateMarker = "updateMarker";

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    /**
     * Fragment variables
     */
    private FragmentTransaction fragmentTransaction;
    private PlaceholderFragment placeholderFragment;
    private Context context;
    private ProgressDialog progressDialog;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        SupervisorApplication supervisorApplication = new SupervisorApplication(this);
        progressDialog = new ProgressDialog(context);
        setContentView(R.layout.activity_main);

        //Toolbar
        initToolbar();

        //FAB
        initFAB();

        //ViewPager and Tab together
        initViewPagerAndTabs();
        initTabLayout();

    }

    private void initToolbar() {
        //Toolbar inflation
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initFAB() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("onClick", "onClick");
                if (isNetworkAvailable()) {
                    //JSON from URL
                    if (!SharedPreferenceManager.getInstance().getBoolean(hasContactFetched)) {
                        getJSONFromURL();
                        SharedPreferenceManager.getInstance().putBoolean(updateMarker, true);
                    } else
                        ToastManager.getInstance().showSimpleToastShort("Contacts already saved");
                } else {
                    showSnackBar();
                }
            }
        });
    }

    private void showSnackBar() {
        Snackbar.make(findViewById(R.id.main_content), "Check your Internet Connection", Snackbar.LENGTH_LONG)
                .show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void initViewPagerAndTabs() {
        mViewPager = (ViewPager) findViewById(R.id.container);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFragment(PlaceholderFragment.newInstance(0, getSupportFragmentManager()), "ALL CONTACTS");
        mSectionsPagerAdapter.addFragment(PlaceholderFragment.newInstance(1, getSupportFragmentManager()), "CONTACTS MAP");
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(5);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("onPageScrolled", "onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("onPageSelected", "onPageSelected");
                if (SharedPreferenceManager.getInstance().getBoolean(updateMarker)) {
                    SharedPreferenceManager.getInstance().putBoolean(updateMarker, false);
                    mSectionsPagerAdapter.notifyDataSetChanged();
                    //updating list again to prevent state loss
                    if (null != PlaceholderFragment.getRecyclerAdapter()) {
                        new PlaceholderFragment().createContactList();
                        PlaceholderFragment.getRecyclerAdapter().notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("onPageScrollStateChanged", "onPageScrollStateChanged");
            }
        });
    }

    private void initTabLayout() {
        // Tablayout for Tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        //the below title will be used if you don't use "tabLayout.setupWithViewPager(mViewPager);"
        tabLayout.addTab(tabLayout.newTab().setText("All MarkerContacts"));
        tabLayout.addTab(tabLayout.newTab().setText("MarkerContacts Map"));

        // onTabSelectedListener
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.d("position ; ", "" + position);
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void getJSONFromURL() {
        Log.d("getJSONFromURL", "getJSONFromURL");
        AsyncTaskHandler.createInstance(context, progressDialog);
        AsyncTaskHandler.getInstance().execute();
        if (null != PlaceholderFragment.getRecyclerAdapter()) {
            new PlaceholderFragment().createContactList();
            PlaceholderFragment.getRecyclerAdapter().notifyDataSetChanged();
        }
        SharedPreferenceManager.getInstance().putBoolean(hasContactFetched, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != PlaceholderFragment.getRecyclerAdapter()) {
            new PlaceholderFragment().createContactList();
            PlaceholderFragment.getRecyclerAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        backPressCount++;
        if (backPressCount == 1) {
            ToastManager.getInstance().showSimpleToastShort("Press again to Exit");
        } else if (backPressCount == 2) {
            MainActivity.this.finish();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressCount = 0;
            }
        }, EXIT_TIME_OUT);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

}
