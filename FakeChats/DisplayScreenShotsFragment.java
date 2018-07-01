package com.eL.FakeChats;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Omar Sheikh on 8/1/2017.
 */
public class DisplayScreenShotsFragment extends Fragment implements DisplayScreenShotsRVAdapter.LoadingInterface{

    View view;


    ArrayList<String> screenshotPaths;
    DisplayScreenShotsRVAdapter screenshotsAdapter;
    RecyclerView screenshotsRecyclerView;

    SwipeRefreshLayout screenShotsRefresh;
    boolean isRefreshed;

    //Sqlite Database
    SQLiteDatabase sqlDatabase;
    DatabaseHelper databaseHelper;
    int enteriesAtOnce;
    int lowerRange;
    int upperRange;
    boolean completeDataLoaded = false;
    boolean firstTime = true;


   // ProgressBar progressBar;
    CardView progressBarCardView;
    // Add screenshot button
    ImageButton addScreenShotButton;

    TextView noScreenShotsTextView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.displayscreenshotsfragment_layout,null,false);
        InitializeStuff();
        SetListners();
        SetAdapterandRecyclerView();
        new LoadPathsFromDatabase().execute();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!firstTime)
            RefreshScreenShotList();
        else
            firstTime = false;
    }
    public void InitializeStuff(){
        screenshotPaths = new ArrayList<>();
        databaseHelper = new DatabaseHelper(getContext());
        sqlDatabase = databaseHelper.getReadableDatabase();
        enteriesAtOnce = 30;
        lowerRange = 0;
        upperRange = lowerRange + enteriesAtOnce;
        completeDataLoaded = false;

        //progressBar = (ProgressBar) view.findViewById(R.id.DisplayScreenshotsProgressBar);
        progressBarCardView = (CardView) view.findViewById(R.id.ProgressBarCardView);

        addScreenShotButton = (ImageButton) view.findViewById(R.id.DisplayScreenshotsAddScreenshot);

        screenShotsRefresh = (SwipeRefreshLayout) view.findViewById(R.id.FullViewRefresh);
        isRefreshed = false;

        noScreenShotsTextView = (TextView) view.findViewById(R.id.DisplayScreenshotsNoScreenshotsTV);
    }
    public void SetListners(){
       addScreenShotButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getContext(),CreateFakeUser.class);
               startActivity(intent);

           }
       });

        screenShotsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshScreenShotList();
            }
        });
    }
    public void SetAdapterandRecyclerView(){
        screenshotsAdapter = new DisplayScreenShotsRVAdapter(getContext(),getActivity(),this);
        screenshotsRecyclerView = (RecyclerView) view.findViewById(R.id.DisplayScreenshotsRecyclerView);

        screenshotsRecyclerView.setAdapter(screenshotsAdapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 4);
        screenshotsRecyclerView.setLayoutManager(gridLayoutManager);

        screenshotsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(gridLayoutManager.findLastCompletelyVisibleItemPosition() == screenshotsAdapter.getItemCount() - 1 && !completeDataLoaded){
                    progressBarCardView.setVisibility(View.VISIBLE);
                    new LoadPathsFromDatabase().execute();
                }
            }
        });


    }
    public void UpdateDataandAdapter(){
        if(!isRefreshed){
            if(screenshotPaths.size() == 0) {
                completeDataLoaded = true;
                progressBarCardView.setVisibility(View.GONE);
                if(lowerRange == 0)
                    noScreenShotsTextView.setVisibility(View.VISIBLE);
            }
            if(!completeDataLoaded) {
                screenshotsAdapter.UpdateAdapter(new ArrayList<>(screenshotPaths));
            }
        }
        else{
            if(screenshotPaths.size() == 0) {
                completeDataLoaded = true;
                progressBarCardView.setVisibility(View.GONE);
                if(lowerRange == 0)
                    noScreenShotsTextView.setVisibility(View.VISIBLE);
            }
            screenshotsAdapter.RefreshAdapter(new ArrayList<>(screenshotPaths));
            isRefreshed = false;
        }

        screenshotPaths.clear();
        screenShotsRefresh.setRefreshing(false);
        lowerRange = upperRange + 1;
        upperRange += enteriesAtOnce;
    }
    public void RefreshScreenShotList(){
        screenShotsRefresh.setRefreshing(true);
        noScreenShotsTextView.setVisibility(View.GONE);
        isRefreshed = true;
        lowerRange = 0;
        upperRange = lowerRange + enteriesAtOnce;
        completeDataLoaded = false;
        screenshotPaths = new ArrayList<>();
        new LoadPathsFromDatabase().execute();
    }

    @Override
    public void onLoadingComplete() {
        progressBarCardView.setVisibility(View.GONE);
    }
    public class LoadPathsFromDatabase extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            Cursor cursor = sqlDatabase.rawQuery("Select * from " + databaseHelper.screenshotsTable +
                    " ORDER BY " + databaseHelper.screenshotsTablePicturePaths + " DESC LIMIT "+ lowerRange + "," + upperRange ,null);

            cursor.moveToFirst();
            if(cursor.getCount() > 0) {
                do {
                    screenshotPaths.add(cursor.getString(cursor.getColumnIndex(databaseHelper.screenshotsTablePicturePaths)));
                } while (cursor.moveToNext());
            }
            OnDataLoaded();
            return null;
        }
        public void OnDataLoaded(){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UpdateDataandAdapter();
                }
            });
        }
    }


}
