package com.juborajsarker.medicare.fragment;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.activity.blood.BloodSearchActivity;
import com.juborajsarker.medicare.activity.diary.DiaryActivity;
import com.juborajsarker.medicare.activity.doctors.DoctorsActivity;
import com.juborajsarker.medicare.activity.more.AmbulanceActivity;
import com.juborajsarker.medicare.activity.more.HelpActivity;
import com.juborajsarker.medicare.activity.more.NearestHospitalActivity;
import com.juborajsarker.medicare.activity.more.NearestPharmacyActivity;
import com.juborajsarker.medicare.activity.more.PrivacyActivity;
import com.juborajsarker.medicare.activity.more.TermsActivity;

import java.util.Locale;


public class MoreFragment extends Fragment {

    View view;

    LinearLayout doctorLAYOUT, searchBloodLAYOUT, diaryLAYOUT, nearestHospitalLAYOUT,
            nearestPharmacyLAYOUT, ambulanceLAYOUT, removeAdLAYOUT, privacyLAYOUT, helpLAYOUT, termsLAYOUT;


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
        removeAdLAYOUT = (LinearLayout) view.findViewById(R.id.remove_ad_LAYOUT);
        privacyLAYOUT = (LinearLayout) view.findViewById(R.id.privacy_LAYOUT);
        helpLAYOUT = (LinearLayout) view.findViewById(R.id.help_and_support_LAYOUT);
        termsLAYOUT = (LinearLayout) view.findViewById(R.id.terms_LAYOUT);

        try {
            if (getUserCountry(getContext()).equals("Bangladesh")){

                removeAdLAYOUT.setVisibility(View.GONE);

            }else {

                removeAdLAYOUT.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){

            Toast.makeText(getContext(), "Error found", Toast.LENGTH_SHORT).show();
        }


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

                startActivity(new Intent(getContext(), BloodSearchActivity.class));

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


        privacyLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getContext(), PrivacyActivity.class));
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

        removeAdLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeAds();
            }
        });



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

}
