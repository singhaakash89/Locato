package com.aks.app.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aks.app.R;
import com.aks.app.adapter.SectionsPagerAdapter;
import com.aks.app.database.sharedPreference.SharedPreferenceManager;
import com.aks.app.fragment.PlaceholderFragment;
import com.aks.app.json_parser.AsyncTaskHandler;
import com.aks.app.supervisor.SupervisorApplication;


public class MainActivity extends AppCompatActivity {

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
                //JSON from URL
//                if (!SharedPreferenceManager.getInstance().getBoolean(hasContactFetched)) {
                if (true) {
                    getJSONFromURL();
                    //update fragments
                    PlaceholderFragment.getRecyclerAdapter().notifyDataSetChanged();
                } else
                    Toast.makeText(context, "Contacts already saved", Toast.LENGTH_SHORT).show();
            }
        });
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
        SharedPreferenceManager.getInstance().putBoolean(hasContactFetched, true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Toolbar
        initToolbar();

        //FAB
        initFAB();

        //ViewPager and Tab together
        initViewPagerAndTabs();
        initTabLayout();
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
