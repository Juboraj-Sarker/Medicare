package com.juborajsarker.medicare.activity.doctors;

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

import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.adapter.DoctorAdapter;
import com.juborajsarker.medicare.database.DoctorDatabase;
import com.juborajsarker.medicare.dataparser.GridSpacingItemDecoration;
import com.juborajsarker.medicare.model.DoctorModel;

import java.util.ArrayList;
import java.util.List;

public class DoctorsActivity extends AppCompatActivity {

    FloatingActionButton fabAdd;
    RecyclerView doctorRV;
    TextView messageTV;

    List<DoctorModel> doctorModelList;
    DoctorModel doctorModel;
    DoctorAdapter adapter;
    DoctorDatabase doctorDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        init();
        doctorModelList = doctorDatabase.getAllDoctor();

        if (doctorModelList.size() > 0){

            prepareForView();


        }else {

            doctorRV.setVisibility(View.GONE);
            messageTV.setVisibility(View.VISIBLE);

        }



    }

    private void init() {

        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        doctorRV = (RecyclerView) findViewById(R.id.recyclerView);
        messageTV = (TextView) findViewById(R.id.messageTV);

        doctorModelList = new ArrayList<>();
        doctorModel = new DoctorModel();
        doctorDatabase = new DoctorDatabase(this);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DoctorsActivity.this, AddDoctorActivity.class));
            }
        });

    }

    private void prepareForView(){


        messageTV.setVisibility(View.GONE);
        doctorRV.setVisibility(View.VISIBLE);

        doctorModelList.clear();
        doctorModelList = doctorDatabase.getAllDoctor();


        adapter = new DoctorAdapter(this, doctorModelList, doctorRV, adapter, "", messageTV, this);

        RecyclerView.LayoutManager layoutManagerBeforeMeal = new GridLayoutManager(this, 1);
        doctorRV.setLayoutManager(layoutManagerBeforeMeal);
        doctorRV.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(2), true));
        doctorRV.setItemAnimator(new DefaultItemAnimator());

        adapter.notifyDataSetChanged();
        doctorRV.setAdapter(adapter);
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
        doctorModelList.clear();
        doctorModelList = doctorDatabase.getAllDoctor();

        if (doctorModelList.size() > 0){

            prepareForView();


        }else {

            doctorRV.setVisibility(View.GONE);
            messageTV.setVisibility(View.VISIBLE);

        }
    }
}
