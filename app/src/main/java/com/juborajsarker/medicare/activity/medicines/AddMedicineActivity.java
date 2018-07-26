package com.juborajsarker.medicare.activity.medicines;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.fragment.AddMedicineFragment;

public class AddMedicineActivity extends AppCompatActivity {

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_home_footer_1));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("93448558CC721EBAD8FAAE5DA52596D3").build();
        mAdView.loadAd(adRequest);


        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, new AddMedicineFragment()).commit();
        setTitle("Add New Medicine");


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
