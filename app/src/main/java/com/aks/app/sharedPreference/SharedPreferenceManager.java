package com.aks.app.sharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Aakash Singh on 28-10-2016.
 */
public class SharedPreferenceManager implements SharedPreferenceContract {

    private SharedPreferences sharedPreferences;
    private Context mContext;
    private static SharedPreferenceManager sharedPreferenceManager;

    public SharedPreferenceManager(Context mContext) {
        this.mContext = mContext;
    }

    public static void createInstance(Context mContext) {
        sharedPreferenceManager = new SharedPreferenceManager(mContext);
    }

    public static SharedPreferenceManager getInstance() {
        return sharedPreferenceManager;
    }

    public void putString(String key, String value) {
        //Opening sharedPreferences File to write
        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences = mContext.getSharedPreferences(USER_DATA, 0); //0 - for private Mode

        //---Using SharedPreference for saving Data----
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Opening editor to write into sharedpreference
        editor.putString(key, value);

        //commiting changes
        editor.commit();

    }

    public void putBoolean(String key, boolean value) {
        //Opening sharedPreferences File to write
        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences = mContext.getSharedPreferences(USER_DATA, 0); //0 - for private Mode

        //---Using SharedPreference for saving Data----
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Opening editor to write into sharedpreference
        editor.putBoolean(String.valueOf(key), value);

        //commiting changes
        editor.commit();
    }

    public boolean getBoolean(String key) {
        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences = mContext.getSharedPreferences(USER_DATA, 0); //0 - for private Mode
        return (sharedPreferences.getBoolean(String.valueOf(key), BOOLEAN_DEFAULT));
    }

    public String getString(String key) {
        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences = mContext.getSharedPreferences(USER_DATA, 0); //0 - for private Mode
        return (sharedPreferences.getString(key, STRING_DEFAULT));
    }

}
