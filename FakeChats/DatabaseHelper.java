package com.eL.FakeChats;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Omar Sheikh on 8/1/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SeriesDatabase.db";
    public final String screenshotsTable = "ScreenShotsTable";
    public final String screenshotsTablePicturePaths = "ScreenShotsTablePicturePath";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        CreateScreenShotTable(sqLiteDatabase);

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    private void CreateScreenShotTable(SQLiteDatabase sqLiteDatabase){
        String sql = "CREATE TABLE " + screenshotsTable + " ( " +
                screenshotsTablePicturePaths+ " TEXT PRIMARY KEY);";
        sqLiteDatabase.execSQL(sql);
    }


}
