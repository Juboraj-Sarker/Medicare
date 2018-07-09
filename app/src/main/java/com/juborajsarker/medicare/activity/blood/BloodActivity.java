package com.juborajsarker.medicare.activity.blood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.activity.user.UserActivity;

import org.apache.commons.lang3.StringUtils;

public class BloodActivity extends AppCompatActivity {

    boolean isLoggedIn;
    SharedPreferences sharedPreferences;

    TextView messageTV;
    LinearLayout bloodLAYOUT;

    EditText cityET;
    Button searchBTN;
    Spinner bgSP;

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


                if (!StringUtils.isBlank(cityET.getText().toString()) && bgSP.getSelectedItemPosition() > 0){

                    Toast.makeText(BloodActivity.this, "Success", Toast.LENGTH_SHORT).show();


                }else {


                    
                }

            }
        });

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
