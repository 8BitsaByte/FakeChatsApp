package com.eL.FakeChats;

import android.graphics.Bitmap;

/**
 * Created by Omar Sheikh on 8/1/2017.
 */
public class ScreenshotClass {
    String screenshotPath;
    Bitmap screenshotBitmap;

    public ScreenshotClass(String screenshotPath, Bitmap screenshotBitmap){
        this.screenshotPath = screenshotPath;
        this.screenshotBitmap = screenshotBitmap;
    }

    public Bitmap getScreenshotBitmap() {
        return screenshotBitmap;
    }
    public String getScreenshotPath() {
        return screenshotPath;
    }
}
