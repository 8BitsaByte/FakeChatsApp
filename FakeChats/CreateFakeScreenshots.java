package com.eL.FakeChats;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Omar Sheikh on 7/30/2017.
 */
public class CreateFakeScreenshots extends AppCompatActivity {


    ArrayList<String>allScreenshotsPaths;
    ArrayList<FakeChatClass> chatArray;
    String friendUsername;
    Bitmap friendProfilePictureBitmap;

    CreateFakeScreenshotsRVAdapter fakeScreenshotsAdapter;
    RecyclerView fakeScreenshotsRecyclerView;

    TextView friendUsernameTextView;

    ImageButton backButton;

    //Screenshot
    RelativeLayout mainRelativeLayout;
    RelativeLayout takeScreenShotRelativLayout;
    LinearLayout takeScreenShotLinearLayout;

    CircleImageView displayScreenShotsImageView;
    ImageButton resizeFooterImageButton,homeButton;
    RelativeLayout resizeFooterRL;
    RelativeLayout footerRL;
    boolean isFooterShown;


    FrameLayout pnlFlash;

    SQLiteDatabase sqlDatabase;
    DatabaseHelper databaseHelper;

    //Admob
    private InterstitialAd interstitial;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void SetStatusBar(){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(CreateFakeScreenshots.this,R.color.facebookTitleBarColor));
    }


    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    public void ShrinkFooter(){
        isFooterShown = false;
        resizeFooterRL.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(footerRL);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        footerRL.setLayoutParams(layoutParams);
    }
    public void GrowFooter(){
        isFooterShown = true;
        resizeFooterRL.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(footerRL);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        footerRL.setLayoutParams(layoutParams);
        footerRL.setMinimumHeight(dpToPx(55));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createfakescreenshots_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SetStatusBar();
        InitializeStuff();
        SetListners();
        LoadFromPreviousActivity();
        SetAdapterandRecyclerView();
        AssignValues();
        HandleAds();
    }
    public void InitializeStuff(){

        allScreenshotsPaths = new ArrayList<>();

        friendUsernameTextView = (TextView) findViewById(R.id.CreateFakeScreenShotsUsernameTextView);
        backButton = (ImageButton) findViewById(R.id.CreateFakeScreenShotsBackButton);

        isFooterShown = true;
        footerRL = (RelativeLayout) findViewById(R.id.CreateFakeScreenshotsTakeScreenShotMainRL);
        resizeFooterRL = (RelativeLayout) findViewById(R.id.CreateFakeScreenShotsResizeFooter);
        resizeFooterImageButton = (ImageButton) findViewById(R.id.CreateFakeScreenshotsResizeFooterImageButton);
        homeButton = (ImageButton) findViewById(R.id.CreateFakeScreenshotsHome);

        mainRelativeLayout = (RelativeLayout) findViewById(R.id.CreateFakeScreenshotMainRL);
        takeScreenShotRelativLayout = (RelativeLayout) findViewById(R.id.CreateFakeScreenshotScreenshotPortionRL);
        takeScreenShotLinearLayout = (LinearLayout) findViewById(R.id.CreateFakeScreenshotsTakeScreenShotLL);

        displayScreenShotsImageView = (CircleImageView) findViewById(R.id.CreateFakeScreenshotsScreenshotsDisplay);
        pnlFlash = (FrameLayout) findViewById(R.id.pnlFlash);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        sqlDatabase = databaseHelper.getWritableDatabase();

    }
    public void SetListners(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        takeScreenShotLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TakeScreenshot();
            }
        });
        displayScreenShotsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allScreenshotsPaths.size() > 0) {
                    Intent intent = new Intent(getApplicationContext(), DisplaySingleScreenShot.class);
                    intent.putExtra("CurrentPosition", 0);
                    intent.putExtra("PathsArray", allScreenshotsPaths);
                    startActivity(intent);
                }
            }
        });
        resizeFooterRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFooterShown)
                    ShrinkFooter();
                else
                    GrowFooter();
            }
        });
        resizeFooterImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShrinkFooter();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateFakeUser.UserActivity.finish();
                CreateFakeChat.ChatActivity.finish();
                finish();
            }
        });
    }
    public void HandleAds(){
        AdRequest adRequest = new AdRequest.Builder().build();
        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(CreateFakeScreenshots.this);
// Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.full_ad));
        interstitial.loadAd(adRequest);
// Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
// Call displayInterstitial() function
                displayInterstitial();
            }
        });
    }
    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
    public void LoadFromPreviousActivity(){
        Intent intent = getIntent();
        if(intent != null) {
            friendUsername = intent.getExtras().getString("Username");
            friendProfilePictureBitmap = (Bitmap) intent.getParcelableExtra("ProfilePicture");
            chatArray = (ArrayList<FakeChatClass>) intent.getSerializableExtra("ChatArray");
        }
    }
    public void SetAdapterandRecyclerView(){
        fakeScreenshotsAdapter = new CreateFakeScreenshotsRVAdapter(getApplicationContext(),chatArray,friendProfilePictureBitmap);
        fakeScreenshotsRecyclerView = (RecyclerView) findViewById(R.id.CreateFakeScreenshotsRecyclerView);
        fakeScreenshotsRecyclerView.setAdapter(fakeScreenshotsAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        fakeScreenshotsRecyclerView.setLayoutManager(mLayoutManager);

    }
    public void AssignValues(){
        friendUsernameTextView.setText(friendUsername);
    }
    public Bitmap ScreenShotBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
    public void TakeScreenshot(){
        takeScreenShotLinearLayout.setVisibility(View.GONE);
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {

            Bitmap bitmap = ScreenShotBitmap(takeScreenShotRelativLayout);
            String tempSSPath = String.valueOf(System.currentTimeMillis()) + ".JPEG";
            SaveScreenShotInInternalStorage(getApplicationContext(),bitmap,tempSSPath);
            ScreenshotAnimation();
            displayScreenShotsImageView.setImageBitmap(GetResizedBitmap(bitmap,bitmap.getWidth()/5,bitmap.getHeight()/5));
            allScreenshotsPaths.add(0,tempSSPath);
            new SavePathInDatabase(tempSSPath).execute();

        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        takeScreenShotLinearLayout.setVisibility(View.VISIBLE);
    }

    public void SaveScreenShotInInternalStorage(Context context, Bitmap bitmap, String screenshotPath){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(screenshotPath, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fileOutputStream != null;
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void ScreenshotAnimation(){
        pnlFlash.setVisibility(View.VISIBLE);
        AlphaAnimation fade = new AlphaAnimation(1, 0);
        fade.setDuration(2500);
        fade.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                pnlFlash.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation anim) {
                pnlFlash.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        pnlFlash.startAnimation(fade);
    }

    public class SavePathInDatabase extends AsyncTask<Void,Void,Void>{

       String screenshotPath;

       public SavePathInDatabase(String screenshotPath){
           this.screenshotPath = screenshotPath;
       }

       @Override
       protected Void doInBackground(Void... voids) {
           ContentValues contentValues = new ContentValues();
           contentValues.put(databaseHelper.screenshotsTablePicturePaths,screenshotPath);
           sqlDatabase.insert(databaseHelper.screenshotsTable,null,contentValues);
           return null;
       }
   }

    public Bitmap GetResizedBitmap(Bitmap bm, float newWidth, float newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = (newWidth) / width;
        float scaleHeight = ( newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }



}
