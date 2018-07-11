package com.juborajsarker.medicare.activity.blood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.activity.user.UserActivity;
import com.juborajsarker.medicare.adapter.DonorAdapter;
import com.juborajsarker.medicare.model.DonorModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BloodActivity extends AppCompatActivity {

    boolean isLoggedIn;
    SharedPreferences sharedPreferences;

    TextView messageTV;
    LinearLayout bloodLAYOUT;
    RecyclerView recyclerView;

    EditText cityET;
    Button searchBTN;
    Spinner bgSP;

    String city, country, bloodGroup;

    DatabaseReference databaseReference;
    List<DonorModel> donorModelList;
    DonorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }



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
        donorModelList = new ArrayList<>();



        if (!isLoggedIn) {

            messageTV.setVisibility(View.VISIBLE);
            bloodLAYOUT.setVisibility(View.GONE);

        } else {

            messageTV.setVisibility(View.GONE);
            bloodLAYOUT.setVisibility(View.VISIBLE);


        }


        messageTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(BloodActivity.this, UserActivity.class));

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

                    Toast.makeText(BloodActivity.this, "Please select a valid blood group", Toast.LENGTH_SHORT).show();

                }else {


                    city = cityET.getText().toString();
                    country = getUserCountry(BloodActivity.this);
                    bloodGroup = getBloodGroup();

                    Intent intent = new Intent(BloodActivity.this, BloodDonorActivity.class);
                    intent.putExtra("city", city);
                    intent.putExtra("bloodGroup", bloodGroup);
                    intent.putExtra("country", country);
                    startActivity(intent);



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
