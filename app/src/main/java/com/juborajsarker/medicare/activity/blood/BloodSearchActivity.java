package com.juborajsarker.medicare.activity.blood;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.activity.user.UserActivity;

import java.util.Locale;

public class BloodSearchActivity extends AppCompatActivity {

    boolean isLoggedIn;
    SharedPreferences sharedPreferences;

    TextView messageTV;
    LinearLayout bloodLAYOUT;
    RecyclerView recyclerView;

    EditText cityET;
    Button searchBTN;
    Spinner bgSP;

    String city, country, bloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_search);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_home_footer_1));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("93448558CC721EBAD8FAAE5DA52596D3").build();
        mAdView.loadAd(adRequest);



        sharedPreferences = getSharedPreferences("registerStatus", MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        init();


    }


    private void init(){

        messageTV = (TextView) findViewById(R.id.messageTV);
        bloodLAYOUT = (LinearLayout) findViewById(R.id.blood_LAYOUT);

        cityET = (EditText) findViewById(R.id.cityET);
        bgSP = (Spinner) findViewById(R.id.blood_group_SP);
        searchBTN = (Button) findViewById(R.id.search_BTN);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);



        if (!isLoggedIn) {

            messageTV.setVisibility(View.VISIBLE);
            bloodLAYOUT.setVisibility(View.GONE);

        } else {

            messageTV.setVisibility(View.GONE);
            bloodLAYOUT.setVisibility(View.VISIBLE);


        }


        try {
            if (getUserCountry(BloodSearchActivity.this).equals("Bangladesh")){

                if (!isLoggedIn) {

                    messageTV.setVisibility(View.VISIBLE);
                    bloodLAYOUT.setVisibility(View.GONE);

                } else {

                    messageTV.setVisibility(View.GONE);
                    bloodLAYOUT.setVisibility(View.VISIBLE);


                }

            }else {


                messageTV.setVisibility(View.VISIBLE);
                bloodLAYOUT.setVisibility(View.GONE);
                messageTV.setText("You have tp buy Medicare Pro for this option !!! Tap here to buy Medicare Pro");
            }

        }catch (Exception e){

            Toast.makeText(BloodSearchActivity.this, "Error found", Toast.LENGTH_SHORT).show();
        }


        messageTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (messageTV.getText().toString().equals("You have tp buy Medicare Pro for this option !!! Tap here to buy Medicare Pro")){

                    removeAds();

                }else {

                    startActivity(new Intent(BloodSearchActivity.this, UserActivity.class));
                }



            }
        });


        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (cityET.getText().toString().length()>0 &&
                        Character.isWhitespace(cityET.getText().toString().charAt(0))){


                    cityET.setError("City Name Cannot start with space");

                }else if (cityET.getText().toString().equals("")){

                    cityET.setError("City name is required");

                }else if ( bgSP.getSelectedItemPosition() == 0){

                    Toast.makeText(BloodSearchActivity.this, "Please select a valid blood group", Toast.LENGTH_SHORT).show();

                }else {


                    if (checkConnection()){

                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


                        city = cityET.getText().toString();
                        country = getUserCountry(BloodSearchActivity.this);
                        bloodGroup = getBloodGroup();

                        Intent intent = new Intent(BloodSearchActivity.this, BloodDonorListActivity.class);
                        intent.putExtra("city", city);
                        intent.putExtra("bloodGroup", bloodGroup);
                        intent.putExtra("country", country);
                        startActivity(intent);

                    }else {

                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        Toast.makeText(BloodSearchActivity.this, "Please check your internet connection !!!", Toast.LENGTH_SHORT).show();
                    }



                }

            }
        });

    }



    public String getBloodGroup (){

        String bg = "";

        switch (bgSP.getSelectedItemPosition()){

            case 1:{

                bg = "A+";
                break;

            }case 2:{

                bg = "A-";
                break;

            }case 3:{

                bg = "B+";
                break;

            }case 4:{

                bg = "B-";
                break;

            }case 5:{

                bg = "AB+";
                break;

            }case 6:{

                bg = "AB-";
                break;

            }case 7:{

                bg = "O+";
                break;

            }case 8:{

                bg = "O-";
                break;

            }
        }

        return bg;
    }



    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available

                Locale loc = new Locale("", simCountry);
                return loc.getDisplayCountry();

            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available

                    Locale loc = new Locale("", networkCountry);
                    return loc.getDisplayCountry();
                }
            }
        }
        catch (Exception e) {

            Log.d("error", e.getMessage());
        }
        return null;
    }


    private Intent removeAdsIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, "com.juborajsarker.medicarepro" )));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }


    public void removeAds() {
        try {
            Intent rateIntent = removeAdsIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = removeAdsIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }


    public boolean checkConnection(){

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoWifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfoWifi.isConnected();
        NetworkInfo networkInfoMobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfoMobile.isConnected();

        if (isWifiConn || isMobileConn){

            return true;

        }else {


            return false;
        }
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
