package com.juborajsarker.medicinealert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juborajsarker.medicinealert.R;
import com.juborajsarker.medicinealert.dataparser.ImageSaver;

public class DetailsActivity extends AppCompatActivity {

    TextView medNameTV, dateTimeTV, numberOfSlotTV,  firstSlotTV, secondSlotTV,
            thirdSlotTV, numberOfDaysTV,  startDateTV, daysIntervalTV, statusTV;
    ImageView medicineIV, optionIV;
    LinearLayout statusLAYOUT, secondSlotLAYOUT, thirdSlotLAYOUT;
    String medicineName, dateTime, numberOfSlot,  firstSlotTime, secondSlotTime,
            thirdSlotTime, numberOfDays,  startDate, daysInterval, status, imagePath, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        init();
        receiveIntent();
        setTextAndIMageIntoView();
        setOnClick();
    }

    private void init() {

        medNameTV = (TextView) findViewById(R.id.medicine_name_TV_DA);
        dateTimeTV = (TextView) findViewById(R.id.date_and_time_TV_DA);
        numberOfSlotTV = (TextView) findViewById(R.id.number_of_slot_TV_DA);
        firstSlotTV = (TextView) findViewById(R.id.first_slot_TV_DA);
        secondSlotTV = (TextView) findViewById(R.id.second_slot_TV_DA);
        thirdSlotTV = (TextView) findViewById(R.id.third_slot_TV_DA);
        numberOfDaysTV = (TextView) findViewById(R.id.number_of_days_TV_DA);
        startDateTV = (TextView) findViewById(R.id.start_date_TV_DA);
        daysIntervalTV = (TextView) findViewById(R.id.days_interval_TV_DA);
        statusTV = (TextView) findViewById(R.id.status_TV_DA);

        medicineIV = (ImageView) findViewById(R.id.medicine_IV_DA);
        optionIV = (ImageView) findViewById(R.id.option_IV_DA);

        statusLAYOUT = (LinearLayout) findViewById(R.id.status_LAYOUT_DA);
        secondSlotLAYOUT = (LinearLayout) findViewById(R.id.second_slot_LAYOUT_DA);
        thirdSlotLAYOUT = (LinearLayout) findViewById(R.id.third_slot_layout_DA);
    }

    private void receiveIntent() {

        Intent intent = getIntent();
        medicineName = intent.getStringExtra("medName");
        dateTime = intent.getStringExtra("dateTime");
        numberOfSlot = intent.getStringExtra("numberOfSlot");
        firstSlotTime = intent.getStringExtra("firstSlotTime");
        secondSlotTime = intent.getStringExtra("secondSlotTime");
        thirdSlotTime = intent.getStringExtra("thirdSlotTime");
        numberOfDays = intent.getStringExtra("numberOfDays");
        startDate = intent.getStringExtra("startDate");
        daysInterval = intent.getStringExtra("daysInterval");
        status = intent.getStringExtra("status");
        imagePath = intent.getStringExtra("imagePath");
        type = intent.getStringExtra("type");


    }

    private void setTextAndIMageIntoView() {

        medNameTV.setText(medicineName + " (" + type + ")");
        dateTimeTV.setText(dateTime);
        numberOfSlotTV.setText(numberOfSlot);
        numberOfDaysTV.setText(numberOfDays);
        firstSlotTV.setText(firstSlotTime);

        if (secondSlotTime.equals("null")){

            secondSlotLAYOUT.setVisibility(View.GONE);

        }else {

            secondSlotTV.setText(secondSlotTime);
        }


        if (thirdSlotTime.equals("null")){

            thirdSlotLAYOUT.setVisibility(View.GONE);

        }else {

            thirdSlotTV.setText(thirdSlotTime);
        }


        startDateTV.setText(startDate);
        daysIntervalTV.setText(daysInterval);
        statusTV.setText(status);

        ImageSaver imageSaver = new ImageSaver(DetailsActivity.this, this);
        imageSaver.loadImage(imagePath, medicineIV, type);

    }

    public void setOnClick(){

        optionIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }
}
