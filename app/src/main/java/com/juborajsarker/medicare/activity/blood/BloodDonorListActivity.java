package com.juborajsarker.medicare.activity.blood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.adapter.DonorAdapter;
import com.juborajsarker.medicare.dataparser.GridSpacingItemDecoration;
import com.juborajsarker.medicare.model.DonorModel;

import java.util.ArrayList;
import java.util.List;

public class BloodDonorListActivity extends AppCompatActivity {

    TextView messageTV, helpTV;
    RecyclerView recyclerView;

    String city, country, bloodGroup;

    DatabaseReference databaseReference;
    List<DonorModel> donorModelList;
    DonorAdapter adapter;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_donor_list);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_home_footer_1));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("93448558CC721EBAD8FAAE5DA52596D3").build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        country = intent.getStringExtra("country");
        bloodGroup = intent.getStringExtra("bloodGroup");

        init();

    }

    private void init() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data. Please wait....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        messageTV = (TextView) findViewById(R.id.messageTV);
        helpTV = (TextView) findViewById(R.id.helpTV);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        helpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(BloodDonorListActivity.this, DonorInputActivity.class));

            }
        });

        donorModelList = new ArrayList<>();

        donorModelList.clear();
        recyclerView.removeAllViewsInLayout();



        databaseReference = FirebaseDatabase.getInstance().getReference("User/Donor/" +
                country + "/" + city.toLowerCase() + "/" + bloodGroup);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                donorModelList.clear();

                for (DataSnapshot donor : dataSnapshot.getChildren()){

                    DonorModel donorModel = donor.getValue(DonorModel.class);
                    donorModelList.add(donorModel);
                }


                if (donorModelList.size() >0){

                    messageTV.setVisibility(View.VISIBLE);
                    messageTV.setText("Congrats !!! " + donorModelList.size() + " (" + bloodGroup + ") donor found in " + city) ;
                    messageTV.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                    adapter = new DonorAdapter(BloodDonorListActivity.this, donorModelList, recyclerView, adapter, BloodDonorListActivity.this);
                    RecyclerView.LayoutManager layoutManagerBeforeMeal = new GridLayoutManager(BloodDonorListActivity.this, 1);
                    recyclerView.setLayoutManager(layoutManagerBeforeMeal);
                    recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(2), true));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    progressDialog.dismiss();

                }else {

                    messageTV.setVisibility(View.VISIBLE);
                    messageTV.setText("Sorry, No " + bloodGroup + " donor found in "+ city+ ". \nShowing " + bloodGroup + " donor for all over in " + country);
                    messageTV.setBackgroundColor(getResources().getColor(R.color.colorRed));
                    progressDialog.dismiss();

                    executeForNotFound();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void executeForNotFound() {

        databaseReference = FirebaseDatabase.getInstance().getReference("User/Donor/" +
                country + "/" + "All" + "/" + bloodGroup);




        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                donorModelList.clear();

                for (DataSnapshot donor : dataSnapshot.getChildren()){

                    DonorModel donorModel = donor.getValue(DonorModel.class);
                    donorModelList.add(donorModel);
                }


                if (donorModelList.size() >0){

                    messageTV.setVisibility(View.VISIBLE);
                    messageTV.setBackgroundColor(getResources().getColor(R.color.colorYellow));
                    adapter = new DonorAdapter(BloodDonorListActivity.this, donorModelList, recyclerView, adapter, BloodDonorListActivity.this);
                    RecyclerView.LayoutManager layoutManagerBeforeMeal = new GridLayoutManager(BloodDonorListActivity.this, 1);
                    recyclerView.setLayoutManager(layoutManagerBeforeMeal);
                    recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(2), true));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    progressDialog.dismiss();

                }else {

                    messageTV.setVisibility(View.VISIBLE);
                    messageTV.setText("Sorry, we have no " + bloodGroup + " donor available in " + country);
                    messageTV.setBackgroundColor(getResources().getColor(R.color.colorRed));
                    progressDialog.dismiss();



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
}
