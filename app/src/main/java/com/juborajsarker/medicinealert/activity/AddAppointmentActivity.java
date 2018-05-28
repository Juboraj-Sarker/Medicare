package com.juborajsarker.medicinealert.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.juborajsarker.medicinealert.R;

public class AddAppointmentActivity extends AppCompatActivity {

    EditText appTitleET, doctorNameET, doctorSpecialityET, rememberBeforeET, locationET, notesET;
    TextView dateTV, timeTV;
    RadioButton hourRB, minuteRB;
    Button addAppointmentBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){

            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        init();
        setOnclick();

    }

    private void init() {

        appTitleET = (EditText) findViewById(R.id.appointment_title_ET);
        doctorNameET = (EditText) findViewById(R.id.doctor_name_ET);
        doctorSpecialityET = (EditText) findViewById(R.id.doctor_speciality_ET);
        rememberBeforeET = (EditText) findViewById(R.id.remember_before_ET);
        locationET = (EditText) findViewById(R.id.location_ET);
        notesET = (EditText) findViewById(R.id.notes_ET);

        dateTV = (TextView) findViewById(R.id.date_TV);
        timeTV = (TextView) findViewById(R.id.time_TV);

        hourRB = (RadioButton) findViewById(R.id.hour_RB);
        minuteRB = (RadioButton) findViewById(R.id.minute_RB);

        addAppointmentBTN = (Button) findViewById(R.id.add_appointment_BTN);

        hourRB.setChecked(true);
    }

    private void setOnclick(){

        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        hourRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        addAppointmentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



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
