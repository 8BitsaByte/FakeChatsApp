package com.eL.FakeChats;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Omar Sheikh on 8/1/2017.
 */
public class DisplayScreenShotsRVAdapter extends RecyclerView.Adapter<DisplayScreenShotsRVAdapter.DisplayScreenshotsViewHolder> {

    Context context;
    Activity activity;
    ArrayList<String> screenhotsPaths;
    ArrayList<String> allScreenshotsPaths;
    ArrayList<ScreenshotClass> screenshotsArray;
    float screenshotWidth;
    float bitmapRatio;

    private LoadingInterface mCallback;


    public interface LoadingInterface {
        void onLoadingComplete();
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public DisplayScreenShotsRVAdapter(Context context, Activity activity, LoadingInterface mCallback){
        this.context = context;
        this.activity = activity;
        this.mCallback = mCallback;
        screenhotsPaths = new ArrayList<>();
        screenshotsArray = new ArrayList<>();
        allScreenshotsPaths = new ArrayList<>();
        screenshotWidth = GetScreenWidth() / 4 ;
        bitmapRatio = 4;
    }

    public float GetScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    @Override
    public DisplayScreenshotsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.displayscreenshotssinglescreenshot_layout,parent,false);
        return new DisplayScreenshotsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DisplayScreenshotsViewHolder viewHolder, int position) {
        if(screenshotsArray.size() > 0) {
            viewHolder.screenShotImageView.setImageBitmap(screenshotsArray.get(position).getScreenshotBitmap());
            SetScreenShotDimensions(viewHolder);
            SetViewHolderListners(viewHolder);
        }
    }
    public void SetViewHolderListners(final DisplayScreenshotsViewHolder viewHolder){
        viewHolder.screenShotImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,DisplaySingleScreenShot.class);
                intent.putExtra("CurrentPosition",viewHolder.getAdapterPosition());
                intent.putExtra("PathsArray",allScreenshotsPaths);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return screenshotsArray.size();
    }

    public void UpdateAdapter(ArrayList<String> screenhotsPaths){
        this.screenhotsPaths = new ArrayList<>(screenhotsPaths);
        if(screenhotsPaths.size() > 0)
            new LoadScreenShots().execute();
    }
    public void RefreshAdapter(ArrayList<String> screenhotsPaths){
        this.screenhotsPaths = new ArrayList<>(screenhotsPaths);
        this.allScreenshotsPaths = new ArrayList<>();
        this.screenshotsArray = new ArrayList<>();
        new LoadScreenShots().execute();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public class DisplayScreenshotsViewHolder extends RecyclerView.ViewHolder{
        ImageView screenShotImageView;
        public DisplayScreenshotsViewHolder(View itemView) {
            super(itemView);
            screenShotImageView = (ImageView) itemView.findViewById(R.id.DisplayScreenshotsSingleScreenshotImageView);
        }
    }

    public class LoadScreenShots extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            for(int i = 0 ; i < screenhotsPaths.size() ; i++){
                allScreenshotsPaths.add(screenhotsPaths.get(i));
                String SSPath = screenhotsPaths.get(i);
                Bitmap tempBitmap = LoadImageBitmap(context,SSPath);
                tempBitmap = GetResizedBitmap(tempBitmap,tempBitmap.getWidth()/ bitmapRatio,tempBitmap.getHeight() / bitmapRatio);
                if(tempBitmap != null)
                    screenshotsArray.add(new ScreenshotClass(SSPath,tempBitmap));
            }
            OnDataLoaded();
            return null;
        }
        public void OnDataLoaded(){
                activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                    mCallback.onLoadingComplete();
                }
            });
        }
    }
    public Bitmap LoadImageBitmap(Context context,String screenshotPath){
        FileInputStream fileInputStream = null;
        Bitmap bitmap = null;
        try{
            fileInputStream = context.openFileInput(screenshotPath);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fileInputStream != null;
                fileInputStream.close();
            } catch (IOException e) {
                //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
        return bitmap;
    }
    public Bitmap GetResizedBitmap(Bitmap bm, float newWidth, float newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
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
    public void SetScreenShotDimensions(DisplayScreenshotsViewHolder viewHolder){
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)screenshotWidth,(int)screenshotWidth);
        viewHolder.screenShotImageView.setLayoutParams(layoutParams);
        viewHolder.screenShotImageView.setPadding(dpToPx(5),dpToPx(5),dpToPx(5),dpToPx(5));
    }
}
