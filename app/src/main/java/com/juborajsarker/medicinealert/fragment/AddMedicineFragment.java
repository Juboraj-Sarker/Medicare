package com.juborajsarker.medicinealert.fragment;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.juborajsarker.medicinealert.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AddMedicineFragment extends Fragment {

    EditText medNameET, noOfDaysET;
    TextView firstSlotTV, secondSlotTV, thirdSlotTV, startDateTV;
    Spinner noOfTimesSP;
    RadioButton everyDayRB, specificDayRB, daysIntervalRB;
    CheckBox cbSaturday, cbSunday, cbMonday, cbTuesday, cbWednesday, cbThursday, cbFriday;
    LinearLayout firstSlotLAYOUT, secondSlotLAYOUT, thirdSlotLAYOUT;
    EditText etDaysInterval;
    ImageView plusIV, mynasIV;
    CardView cvSpecificDayOfWeek, cvDaysInterval;
    Button setBTN;

    String formattedTime;
    int mYear;
    int mMonth;
    int mDay;
    Calendar myCalender;

    View view;


    public AddMedicineFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_add_medicine, container, false);


        init();
        setOnClick();



        return view;
    }



    private void init() {

        medNameET = (EditText) view.findViewById(R.id.medicine_name_ET);
        noOfDaysET = (EditText) view.findViewById(R.id.no_of_days_ET);

        firstSlotTV = (TextView) view.findViewById(R.id.first_slot_TV);
        secondSlotTV = (TextView) view.findViewById(R.id.second_slot_TV);
        thirdSlotTV = (TextView) view.findViewById(R.id.third_slot_TV);
        startDateTV = (TextView) view.findViewById(R.id.start_date_TV);

        noOfTimesSP = (Spinner) view.findViewById(R.id.no_of_times_SP);
        noOfTimesSP.setSelection(2);

        everyDayRB = (RadioButton) view.findViewById(R.id.everyday_RB);
        specificDayRB = (RadioButton) view.findViewById(R.id.specific_day_RB);
        daysIntervalRB = (RadioButton) view.findViewById(R.id.days_interval_RB);

        firstSlotLAYOUT = (LinearLayout) view.findViewById(R.id.first_slot_LAYOUT);
        secondSlotLAYOUT = (LinearLayout) view.findViewById(R.id.second_slot_LAYOUT);
        thirdSlotLAYOUT = (LinearLayout) view.findViewById(R.id.third_slot_layout);

        cbSaturday = (CheckBox) view.findViewById(R.id.cb_saturday);
        cbSunday = (CheckBox) view.findViewById(R.id.cb_sunday);
        cbMonday = (CheckBox) view.findViewById(R.id.cb_monday);
        cbTuesday = (CheckBox) view.findViewById(R.id.cb_tuesday);
        cbWednesday = (CheckBox) view.findViewById(R.id.cb_wednesday);
        cbThursday = (CheckBox) view.findViewById(R.id.cb_thursday);
        cbFriday = (CheckBox) view.findViewById(R.id.cb_friday);

        etDaysInterval = (EditText) view.findViewById(R.id.et_days_interval);

        plusIV = (ImageView) view.findViewById(R.id.iv_plus);
        mynasIV = (ImageView) view.findViewById(R.id.iv_mynas);

        cvSpecificDayOfWeek = (CardView) view.findViewById(R.id.cv_specific_day_of_week);
        cvDaysInterval = (CardView) view.findViewById(R.id.cv_days_interval);

        setBTN = (Button) view.findViewById(R.id.set_BTN);

    }



    private void setOnClick() {


        noOfTimesSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){

                    secondSlotLAYOUT.setVisibility(View.GONE);
                    thirdSlotLAYOUT.setVisibility(View.GONE);

                }else if (position == 1){

                    secondSlotLAYOUT.setVisibility(View.VISIBLE);
                    thirdSlotLAYOUT.setVisibility(View.GONE);

                }else if (position == 2){

                    secondSlotLAYOUT.setVisibility(View.VISIBLE);
                    thirdSlotLAYOUT.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        firstSlotLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 showHourPicker("Select first slot", 1);

            }
        });


        secondSlotLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showHourPicker("Select second slot", 2);
            }
        });



        thirdSlotLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showHourPicker("Select third slot", 3);
            }
        });



        startDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePicker();
            }
        });


        everyDayRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                    cvSpecificDayOfWeek.setVisibility(View.GONE);
                    cvDaysInterval.setVisibility(View.GONE);
                }
            }
        });


        specificDayRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                   cvSpecificDayOfWeek.setVisibility(View.VISIBLE);
                   cvDaysInterval.setVisibility(View.GONE);
                }

            }
        });


        daysIntervalRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                    cvSpecificDayOfWeek.setVisibility(View.GONE);
                    cvDaysInterval.setVisibility(View.VISIBLE);
                }
            }
        });


    }




    public void showHourPicker(String message, final int number) {


         myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);


        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalender.set(Calendar.MINUTE, minute);


                    try {
                        SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a");

                        String strTime = myCalender.get(Calendar.HOUR_OF_DAY) + ":" + myCalender.get(Calendar.MINUTE);
                        Date time = sdf24.parse(strTime);
                        formattedTime = sdf12.format(time);

                        if (number == 1){

                            firstSlotTV.setText(formattedTime);

                        }else if (number == 2){

                            secondSlotTV.setText(formattedTime);

                        }else if (number == 3){

                            thirdSlotTV.setText(formattedTime);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                myTimeListener,
                hour,
                minute,
                false);

        timePickerDialog.setTitle(message);
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();




    }








    public void showDatePicker() {



    }






}
