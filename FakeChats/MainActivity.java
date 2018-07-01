package com.eL.FakeChats;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.astuetz.PagerSlidingTabStrip;

public class MainActivity extends AppCompatActivity {
    private static final int numberOfTabs = 3;
    ViewPager viewPager;
    View focusView;
    FragmentPagerAdapterClass viewPagerAdapter;
    public void HideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void SetStatusBar(){
        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.grayShade2));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetStatusBar();
        SetViewPager();
        CheckToAskForRating();
    }
    private void SetViewPager(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(numberOfTabs); // Keep the data of the fragments loaded
        viewPagerAdapter = new FragmentPagerAdapterClass(getSupportFragmentManager(),getApplicationContext(), MainActivity.this);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                focusView= getCurrentFocus();
                if(focusView != null)
                    HideSoftKeyboard();

            }
            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
        tabsStrip.setIndicatorColor(Color.parseColor("#FFFFFF"));
        tabsStrip.setIndicatorHeight(DpToPx(2,getApplicationContext()));
        tabsStrip.setBackgroundColor(Color.parseColor("#4c4d4d"));
        tabsStrip.setTextColor(Color.parseColor("#FFFFFF"));
        tabsStrip.setDividerColor(Color.TRANSPARENT);
    }
    public static int DpToPx(float dp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

    public void CheckToAskForRating(){
        SharedPreferences prefs = getSharedPreferences("RatingPref", MODE_PRIVATE);
        int askedCounter = prefs.getInt("TimesAsked",0);
        if(askedCounter == -1)
            return;
        if(askedCounter == 2) {
            AskToRateOnGooglePlay();
            askedCounter = 0;
        }
        else
            askedCounter++;
        SharedPreferences.Editor editor = getSharedPreferences("RatingPref", MODE_PRIVATE).edit();
        editor.putInt("TimesAsked",askedCounter);
        editor.apply();
    }
    public void AskToRateOnGooglePlay(){
        new AlertDialog.Builder(this)
                .setTitle("Rate Us")
                .setMessage("If you like the app would you take out some time to rate it? " +
                        "\nThank you for the support!")
                .setIcon(R.drawable.googleplay_icon)
                .setPositiveButton("Rate Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        RateOnGooglePlay();
                    }})
                .setNegativeButton("Maybe Later", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }}).show();
    }
    public void RateOnGooglePlay(){
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
        }

        SharedPreferences.Editor editor = getSharedPreferences("RatingPref", MODE_PRIVATE).edit();
        editor.putInt("TimesAsked",-1);
        editor.apply();
    }
}
