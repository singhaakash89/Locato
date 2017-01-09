package com.aks.app.Logger;

import android.util.Log;

/**
 * Created by AA333093 on 08-Aug-16.
 */
public class Logger {
    private static final String TAG = "Logger";

    public static void putInDebugLog(String TAG, String msg1, String msg2) {
        if (msg1.equals(null)) {
            Log.d(TAG + " : null : ", " : " + msg2 + " ; \n");
        } else if (msg2.equals(null)) {
            Log.d(TAG + " : " + msg1 + " : ", " : null ; \n");
        } else {
            Log.d(TAG + " : " + msg1 + " : ", " : " + msg2 + " ; \n");
        }
    }

    public static void putInErrorLog(String TAG, String msg1, String msg2) {
        if (msg1.equals(null)) {
            Log.d(TAG + " : null : ", " : " + msg2 + " ; \n");
        } else if (msg2.equals(null)) {
            Log.d(TAG + " : " + msg1 + " : ", " : null ; \n");
        } else {
            Log.d(TAG + " : " + msg1 + " : ", " : " + msg2 + " ; \n");
        }
    }


}
