package com.juborajsarker.medicinealert.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.juborajsarker.medicinealert.R;
import com.juborajsarker.medicinealert.database.DatabaseHelper;
import com.juborajsarker.medicinealert.dataparser.DateCalculations;
import com.juborajsarker.medicinealert.dataparser.ImageSaver;
import com.juborajsarker.medicinealert.model.MedicineModel;
import com.juborajsarker.medicinealert.model.StaticVariables;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class AddMedicineFragment extends Fragment {

    EditText medNameET, noOfDaysET;
    TextView firstSlotTV, secondSlotTV, thirdSlotTV, startDateTV;
    Spinner noOfTimesSP;
    RadioButton everyDayRB, specificDayRB, daysIntervalRB;
    CheckBox cbSaturday, cbSunday, cbMonday, cbTuesday, cbWednesday, cbThursday, cbFriday;
    LinearLayout firstSlotLAYOUT, secondSlotLAYOUT, thirdSlotLAYOUT;
    EditText etDaysInterval;
    ImageView plusIV, mynasIV, takeSnapIV, medicineIV;
    CardView cvSpecificDayOfWeek, cvDaysInterval, cvMedicineImage;
    Button setBTN, retakeBTN, cancelBTN;

    String formattedTime;
    Calendar myCalender;

    int id, numberOfSlot, noOfDays, daysInterval;
    String medName, imagePath, firstSlotTime, secondSlotTime, thirdSlotTime, startDate, daysNameOfWeek, status, calculatedDate, newStartDate;
    boolean isEveryday, isSpecificDaysOfWeek, isDaysInterval;
    boolean sat, sun, mon, tue, wed, thu, fri;
    boolean allPermission;

    DatabaseHelper dbHelper;

    View view;


    public AddMedicineFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_add_medicine, container, false);


        init();
        setOnClick();


        if (Build.VERSION.SDK_INT >= 23) {
            checkMultiplePermissions();
        }


