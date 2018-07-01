package com.eL.FakeChats;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;

/**
 * Created by Omar Sheikh on 7/26/2017.
 */
public class CreateFakeUser extends AppCompatActivity {

    public static Activity UserActivity;


    ImageView profilePictureImageView;
    ImageButton profilePictureOptionsButton,profilePictureMaleButton,profilePitureFemaleButton,profilePictureGalleryButton,profilePictureCameraButton,profilePictureRemoveButton, profilePictureRotateButton;

    ImageButton nextButton, backButton;

    TextView usernameTextView;

    RelativeLayout usernameRelativeLayout;
    RelativeLayout profilePictureOptionsRelativeLayout;
    RelativeLayout closeProfilePictureOptionsRelativeLayout;

    public static final int changeUsernameRequestCodeID = 10;
    public static final int GalleryRequestCodeID = 1;
    public static final int CameraRequestCodeID = 2;

    public boolean optionsRelativeLayoutShown = false;
    public boolean returnFromNextActivityBoolean = false;

    Bitmap profilePicBitmap;


    //Add Mob
    private AdView footerAd;

    //For Permissions
    final static int PERMISSION_ALL = 1;
    boolean cameraOrGalleryBoolean ; // true for camera , false for gallery
    Button permissionsButton;

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void SetStatusBar(){
        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(CreateFakeUser.this,R.color.grayShade2));
    }

    public void ShrinkPictureOptionsRelativeLayout(){
        optionsRelativeLayoutShown = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(profilePictureOptionsRelativeLayout);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dpToPx(0));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        profilePictureOptionsRelativeLayout.setLayoutParams(layoutParams);
    }
    public void GrowOptionsRelativeLayout(){
        optionsRelativeLayoutShown = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(profilePictureOptionsRelativeLayout);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.BELOW,R.id.CreateFakeUserProfilePicRL);
        layoutParams.topMargin = dpToPx(10);
        profilePictureOptionsRelativeLayout.setLayoutParams(layoutParams);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createfakeuser_layout);
        UserActivity = this;
        SetStatusBar();
        InitializeStuff();
        SetListners();
        HandleAds();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(returnFromNextActivityBoolean)
            profilePictureImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.male_default));
        returnFromNextActivityBoolean = false;
    }
    @Override
    public void onBackPressed() {
        if(optionsRelativeLayoutShown)
            ShrinkPictureOptionsRelativeLayout();
        else
            finish();
    }



    public void InitializeStuff(){
        profilePictureImageView = (ImageView) findViewById(R.id.CreateFakeUserProfilePictureImageView);
        profilePictureImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.male_default));

        profilePictureOptionsButton = (ImageButton) findViewById(R.id.CreateFakeUserProfilePictureOptionsButton);
        profilePictureMaleButton = (ImageButton) findViewById(R.id.CreateFakeUserProfilePictureMaleImageButton);
        profilePitureFemaleButton = (ImageButton) findViewById(R.id.CreateFakeUserProfilePictureFemaleImageButton);

        profilePictureGalleryButton = (ImageButton) findViewById(R.id.CreateFakeUserProfilePictureSelectFromGallery);
        profilePictureCameraButton = (ImageButton) findViewById(R.id.CreateFakeUserProfilePictureTakePicture);
        profilePictureRemoveButton = (ImageButton) findViewById(R.id.CreateFakeUserProfilePictureRemovePicture);

        profilePictureRotateButton = (ImageButton) findViewById(R.id.CreateFakeUserProfileRotate);


        profilePictureOptionsRelativeLayout = (RelativeLayout) findViewById(R.id.CreateFakeUserProfilePictureOptionsRL);
        closeProfilePictureOptionsRelativeLayout = (RelativeLayout) findViewById(R.id.CreateFakeUserClosePictureOptionsRL);

        usernameTextView = (TextView) findViewById(R.id.CreateFakeUserUsernameTextView);
        usernameRelativeLayout = (RelativeLayout) findViewById(R.id.CreateFakeUserUsernameRelativeLayout);

        backButton = (ImageButton) findViewById(R.id.CreateFakeUserBack);
        nextButton = (ImageButton) findViewById(R.id.CreateFakeUserNext);



        permissionsButton = (Button) findViewById(R.id.CreateFakeUserPermissionsButton);

    }
    public void SetListners(){
        profilePictureOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GrowOptionsRelativeLayout();
            }
        });

        profilePictureMaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePictureImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.male_default));
                ShrinkPictureOptionsRelativeLayout();
            }
        });

        profilePitureFemaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePictureImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.female_default));
                ShrinkPictureOptionsRelativeLayout();
            }
        });

        profilePictureRotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               profilePictureImageView.setImageBitmap(RotateImage(((BitmapDrawable)profilePictureImageView.getDrawable()).getBitmap(),90));
            }
        });

        closeProfilePictureOptionsRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShrinkPictureOptionsRelativeLayout();
            }
        });

        profilePictureGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckPermissions()) {
                   GetImageFromGallery();
                }
                else {
                    cameraOrGalleryBoolean = false;
                    GetPermissions();
                }
            }
        });
        profilePictureCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckPermissions()) {
                   GetImageFromCamera();
                }
                else {
                    cameraOrGalleryBoolean = true;
                    GetPermissions();
                }
            }
        });
        profilePictureRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePictureImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.male_default));
            }
        });

        usernameRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!optionsRelativeLayoutShown) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                    Intent intent = new Intent(CreateFakeUser.this, EditUsername.class);
                    intent.putExtra("CurrentUsername",usernameTextView.getText().toString());
                    startActivityForResult(intent, changeUsernameRequestCodeID);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!optionsRelativeLayoutShown)
                    finish();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!optionsRelativeLayoutShown) {
                    GotoNextActivity();
                }
            }
        });

        permissionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
            }
        });


    }
    public void HandleAds(){
        footerAd = (AdView) findViewById(R.id.FooterAds);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        footerAd.loadAd(adRequest);
    }

    public void GetImageFromGallery(){
        ShrinkPictureOptionsRelativeLayout();
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, GalleryRequestCodeID);
    }
    public void GetImageFromCamera(){
        ShrinkPictureOptionsRelativeLayout();
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CameraRequestCodeID);
    }
    public void GotoNextActivity(){
        if(!usernameTextView.getText().toString().equals("")) {
            returnFromNextActivityBoolean = true;
            profilePicBitmap = ((BitmapDrawable)profilePictureImageView.getDrawable()).getBitmap();
            float bitmapRatio = CalculateBitmapRatio(profilePicBitmap);
            Intent intent = new Intent(CreateFakeUser.this, CreateFakeChat.class);
            intent.putExtra("Username",usernameTextView.getText().toString());
            intent.putExtra("ProfilePicture",GetResizedBitmap(profilePicBitmap,profilePicBitmap.getWidth()/bitmapRatio,profilePicBitmap.getHeight()/bitmapRatio));
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            profilePicBitmap = null;
            profilePictureImageView.setImageBitmap(null);
        }
        else{
            Toast.makeText(getApplicationContext(),"Please enter a username", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == CameraRequestCodeID){

            if(data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                profilePictureImageView.setImageBitmap(photo);
            }
            else
                Toast.makeText(getApplicationContext(),"Error capturing image",Toast.LENGTH_SHORT).show();

        }
        else if(requestCode == GalleryRequestCodeID && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
                profilePicBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                profilePictureImageView.setImageBitmap(profilePicBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(resultCode == RESULT_OK && requestCode == changeUsernameRequestCodeID){

            String newUsername =  data.getExtras().getString("NewUsername");
            if(newUsername != null)
                usernameTextView.setText(newUsername);
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
        return resizedBitmap;
    }

    private Bitmap RotateImage(Bitmap source, float angle) {
        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }

    public float CalculateBitmapRatio(Bitmap bitmap){
        float smallestValue = 0;
        if(bitmap.getHeight() <= bitmap.getWidth())
            smallestValue = bitmap.getHeight();
        else
            smallestValue = bitmap.getWidth();
        if(smallestValue > 300 && smallestValue != 0)
            return smallestValue/300;
        else
            return 1;
    }


    //Handeling Permissions
    public void GetPermissions(){
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        for(int i = 0 ; i < PERMISSIONS.length ; i++) {
            if (!HasPermissions(this, PERMISSIONS[i])) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }
        }
    }
    public Boolean CheckPermissions(){
        int cameraResults = CreateFakeUser.this.checkCallingOrSelfPermission(Manifest.permission.CAMERA);
        int writeStorage = CreateFakeUser.this.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(cameraResults == PackageManager.PERMISSION_GRANTED && writeStorage == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
    public boolean HasPermissions(Context context, String permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            int result = CreateFakeUser.this.checkCallingOrSelfPermission(permissions);
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
                    if(cameraOrGalleryBoolean)
                        GetImageFromCamera();
                    else
                        GetImageFromGallery();
                    permissionsButton.setVisibility(View.GONE);
                }
                else{
                    permissionsButton.setVisibility(View.VISIBLE);
                }
                return;
            }
        }
    }



}