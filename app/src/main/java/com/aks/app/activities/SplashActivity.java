package com.aks.app.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aks.app.R;

/**
 * Created by Aakash Singh on 24-10-2016.
 */
public class SplashActivity extends AppCompatActivity {

    private boolean isAlreadyRegistered = false;
    private Context mContext;
    private final static int SPLASH_TIME_OUT = 3000;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //**************Hiding status bar*************************
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
//**********************************************************

//**************cHANGINF STATUS BAR COLOR*******************
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.black));
//**********************************************************

//******hiding AvtionBAR IF EXISTS*****************************
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();
//************************************************************

        setContentView(R.layout.activity_splash_new);
        //Setting opacity of textview-developer
        float alpha = 0.7f;
        ((TextView) findViewById(R.id.devInfo)).setAlpha(alpha);

//***********************Important********************************
//*************Resizing any image to full screen*********************
//        //taking display dimensions
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//
//        //resizing image
//        ImageView iv = (ImageView) findViewById(R.id.imageView);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_splash);
//        Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, size.x, size.y, true);
//        iv.setImageBitmap(bitmapScaled);
//*******************************************************************

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