//        ImageSaver imageSaver = new ImageSaver(getContext(), getActivity());
//        cvMedicineImage.setVisibility(View.VISIBLE);
//        imageSaver.loadImage("HHHHHH (Thu May 10 02:15:12 GMT+06:00 2018)", medicineIV);





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
        takeSnapIV = (ImageView) view.findViewById(R.id.iv_take_snap);
        medicineIV = (ImageView) view.findViewById(R.id.medicine_IV);

        cvSpecificDayOfWeek = (CardView) view.findViewById(R.id.cv_specific_day_of_week);
        cvDaysInterval = (CardView) view.findViewById(R.id.cv_days_interval);
        cvMedicineImage = (CardView) view.findViewById(R.id.medicine_image_cv);

        retakeBTN = (Button) view.findViewById(R.id.retakeBTN);
        cancelBTN = (Button) view.findViewById(R.id.cancelBTN);
        setBTN = (Button) view.findViewById(R.id.set_BTN);


        everyDayRB.setChecked(true);

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


        plusIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int daysInterval = Integer.parseInt(etDaysInterval.getText().toString());
                daysInterval = daysInterval + 1;
                etDaysInterval.setText(String.valueOf(daysInterval));

            }
        });


        mynasIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int daysInterval = Integer.parseInt(etDaysInterval.getText().toString());
                daysInterval = daysInterval - 1;

                if (daysInterval <= 1){

                    etDaysInterval.setText("1");

                }else {

                    etDaysInterval.setText(String.valueOf(daysInterval));
                }
            }
        });



        takeSnapIV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {


                takePhoto();

            }
        });





        retakeBTN.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                takePhoto();

            }
        });


        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cvMedicineImage.setVisibility(View.GONE);
            }
        });



        setBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (allPermission){



                    if (checkValidity() && checkSpecificDayValidity()){

                        if (everyDayRB.isChecked()){


                            String numberOfDays = noOfDaysET.getText().toString();
                            DateCalculations dc = new DateCalculations();

                            id = 0;
                            medName = medNameET.getText().toString();
                            numberOfSlot = noOfTimesSP.getSelectedItemPosition() + 1;
                            noOfDays = Integer.parseInt(numberOfDays);
                            isEveryday = true;
                            isSpecificDaysOfWeek = false;
                            isDaysInterval = false;
                            daysNameOfWeek = "null";
                            daysInterval = 0;
                            startDate = startDateTV.getText().toString();
                            newStartDate = startDate;
                            status = "not_taken";


                            dbHelper = new DatabaseHelper(getContext());

                            saveImageToDirectory();
                            getSlotTime();







                            for (int i = 0; i < noOfDays; i++){

                                calculatedDate = dc.addDays(newStartDate, "1");
                                MedicineModel medicineModel = new MedicineModel();
                                medicineModel.setId(id);
                                medicineModel.setDate(calculatedDate);
                                medicineModel.setMedicineName(medName);
                                medicineModel.setImagePath(imagePath);
                                medicineModel.setNumberOfSlot(numberOfSlot);
                                medicineModel.setFirstSlotTime(firstSlotTime);
                                medicineModel.setSecondSlotTime(secondSlotTime);
                                medicineModel.setThirdSlotTime(thirdSlotTime);
                                medicineModel.setNumberOfDays(noOfDays);
                                medicineModel.setEveryday(isEveryday);
                                medicineModel.setSpecificDaysOfWeek(isSpecificDaysOfWeek);
                                medicineModel.setDaysInterval(isDaysInterval);
                                medicineModel.setDaysNameOfWeek(daysNameOfWeek);
                                medicineModel.setDaysInterval(daysInterval);
                                medicineModel.setStartDate(startDate);
                                medicineModel.setStatus(status);

                                dbHelper.insertData(medicineModel);

                                newStartDate = calculatedDate;



                            }




                        }else if (specificDayRB.isChecked()){

                            String numberOfDays = noOfDaysET.getText().toString();
                            DateCalculations dc = new DateCalculations();

                            id = 0;
                            medName = medNameET.getText().toString();
                            imagePath = "";
                            numberOfSlot = noOfTimesSP.getSelectedItemPosition() + 1;
                            noOfDays = Integer.parseInt(numberOfDays);
                            isEveryday = false;
                            isSpecificDaysOfWeek = true;
                            isDaysInterval = false;
                            daysNameOfWeek = getDaysNameOfWeek();
                            daysInterval = 0;
                            startDate = startDateTV.getText().toString();
                            newStartDate = startDate;
                            status = "not_taken";


                            dbHelper = new DatabaseHelper(getContext());



                            saveImageToDirectory();
                            getSlotTime();


                            for (int i = 0; i < noOfDays; i++){



                                calculatedDate = dc.addDays(newStartDate, "1");
                                String singleDayName = dc.daysNameOfWeek(calculatedDate);

                                if (daysNameOfWeek.contains(singleDayName)){

                                    String finalDate = calculatedDate;
                                    MedicineModel medicineModel = new MedicineModel();
                                    medicineModel.setId(id);
                                    medicineModel.setDate(finalDate);
                                    medicineModel.setMedicineName(medName);
                                    medicineModel.setImagePath(imagePath);
                                    medicineModel.setNumberOfSlot(numberOfSlot);
                                    medicineModel.setFirstSlotTime(firstSlotTime);
                                    medicineModel.setSecondSlotTime(secondSlotTime);
                                    medicineModel.setThirdSlotTime(thirdSlotTime);
                                    medicineModel.setNumberOfDays(noOfDays);
                                    medicineModel.setEveryday(isEveryday);
                                    medicineModel.setSpecificDaysOfWeek(isSpecificDaysOfWeek);
                                    medicineModel.setDaysInterval(isDaysInterval);
                                    medicineModel.setDaysNameOfWeek(daysNameOfWeek);
                                    medicineModel.setDaysInterval(daysInterval);
                                    medicineModel.setStartDate(startDate);
                                    medicineModel.setStatus(status);

                                    dbHelper.insertData(medicineModel);

                                    newStartDate = calculatedDate;


                                }else {

                                    newStartDate = calculatedDate;
                                    i--;


                                }


                            }






                        }else if (daysIntervalRB.isChecked()){



                            String numberOfDays = noOfDaysET.getText().toString();
                            DateCalculations dc = new DateCalculations();

                            id = 0;
                            medName = medNameET.getText().toString();
                            imagePath = "";
                            numberOfSlot = noOfTimesSP.getSelectedItemPosition() + 1;
                            noOfDays = Integer.parseInt(numberOfDays);
                            isEveryday = false;
                            isSpecificDaysOfWeek = false;
                            isDaysInterval = true;
                            daysNameOfWeek = "null";
                            daysInterval = Integer.parseInt(etDaysInterval.getText().toString()) + 1;
                            startDate = startDateTV.getText().toString();
                            newStartDate = startDate;
                            status = "not_taken";


                            dbHelper = new DatabaseHelper(getContext());



                            saveImageToDirectory();
                            getSlotTime();



                            for (int i = 0; i < noOfDays; i++){

                                calculatedDate = dc.addDays(newStartDate, String.valueOf(daysInterval));
                                MedicineModel medicineModel = new MedicineModel();
                                medicineModel.setId(id);
                                medicineModel.setDate(calculatedDate);
                                medicineModel.setMedicineName(medName);
                                medicineModel.setImagePath(imagePath);
                                medicineModel.setNumberOfSlot(numberOfSlot);
                                medicineModel.setFirstSlotTime(firstSlotTime);
                                medicineModel.setSecondSlotTime(secondSlotTime);
                                medicineModel.setThirdSlotTime(thirdSlotTime);
                                medicineModel.setNumberOfDays(noOfDays);
                                medicineModel.setEveryday(isEveryday);
                                medicineModel.setSpecificDaysOfWeek(isSpecificDaysOfWeek);
                                medicineModel.setDaysInterval(isDaysInterval);
                                medicineModel.setDaysNameOfWeek(daysNameOfWeek);
                                medicineModel.setDaysInterval(daysInterval);
                                medicineModel.setStartDate(startDate);
                                medicineModel.setStatus(status);

                                dbHelper.insertData(medicineModel);

                                newStartDate = calculatedDate;



                            }




                        }

                    }else {

                        // Toast.makeText(getContext(), "Please check all fields has been filled up", Toast.LENGTH_SHORT).show();
                    }


                }else {

                    checkMultiplePermissions();
                }







            }
        });

    }

    private void getSlotTime() {

        if (noOfTimesSP.getSelectedItemPosition() == 0){

            firstSlotTime = firstSlotTV.getText().toString();
            secondSlotTime = "null";
            thirdSlotTime = "null";

        }else if (noOfTimesSP.getSelectedItemPosition() == 1){

            firstSlotTime = firstSlotTV.getText().toString();
            secondSlotTime = secondSlotTV.getText().toString();
            thirdSlotTime = "null";

        }else if (noOfTimesSP.getSelectedItemPosition() == 2){

            firstSlotTime = firstSlotTV.getText().toString();
            secondSlotTime = secondSlotTV.getText().toString();
            thirdSlotTime = thirdSlotTV.getText().toString();

        }
    }

    private void saveImageToDirectory() {

        if (cvMedicineImage.getVisibility() == View.VISIBLE){

            imagePath = medName.toUpperCase() + "(" +  Calendar.getInstance().getTime() + ")";
            BitmapDrawable drawable = (BitmapDrawable) medicineIV.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            ImageSaver imageSaver = new ImageSaver(getContext(), getActivity());
            imageSaver.saveImage(bitmap, imagePath);

        }else {

            imagePath = "null";
        }
    }

    private boolean checkValidity() {


        if (medNameET.getText().toString().equals("")){

            medNameET.setError("Enter a medicine name");
            return false;


        }else if ( (firstSlotLAYOUT.getVisibility() == View.VISIBLE &&
                secondSlotLAYOUT.getVisibility() == View.VISIBLE &&
                thirdSlotLAYOUT.getVisibility() == View.VISIBLE )
                        &&
                ((firstSlotTV.getText().toString().contains("Set"))
                || secondSlotTV.getText().toString().contains("Set")
                || thirdSlotTV.getText().toString().contains("Set"))){


            if (firstSlotTV.getText().toString().contains("Set")){

                Toast.makeText(getContext(), "Please enter a valid time in slot 1", Toast.LENGTH_SHORT).show();

            }else if (secondSlotTV.getText().toString().contains("Set")){

                Toast.makeText(getContext(), "Please enter a valid time in slot 2", Toast.LENGTH_SHORT).show();

            }else if (thirdSlotTV.getText().toString().contains("Set")){

                Toast.makeText(getContext(), "Please enter a valid time in slot 3", Toast.LENGTH_SHORT).show();
            }


            return false;


        }else if (( (firstSlotLAYOUT.getVisibility() == View.VISIBLE &&
                secondSlotLAYOUT.getVisibility() == View.VISIBLE &&
                thirdSlotLAYOUT.getVisibility() == View.GONE ))
                &&
                ((firstSlotTV.getText().toString().contains("Set"))
                || secondSlotTV.getText().toString().contains("Set"))){


            if (firstSlotTV.getText().toString().contains("Set")){

                Toast.makeText(getContext(), "Please enter a valid time in slot 1", Toast.LENGTH_SHORT).show();

            }else if (secondSlotTV.getText().toString().contains("Set")){

                Toast.makeText(getContext(), "Please enter a valid time in slot 2", Toast.LENGTH_SHORT).show();
            }


            return false;


        }else if ( ((firstSlotLAYOUT.getVisibility() == View.VISIBLE &&
                secondSlotLAYOUT.getVisibility() == View.GONE &&
                thirdSlotLAYOUT.getVisibility() == View.GONE ))
                &&
                ((firstSlotTV.getText().toString().contains("Set")))){


            Toast.makeText(getContext(), "Please enter a valid time in slot 1", Toast.LENGTH_SHORT).show();
            return false;


        }else if (noOfDaysET.getText().toString().equals("")){


            noOfDaysET.setError("this field is required");
            return false;


        }else if (startDateTV.getText().toString().contains("Touch here to set date")){

            Toast.makeText(getContext(), "Please enter a valid date", Toast.LENGTH_SHORT).show();

            return false;

        }else {

            return true;
        }


    }

    public boolean checkSpecificDayValidity(){

        if (cvSpecificDayOfWeek.getVisibility() == View.VISIBLE && cbSaturday.isChecked()){

            return true;

        }else if (cvSpecificDayOfWeek.getVisibility() == View.VISIBLE && cbSunday.isChecked()){

            return true;

        }else if (cvSpecificDayOfWeek.getVisibility() == View.VISIBLE && cbMonday.isChecked()){

            return true;

        }else if (cvSpecificDayOfWeek.getVisibility() == View.VISIBLE && cbTuesday.isChecked()){

            return true;

        }else if (cvSpecificDayOfWeek.getVisibility() == View.VISIBLE && cbWednesday.isChecked()){

            return true;

        }else if (cvSpecificDayOfWeek.getVisibility() == View.VISIBLE && cbThursday.isChecked()){

            return true;

        }else if (cvSpecificDayOfWeek.getVisibility() == View.VISIBLE && cbFriday.isChecked()){

            return true;

        }else if (cvSpecificDayOfWeek.getVisibility() == View.GONE){

            return true;

        }else {

            Toast.makeText(getContext(), "Please select at least one specific day in week\nOr choose Everyday", Toast.LENGTH_LONG).show();
            return false;
        }
    }


    private void takePhoto() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, StaticVariables.MY_CAMERA_PERMISSION_CODE);

            } else {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, StaticVariables.CAMERA_REQUEST);
            }

        }else {

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, StaticVariables.CAMERA_REQUEST);
        }
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

       try {

           timePickerDialog.setTitle(message);
           timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
           timePickerDialog.show();

       }catch (Exception e){

           Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
       }



    }

    public void showDatePicker() {



        DialogFragment dFragment = new DatePickerFragment();
        dFragment.show(getActivity().getFragmentManager(), "Date Picker");

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == StaticVariables.MY_CAMERA_PERMISSION_CODE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
//                Intent cameraIntent = new
//                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, StaticVariables.CAMERA_REQUEST);
//            } else {
//                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
//            }
//
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == StaticVariables.CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
              //  Bitmap bMapScaled = Bitmap.createScaledBitmap(photo, 330, 189, true);
                cvMedicineImage.setVisibility(View.VISIBLE);
                medicineIV.setImageBitmap(photo);
            }
        }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT,this,year,month,day);
            return  dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day){

            TextView tv = (TextView) getActivity().findViewById(R.id.start_date_TV);


            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);
            Date chosenDate = cal.getTime();

            DateFormat df_medium_uk = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
            String df_medium_uk_str = df_medium_uk.format(chosenDate);
            tv.setText(df_medium_uk_str );


        }
    }

    public String getDaysNameOfWeek(){

        String daysName = "";

        if (cbSaturday.isChecked()){

            daysName = daysName + "Saturday, ";
            sat = true;

        }if (cbSunday.isChecked()){

            daysName = daysName + "Sunday, ";
            sun = true;

        }if (cbMonday.isChecked()){

            daysName = daysName + "Monday, ";
            mon = true;

        }if (cbTuesday.isChecked()){

            daysName = daysName + "Tuesday, ";
            tue = true;

        }if (cbWednesday.isChecked()){

            daysName = daysName + "Wednesday, ";
            wed = true;

        }if (cbThursday.isChecked()){

            daysName = daysName + "Thursday, ";
            thu = true;

        }if (cbFriday.isChecked()){

            daysName = daysName + "Friday, ";
            fri = true;

        }


        return daysName.substring(0, daysName.length()-2);
    }






    private void checkMultiplePermissions() {

        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            List<String> permissionsList = new ArrayList<String>();

            if (!addPermission(permissionsList, Manifest.permission.CAMERA)) {
                permissionsNeeded.add("Camera");
            }

            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionsNeeded.add("Storage");
            }

            if (permissionsList.size() > 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        StaticVariables.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
        }
    }


    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23)

            if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);

                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case StaticVariables.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                    allPermission = true;

                    return;
                } else {
                    // Permission Denied
                    if (Build.VERSION.SDK_INT >= 23) {
                        Toast.makeText(
                                getContext(),
                                "My App cannot run without Camera and Storage " +
                                        "Permissions.\nRelaunch My App or allow permissions" +
                                        " in Applications Settings",
                                Toast.LENGTH_LONG).show();

                        allPermission = false;


                        if (perms.get(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){


                        }


                        if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){


                        }

                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    }






