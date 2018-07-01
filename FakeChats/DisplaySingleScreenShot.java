package com.eL.FakeChats;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Omar Sheikh on 8/1/2017.
 */
public class DisplaySingleScreenShot extends Activity {

    int currentScreenShotIndex;
    ArrayList<String> screenShotPathsArray;

    ImageButton backButton, shareButton, deleteButton, hideFooterButton;
    public Bitmap currentBitmap;
    LinearLayout footerLinearLayout;
    RelativeLayout showFooterRelativeLayout;
    boolean isFooterShown;

    ViewPager viewPager;
    ImagePagerAdapter adapter;


    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;


    //For Permissions
    final static int PERMISSION_ALL = 1;

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    public void HideStatusBar(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void SetStatusBar(){
        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(DisplaySingleScreenShot.this,R.color.Black));
    }
    public void ShrinkFooter(){
        isFooterShown = false;
        showFooterRelativeLayout.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(footerLinearLayout);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        footerLinearLayout.setLayoutParams(layoutParams);
    }
    public void GrowFooter(){
        isFooterShown = true;
        showFooterRelativeLayout.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(footerLinearLayout);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        footerLinearLayout.setLayoutParams(layoutParams);
        footerLinearLayout.setMinimumHeight(dpToPx(50));

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displaysinglescreenshot_layout);
        SetStatusBar();
        InitializeStuff();
        SetListners();
        LoadDataFromPreviousActivity();
        SetViewPager();
    }

    public void LoadDataFromPreviousActivity(){
        Intent intent = getIntent();
        currentScreenShotIndex = intent.getExtras().getInt("CurrentPosition");
        screenShotPathsArray = new ArrayList<>((ArrayList<String>)intent.getSerializableExtra("PathsArray"));
    }

    public void SetViewPager(){
        viewPager = (ViewPager) findViewById(R.id.DisplaySingleScreenshotViewPager);
        adapter = new ImagePagerAdapter(screenShotPathsArray);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setCurrentItem(currentScreenShotIndex);
    }

    private class ImagePagerAdapter extends PagerAdapter {
        Context context;
        ArrayList<String> pathArray;

        public String ScreenShotPath(int position){
            return pathArray.get(position);
        }
        public void DeleteCurrentPosition(int position){
            pathArray.remove(position);
            this.notifyDataSetChanged();
        }
        public boolean ValidIndex(int index){
            return(index >= 0 && index < pathArray.size());
        }

        public ImagePagerAdapter(ArrayList<String> pathArray){
            this.pathArray = new ArrayList<>(pathArray);
        }
        @Override
        public int getCount() {
            return pathArray.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if(position  < pathArray.size()) {
                Context context = DisplaySingleScreenShot.this;
                final ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                imageView.setLayoutParams(layoutParams);
                int padding = dpToPx(5);
                imageView.setPadding(padding,padding,padding,0);
                currentBitmap = LoadImageBitmap(pathArray.get(position));
                imageView.setImageBitmap(currentBitmap);
                ((ViewPager) container).addView(imageView, 0);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
                return imageView;
            }
            return null;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
        public Bitmap LoadImageBitmap(String screenshotPath){
            FileInputStream fileInputStream = null;
            Bitmap bitmap = null;
            try{
                fileInputStream = getApplicationContext().openFileInput(screenshotPath);
                bitmap = BitmapFactory.decodeStream(fileInputStream);
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    assert fileInputStream != null;
                    fileInputStream.close();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
            return bitmap;
        }
    }

    public void InitializeStuff(){

        databaseHelper = new DatabaseHelper(getApplicationContext());
        sqLiteDatabase = databaseHelper.getWritableDatabase();

        backButton = (ImageButton) findViewById(R.id.DisplaySingleScreenshotBackButton);
        shareButton = (ImageButton) findViewById(R.id.DisplaySingleScreenshotShareButton);
        deleteButton = (ImageButton) findViewById(R.id.DisplaySingleScreenshotDeleteButton);
        hideFooterButton = (ImageButton) findViewById(R.id.DisplaySingleScreenshotHideFooter);

        isFooterShown = true;

        footerLinearLayout = (LinearLayout) findViewById(R.id.DisplaySingleScreenshotFooterLL);
        showFooterRelativeLayout = (RelativeLayout) findViewById(R.id.DisplaySingleScreenShotShowFooterRL);

    }
    public void SetListners(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentBitmap != null) {
                    if(CheckPermissions()) {
                        ShareScreenshotBitmap();
                    }
                    else
                        GetPermissions();
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteScreenshot();
            }
        });

        hideFooterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFooterShown)
                    ShrinkFooter();
            }
        });
        showFooterRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GrowFooter();
            }
        });
    }

    private void ShareScreenshotBitmap () {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        currentBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                currentBitmap, "Title", null);
        Uri imageUri =  Uri.parse(path);
        share.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(share, "Select"));
    }
    private void DeleteScreenshot(){
        int currPosition = viewPager.getCurrentItem();
        String ssPath = adapter.ScreenShotPath(currPosition);
        viewPager.setAdapter(null);
        adapter.DeleteCurrentPosition(currPosition);
        viewPager.setAdapter(adapter);
        if(adapter.ValidIndex(currPosition)) {
            viewPager.setCurrentItem(currPosition);
        }
        else if(adapter.ValidIndex(currPosition - 1)) {
            viewPager.setCurrentItem(currPosition - 1);
        }
        else{
            new DeleteScreenshotFromDatabase(ssPath).execute();
            finish();
        }
        new DeleteScreenshotFromDatabase(ssPath).execute();
    }

    public class DeleteScreenshotFromDatabase extends AsyncTask<Void,Void,Void>{

        String ssPath;
        public DeleteScreenshotFromDatabase(String ssPath){
            this.ssPath = ssPath;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            if(!ssPath.equals("")){
                File fdelete = new File(ssPath);
                if (fdelete.exists()) {
                    fdelete.delete();
                }
                sqLiteDatabase.delete(databaseHelper.screenshotsTable,databaseHelper.screenshotsTablePicturePaths + " = '" + ssPath + "'",null);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),"Image deleted",Toast.LENGTH_SHORT).show();
        }
    }



    //Handeling Permissions
    public void GetPermissions(){
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for(int i = 0 ; i < PERMISSIONS.length ; i++) {
            if (!HasPermissions(this, PERMISSIONS[i])) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }
        }
    }
    public Boolean CheckPermissions(){
        int writeStorage = DisplaySingleScreenShot.this.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(writeStorage == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
    public boolean HasPermissions(Context context, String permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            int result = DisplaySingleScreenShot.this.checkCallingOrSelfPermission(permissions);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL: {
                if(CheckPermissions()){
                    ShareScreenshotBitmap();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Could not share photo",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}