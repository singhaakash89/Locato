package com.aks.app.supervisor;

import android.content.Context;

import com.aks.app.database.PrimaryDBProvider;
import com.aks.app.database.StandardStorageHelper;
import com.aks.app.database.sharedPreference.SharedPreferenceManager;
import com.aks.app.toast_manager.ToastManager;

/**
 * Created by Aakash Singh on 07-01-2017.
 */

public class SupervisorApplication {

    private static Context context;

    public SupervisorApplication(Context mContext) {
        try {
            PrimaryDBProvider.createInstance(mContext);
            StandardStorageHelper.createInstance(mContext);
            PrimaryDBProvider.getInstance().getWritableDatabase();
            SharedPreferenceManager.createInstance(mContext);
            ToastManager.createInstance(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Context getContext() {
        return context;
    }

}
