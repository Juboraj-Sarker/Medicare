package com.juborajsarker.medicare.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.activity.blood.BloodActivity;
import com.juborajsarker.medicare.activity.diary.DiaryActivity;
import com.juborajsarker.medicare.activity.doctors.DoctorsActivity;
import com.juborajsarker.medicare.activity.more.AmbulanceActivity;
import com.juborajsarker.medicare.activity.more.HelpActivity;
import com.juborajsarker.medicare.activity.more.NearestHospitalActivity;
import com.juborajsarker.medicare.activity.more.NearestPharmacyActivity;
import com.juborajsarker.medicare.activity.more.SettingsActivity;
import com.juborajsarker.medicare.activity.more.TermsActivity;


public class MoreFragment extends Fragment {

    View view;

    LinearLayout doctorLAYOUT, searchBloodLAYOUT, diaryLAYOUT, nearestHospitalLAYOUT,
            nearestPharmacyLAYOUT, ambulanceLAYOUT, settingsLAYOUT, helpLAYOUT, termsLAYOUT;


    public MoreFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_more, container, false);

        init();
        setOnClick();




        return view;
    }

    private void init() {

        doctorLAYOUT = (LinearLayout) view.findViewById(R.id.doctor_LAYOUT);
        searchBloodLAYOUT = (LinearLayout) view.findViewById(R.id.search_blood_LAYOUT);
        diaryLAYOUT = (LinearLayout) view.findViewById(R.id.diary_LAYOUT);
        nearestHospitalLAYOUT = (LinearLayout) view.findViewById(R.id.nearest_hospital_LAYOUT);
        nearestPharmacyLAYOUT = (LinearLayout) view.findViewById(R.id.nearest_pharmacy_LAYOUT);
        ambulanceLAYOUT = (LinearLayout) view.findViewById(R.id.ambulance_LAYOUT);
        settingsLAYOUT = (LinearLayout) view.findViewById(R.id.settings_LAYOUT);
        helpLAYOUT = (LinearLayout) view.findViewById(R.id.help_and_support_LAYOUT);
        termsLAYOUT = (LinearLayout) view.findViewById(R.id.terms_LAYOUT);


    }

    private void setOnClick(){

        doctorLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getContext(), DoctorsActivity.class));

            }
        });


        searchBloodLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), BloodActivity.class));

            }
        });


        diaryLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), DiaryActivity.class));
            }
        });


        nearestHospitalLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), NearestHospitalActivity.class));

            }
        });


        nearestPharmacyLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getContext(), NearestPharmacyActivity.class));
            }
        });

        ambulanceLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), AmbulanceActivity.class));
            }
        });


        settingsLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getContext(), SettingsActivity.class));
            }
        });


        helpLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), HelpActivity.class));

            }
        });


        termsLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), TermsActivity.class));

            }
        });



    }

}
