package com.juborajsarker.medicare.activity.diary;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.adapter.DiaryAdapter;
import com.juborajsarker.medicare.database.DiaryDatabase;
import com.juborajsarker.medicare.dataparser.GridSpacingItemDecoration;
import com.juborajsarker.medicare.model.DiaryModel;

import java.util.ArrayList;
import java.util.List;

public class DiaryActivity extends AppCompatActivity {

    FloatingActionButton fabAdd;
    RecyclerView recyclerView;
    TextView messageTV;

    List<DiaryModel> diaryModelList;
    DiaryModel diaryModel;
    DiaryAdapter adapter;
    DiaryDatabase diaryDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_home_footer_1));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("93448558CC721EBAD8FAAE5DA52596D3").build();
        mAdView.loadAd(adRequest);

        init();
        diaryModelList = diaryDatabase.getAllDiary();

        if (diaryModelList.size() > 0){

            prepareForView();


        }else {

            recyclerView.setVisibility(View.GONE);
            messageTV.setVisibility(View.VISIBLE);

        }


    }


    private void init() {

        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        messageTV = (TextView) findViewById(R.id.messageTV);

        diaryModelList = new ArrayList<>();
        diaryModel = new DiaryModel();
        diaryDatabase = new DiaryDatabase(this);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DiaryActivity.this, AddDiaryActivity.class));
            }
        });

    }

    private void prepareForView(){


        messageTV.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        diaryModelList.clear();
        diaryModelList = diaryDatabase.getAllDiary();


        adapter = new DiaryAdapter(this, diaryModelList, recyclerView, adapter, "", messageTV, this);

        RecyclerView.LayoutManager layoutManagerBeforeMeal = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManagerBeforeMeal);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(2), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                super.onBackPressed();

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        diaryModelList.clear();
        diaryModelList = diaryDatabase.getAllDiary();

        if (diaryModelList.size() > 0){

            prepareForView();


        }else {

            recyclerView.setVisibility(View.GONE);
            messageTV.setVisibility(View.VISIBLE);

        }
    }
}
