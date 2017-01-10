package com.aks.app.toast_manager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aks.app.R;

/**
 * Created by Aakash Singh on 31-10-2016.
 */

public class ToastManager extends Toast {
    private static Context mContext = null;
    private static ToastManager toastManager = null;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param mContext The context to use.  Usually your {@link Application}
     *                 or {@link Activity} object.
     */
    private ToastManager(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    public static void createInstance(Context mContext) {
        toastManager = new ToastManager(mContext);
    }

    public static ToastManager getInstance() {
        return toastManager;
    }

    public static void showToast_LONG(String msg1, String msg2) {
        Toast.makeText(mContext, msg1 + " " + msg2, Toast.LENGTH_LONG).show();
    }

    public static void showToast_SHORT(String msg1, String msg2) {
        Toast.makeText(mContext, msg1 + " " + msg2, Toast.LENGTH_SHORT).show();
    }

    public void showSimpleToastShort(String msg) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setBackgroundResource(R.drawable.layout_toast_background);

        TextView tv = new TextView(mContext);
        // set the TextView properties like color, size etc
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(15);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        // set the text you want to show in  Toast
        tv.setText(msg);

//        ImageView img=new ImageView(mContext);
//        // give the drawble resource for the ImageView
//        img.setImageResource(R.drawable.layout_background_white_color);
//        // add both the Views TextView and ImageView in layout
//        layout.addView(img);
        layout.addView(tv);

        //context is object of Context write "this" if you are an Activity
        Toast toast = new Toast(mContext);
        // Set The layout as Toast View
        toast.setView(layout);
        // Position you toast here toast position is 50 dp from bottom you can give any integral value
        toast.setGravity(Gravity.BOTTOM, 0, 100);

        toast.setDuration(LENGTH_SHORT);
        toast.show();
    }


    public void showBoldToastShort(Context mContext, String msg_normal_1, String msg_bold_2, String msg_normal_3) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setBackgroundResource(R.drawable.layout_toast_background);

        TextView tv = new TextView(mContext);
        // set the TextView properties like color, size etc
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(15);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        // set the text you want to show in  Toast
        tv.setText(msg_normal_1 + " " + Html.fromHtml("<b>" + msg_bold_2 + "</b>") + " " + msg_normal_3);
//        ImageView img=new ImageView(mContext);
//        // give the drawble resource for the ImageView
//        img.setImageResource(R.drawable.layout_background_white_color);
//        // add both the Views TextView and ImageView in layout
//        layout.addView(img);
        layout.addView(tv);

        //context is object of Context write "this" if you are an Activity
        Toast toast = new Toast(mContext);
        // Set The layout as Toast View
        toast.setView(layout);
        // Position you toast here toast position is 50 dp from bottom you can give any integral value
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(LENGTH_SHORT);
        toast.show();
    }
}
