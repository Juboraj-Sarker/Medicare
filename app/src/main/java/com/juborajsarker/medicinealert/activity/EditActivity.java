package com.juborajsarker.medicinealert.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
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
import com.juborajsarker.medicinealert.broadcastReceiver.AlarmReceiver;
import com.juborajsarker.medicinealert.database.AlarmDatabase;
import com.juborajsarker.medicinealert.database.DatabaseHelper;
import com.juborajsarker.medicinealert.dataparser.DateCalculations;
import com.juborajsarker.medicinealert.dataparser.ImageSaver;
import com.juborajsarker.medicinealert.fragment.AddMedicineFragment;
import com.juborajsarker.medicinealert.model.AlarmModel;
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

public class EditActivity extends AppCompatActivity {

    String tableName;
    int id;
    MedicineModel medicineModel;
    List<MedicineModel> medicineModelList;
    DatabaseHelper dbHelper;



    EditText medNameET, noOfDaysET;
    TextView firstSlotTV, secondSlotTV, thirdSlotTV, startDateTV;
    Spinner noOfTimesSP, medicineTypeSP;
    RadioButton everyDayRB, specificDayRB, daysIntervalRB, beforeMealRB, afterMealRB;
    CheckBox cbSaturday, cbSunday, cbMonday, cbTuesday, cbWednesday, cbThursday, cbFriday;
    LinearLayout firstSlotLAYOUT, secondSlotLAYOUT, thirdSlotLAYOUT;
    EditText etDaysInterval;
    ImageView plusIV, mynasIV, takeSnapIV, medicineIV;
    CardView cvSpecificDayOfWeek, cvDaysInterval, cvMedicineImage;
    Button setBTN, retakeBTN, cancelBTN;


    String beforeType, beforeStartDate, beforeDaysNameOfWeek;
    int beforeNumberOfSlot, beforeNumberOfDays, beforeDaysInterval;

    boolean allPermission;
    boolean sat, sun, mon, tue, wed, thu, fri;
    String formattedTime, imagePath;
    Calendar myCalender;
    String medName,  firstSlotTime, secondSlotTime, thirdSlotTime, startDate, daysNameOfWeek, status, calculatedDate,
            newStartDate, medicineMeal, medicineType, finalDate;


    int  numberOfSlot, noOfDays, daysInterval, uniqueCode;
    boolean isEveryday, isSpecificDaysOfWeek, isDaysInterval;
    int requestCode = 1;
    int firstRequestCode, secondRequestCode, thirdRequestCode;
    String newTableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){

            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Intent intent = getIntent();
        tableName = intent.getStringExtra("tableName");
        id = intent.getIntExtra("id",-1);

        medicineModel = new MedicineModel();
        medicineModelList = new ArrayList<>();
        dbHelper = new DatabaseHelper(this);


        if (id != -1){

            medicineModel = dbHelper.getSingleMedicine(String.valueOf(id), tableName);

        }else {

            Log.d("data", "data not found");
        }

        init();
        setData();
        setListener();
        collectData();
    }


    private void init() {

            medNameET = (EditText) findViewById(R.id.medicine_name_ET);
            noOfDaysET = (EditText) findViewById(R.id.no_of_days_ET);

            firstSlotTV = (TextView) findViewById(R.id.first_slot_TV);
            secondSlotTV = (TextView) findViewById(R.id.second_slot_TV);
            thirdSlotTV = (TextView) findViewById(R.id.third_slot_TV);
            startDateTV = (TextView) findViewById(R.id.start_date_TV);

            noOfTimesSP = (Spinner) findViewById(R.id.no_of_times_SP);
            medicineTypeSP = (Spinner) findViewById(R.id.medicine_type_SP);

            everyDayRB = (RadioButton) findViewById(R.id.everyday_RB);
            specificDayRB = (RadioButton) findViewById(R.id.specific_day_RB);
            daysIntervalRB = (RadioButton) findViewById(R.id.days_interval_RB);
            beforeMealRB = (RadioButton) findViewById(R.id.before_meal_RB);
            afterMealRB = (RadioButton)findViewById(R.id.after_meal_RB);

            firstSlotLAYOUT = (LinearLayout) findViewById(R.id.first_slot_LAYOUT);
            secondSlotLAYOUT = (LinearLayout) findViewById(R.id.second_slot_LAYOUT);
            thirdSlotLAYOUT = (LinearLayout) findViewById(R.id.third_slot_layout);

            cbSaturday = (CheckBox) findViewById(R.id.cb_saturday);
            cbSunday = (CheckBox) findViewById(R.id.cb_sunday);
            cbMonday = (CheckBox) findViewById(R.id.cb_monday);
            cbTuesday = (CheckBox) findViewById(R.id.cb_tuesday);
            cbWednesday = (CheckBox) findViewById(R.id.cb_wednesday);
            cbThursday = (CheckBox) findViewById(R.id.cb_thursday);
            cbFriday = (CheckBox) findViewById(R.id.cb_friday);

            etDaysInterval = (EditText) findViewById(R.id.et_days_interval);

            plusIV = (ImageView) findViewById(R.id.iv_plus);
            mynasIV = (ImageView) findViewById(R.id.iv_mynas);
            takeSnapIV = (ImageView)findViewById(R.id.iv_take_snap);
            medicineIV = (ImageView) findViewById(R.id.medicine_IV);

            cvSpecificDayOfWeek = (CardView) findViewById(R.id.cv_specific_day_of_week);
            cvDaysInterval = (CardView) findViewById(R.id.cv_days_interval);
            cvMedicineImage = (CardView) findViewById(R.id.medicine_image_cv);

            retakeBTN = (Button) findViewById(R.id.retakeBTN);
            cancelBTN = (Button) findViewById(R.id.cancelBTN);
            setBTN = (Button) findViewById(R.id.set_BTN);



        }

    private void setData() {

        medNameET.setText(medicineModel.getMedicineName());
        beforeType = medicineModel.getMedicineType();

        if (beforeType.equals("Tablet")){

            medicineTypeSP.setSelection(1);

        }else if (beforeType.equals("Capsule")){

            medicineTypeSP.setSelection(2);

        }else if (beforeType.equals("syrup")){

            medicineTypeSP.setSelection(3);

        }else if (beforeType.equals("Injection")){

            medicineTypeSP.setSelection(4);

        }else if (beforeType.equals("Ointment")){

            medicineTypeSP.setSelection(5);

        }else if (beforeType.equals("Eye Drop")){

            medicineTypeSP.setSelection(6);

        }



        if (medicineModel.getImagePath().equals("null")){

            cvMedicineImage.setVisibility(View.GONE);

        }else {

            cvMedicineImage.setVisibility(View.VISIBLE);
            ImageSaver imageSaver = new ImageSaver(this, this);
            imageSaver.loadImage(medicineModel.getImagePath(), medicineIV, medicineModel.getMedicineType());
        }


        beforeNumberOfSlot = medicineModel.getNumberOfSlot();
        noOfTimesSP.setSelection(beforeNumberOfSlot - 1);

        if (beforeNumberOfSlot == 1){

            firstSlotLAYOUT.setVisibility(View.VISIBLE);
            firstSlotTV.setText(medicineModel.getFirstSlotTime());
            secondSlotLAYOUT.setVisibility(View.GONE);
            thirdSlotLAYOUT.setVisibility(View.GONE);

        }else if (beforeNumberOfSlot == 2){

            firstSlotLAYOUT.setVisibility(View.VISIBLE);
            secondSlotLAYOUT.setVisibility(View.VISIBLE);
            thirdSlotLAYOUT.setVisibility(View.GONE);

            firstSlotTV.setText(medicineModel.getFirstSlotTime());
            secondSlotTV.setText(medicineModel.getSecondSlotTime());

        }else if (beforeNumberOfSlot == 3){

            firstSlotLAYOUT.setVisibility(View.VISIBLE);
            secondSlotLAYOUT.setVisibility(View.VISIBLE);
            thirdSlotLAYOUT.setVisibility(View.VISIBLE);

            firstSlotTV.setText(medicineModel.getFirstSlotTime());
            secondSlotTV.setText(medicineModel.getSecondSlotTime());
            thirdSlotTV.setText(medicineModel.getThirdSlotTime());

        }

        beforeNumberOfDays = medicineModel.getNumberOfDays();
        noOfDaysET.setText(String.valueOf(beforeNumberOfDays));

        beforeStartDate = medicineModel.getStartDate();
        startDateTV.setText(beforeStartDate);




        if ( (medicineModel.getDaysNameOfWeek().equals("null") ||
                medicineModel.getDaysNameOfWeek().equals(""))
                &&
                ( medicineModel.getDaysInterval() == 0)){

            everyDayRB.setChecked(true);

        }else if (medicineModel.getDaysInterval() > 0){

            cvDaysInterval.setVisibility(View.VISIBLE);
            cvSpecificDayOfWeek.setVisibility(View.GONE);
            daysIntervalRB.setChecked(true);
            beforeDaysInterval = medicineModel.getDaysInterval();
            etDaysInterval.setText(String.valueOf(beforeDaysInterval));


        }else if ( ! medicineModel.getDaysNameOfWeek().equals("")
                || ! medicineModel.getDaysNameOfWeek().equals("null")){

            cvSpecificDayOfWeek.setVisibility(View.VISIBLE);
            beforeDaysNameOfWeek = medicineModel.getDaysNameOfWeek();
            specificDayRB.setChecked(true);

            if (beforeDaysNameOfWeek.contains("Saturday")){

                cbSaturday.setChecked(true);
            }

            if (beforeDaysNameOfWeek.contains("Sunday")){

                cbSunday.setChecked(true);
            }

            if (beforeDaysNameOfWeek.contains("Monday")){

                cbMonday.setChecked(true);
            }

            if (beforeDaysNameOfWeek.contains("Tuesday")){

                cbTuesday.setChecked(true);
            }

            if (beforeDaysNameOfWeek.contains("Wednesday")){

                cbWednesday.setChecked(true);
            }

            if (beforeDaysNameOfWeek.contains("Thursday")){

                cbThursday.setChecked(true);
            }

            if (beforeDaysNameOfWeek.contains("Friday")){

                cbFriday.setChecked(true);
            }

        }



        if (tableName.contains("before")){

            beforeMealRB.setChecked(true);

        }else if (tableName.contains("after")){

            afterMealRB.setChecked(true);
        }



    }

    private void setListener() {

        noOfTimesSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                    secondSlotLAYOUT.setVisibility(View.GONE);
                    thirdSlotLAYOUT.setVisibility(View.GONE);

                } else if (position == 1) {

                    secondSlotLAYOUT.setVisibility(View.VISIBLE);
                    thirdSlotLAYOUT.setVisibility(View.GONE);

                } else if (position == 2) {

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

                if (isChecked) {

                    cvSpecificDayOfWeek.setVisibility(View.GONE);
                    cvDaysInterval.setVisibility(View.GONE);
                }
            }
        });


        specificDayRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    cvSpecificDayOfWeek.setVisibility(View.VISIBLE);
                    cvDaysInterval.setVisibility(View.GONE);
                }

            }
        });


        daysIntervalRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

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

                if (daysInterval <= 1) {

                    etDaysInterval.setText("1");

                } else {

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

                showEditDialog();
            }
        });
    }

    private void collectData(){

        medicineModelList = new ArrayList<>();

        String medName = medicineModel.getMedicineName();
        String startDate = medicineModel.getStartDate();
        String firstSlotTime = medicineModel.getFirstSlotTime();
        int numberOfSlot = medicineModel.getNumberOfSlot();
        int numberOfDays = medicineModel.getNumberOfDays();
        String type = medicineModel.getMedicineType();

        medicineModelList = dbHelper.selectWithMultipleQueries(startDate, medName, firstSlotTime, String.valueOf(numberOfSlot),
                String.valueOf(numberOfDays), type, tableName);

        Toast.makeText(this, ""+ medicineModelList.size(), Toast.LENGTH_SHORT).show();




    }


    private void showEditDialog() {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Select EDIT operation")
                .setMessage("EDIT ONCE--> will edit only single medicine\nEDIT ALL---> will edit all similar medicine with similar name and date")

                .setPositiveButton("EDIT ALL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        editAll();

                    }
                }).setNegativeButton("EDIT ONCE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               editOnce();

            }
        }).setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                })
                .show();
    }

    private void editAll() {


    }

    private void editOnce() {

        if (checkValidity() && checkSpecificDayValidity()){


            mustExecute();

        }


    }


    private void mustExecute() {

        if (checkValidity() && checkSpecificDayValidity()) {


            if (beforeMealRB.isChecked()) {

                newTableName = "before_table";

            } else if (afterMealRB.isChecked()) {

                newTableName = "after_table";
            }

            if (everyDayRB.isChecked()) {


                String numberOfDays = noOfDaysET.getText().toString();
                DateCalculations dc = new DateCalculations();

                id = medicineModel.getId();
                medName = medNameET.getText().toString();
                medicineType = getMedicineType();
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
                medicineMeal = getMedicineMeal();


                dbHelper = new DatabaseHelper(this);

                saveImageToDirectory();
                getSlotTime();
                calculatedDate = newStartDate;

                for (int i = 0; i < noOfDays; i++) {


                    MedicineModel medicineModel = new MedicineModel();
                    medicineModel.setId(id);
                    medicineModel.setDate(calculatedDate);
                    medicineModel.setMedicineName(medName);
                    medicineModel.setMedicineType(medicineType);
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
                    medicineModel.setMedicineMeal(medicineMeal);
                    medicineModel.setUniqueCode(uniqueCode);

                    setAlarm(calculatedDate, firstSlotTime, secondSlotTime, thirdSlotTime);
                    dbHelper.insertData(medicineModel, newTableName);


//                    uniqueCode++;
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putInt("uniqueCodeLastValue", uniqueCode);
//                    editor.commit();


                    calculatedDate = dc.addDays(newStartDate, "1");
                    newStartDate = calculatedDate;


                }


            } else if (specificDayRB.isChecked()) {

                String numberOfDays = noOfDaysET.getText().toString();
                DateCalculations dc = new DateCalculations();

                id = 0;
                medName = medNameET.getText().toString();
                medicineType = getMedicineType();
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
                medicineMeal = getMedicineMeal();


                dbHelper = new DatabaseHelper(this);


                saveImageToDirectory();
                getSlotTime();
                finalDate = startDate;


                for (int i = 0; i < noOfDays; i++) {


                    calculatedDate = dc.addDays(newStartDate, "1");
                    String singleDayName = dc.daysNameOfWeek(calculatedDate);

                    if (daysNameOfWeek.contains(singleDayName)) {

                        MedicineModel medicineModel = new MedicineModel();
                        medicineModel.setId(id);
                        medicineModel.setDate(finalDate);
                        medicineModel.setMedicineName(medName);
                        medicineModel.setMedicineType(medicineType);
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
                        medicineModel.setMedicineMeal(medicineMeal);
                        medicineModel.setUniqueCode(uniqueCode);


                        setAlarm(finalDate, firstSlotTime, secondSlotTime, thirdSlotTime);
                        dbHelper.insertData(medicineModel, newTableName);


//                        uniqueCode++;
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putInt("uniqueCodeLastValue", uniqueCode);
//                        editor.commit();

                        finalDate = calculatedDate;
                        newStartDate = calculatedDate;


                    } else {

                        newStartDate = calculatedDate;
                        i--;


                    }


                }

            } else if (daysIntervalRB.isChecked()) {

                String numberOfDays = noOfDaysET.getText().toString();
                DateCalculations dc = new DateCalculations();

                id = 0;
                medName = medNameET.getText().toString();
                medicineType = getMedicineType();
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
                medicineMeal = getMedicineMeal();

                dbHelper = new DatabaseHelper(this);

                saveImageToDirectory();
                getSlotTime();


                calculatedDate = startDate;

                for (int i = 0; i < noOfDays; i++) {


                    MedicineModel medicineModel = new MedicineModel();
                    medicineModel.setId(id);
                    medicineModel.setDate(calculatedDate);
                    medicineModel.setMedicineName(medName);
                    medicineModel.setMedicineType(medicineType);
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
                    medicineModel.setMedicineMeal(medicineMeal);
                    medicineModel.setUniqueCode(uniqueCode);


                    setAlarm(calculatedDate, firstSlotTime, secondSlotTime, thirdSlotTime);
                    dbHelper.insertData(medicineModel, newTableName);

//                    uniqueCode++;
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putInt("uniqueCodeLastValue", uniqueCode);
//                    editor.commit();

                    calculatedDate = dc.addDays(newStartDate, String.valueOf(daysInterval));
                    newStartDate = calculatedDate;


                }


            }


            Toast.makeText(this, "Successfully added a medicine", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();

        } else {

            // Toast.makeText(getContext(), "Please check all fields has been filled up", Toast.LENGTH_SHORT).show();
        }


    }

    private void setAlarm(String calculatedDate, String firstSlotTime, String secondSlotTime, String thirdSlotTime) {


        String combine = calculatedDate + " " + firstSlotTime;
        setFinalAlarm(combine, 1);

        if (!secondSlotTime.equals("null")) {

            String combine2 = calculatedDate + " " + secondSlotTime;
            setFinalAlarm(combine2, 2);


            Log.d("Entered", "Entered 2");
        }

        if (!thirdSlotTime.equals("null")) {

            String combine3 = calculatedDate + " " + thirdSlotTime;
            setFinalAlarm(combine3, 3);
            Log.d("Entered", "Entered 3");
        }


        createAlarmModelObject();


    }

    private void createAlarmModelObject() {


        AlarmModel alarmModel = new AlarmModel();
        alarmModel.setId(0);
        alarmModel.setNdt(medName + uniqueCode);
        alarmModel.setNumberOfSlot(numberOfSlot);
        alarmModel.setFirstSlotTime(firstSlotTime);
        alarmModel.setSecondSlotTime(secondSlotTime);
        alarmModel.setThirdSlotTime(thirdSlotTime);
        alarmModel.setFirstSlotRequestCode(firstRequestCode);
        alarmModel.setSecondSlotRequestCode(secondRequestCode);
        alarmModel.setThirdSlotRequestCode(thirdRequestCode);

        AlarmDatabase alarmDatabase = new AlarmDatabase(this);
        alarmDatabase.insertAlarm(alarmModel);


        Log.d("firstRequest: ", ""+firstRequestCode);
        Log.d("secondRequest: ", ""+secondRequestCode);
        Log.d("thirdRequest: ", ""+thirdRequestCode);

//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("requestCodeValue", requestCode);
//        editor.commit();



    }

    private void setFinalAlarm(String combine, int value) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm aaa");

        Calendar calendar = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();

        try {
            calendar.setTime(sdf.parse(combine));

            int dateForAlarm = calendar.get(Calendar.DAY_OF_MONTH);
            int monthForAlarm = calendar.get(Calendar.MONTH);
            int yearForAlarm = calendar.get(Calendar.YEAR);
            int hourForAlarm = calendar.get(Calendar.HOUR_OF_DAY);
            int minuteForAlarm = calendar.get(Calendar.MINUTE);
            int secondForAlarm = 0;

            cal.set(yearForAlarm, monthForAlarm, dateForAlarm, hourForAlarm, minuteForAlarm, secondForAlarm);


        } catch (ParseException e) {
            e.printStackTrace();
        }


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("medName", medName);
        intent.putExtra("imagePath", imagePath);
        intent.putExtra("mealStatus", medicineMeal);
        intent.putExtra("time", combine);
        intent.putExtra("medType", medicineType);




        if (!firstSlotTime.equals("null") && value == 1) {


            firstRequestCode = requestCode;
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, firstRequestCode, intent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            requestCode++;

        } else if (firstSlotTime.equals("null") && value == 1){

            firstRequestCode = -1;
            Log.d("entered", "entered 1");
        }


        if (!secondSlotTime.equals("null") && value == 2) {

            secondRequestCode = requestCode;
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, secondRequestCode, intent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            requestCode++;


        } else if (secondSlotTime.equals("null") && value == 2){

            secondRequestCode = -2;
            Log.d("entered", "entered 2");
        }


        if (!thirdSlotTime.equals("null") && value == 3) {

            thirdRequestCode = requestCode;
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, thirdRequestCode, intent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            requestCode++;

        } else if (thirdSlotTime.equals("null") && value == 3){

            thirdRequestCode = -3;
            Log.d("entered", "entered 3");
        }




    }


    private String getMedicineMeal() {

        String meal = "";

        if (beforeMealRB.isChecked()) {

            meal = "Before Meal";

        } else if (afterMealRB.isChecked()) {

            meal = "After Meal";
        }

        return meal;
    }

    private String getMedicineType() {

        String type = "";

        if (medicineTypeSP.getSelectedItemPosition() == 1) {

            type = "Tablet";

        }
        if (medicineTypeSP.getSelectedItemPosition() == 2) {

            type = "Capsule";
        }
        if (medicineTypeSP.getSelectedItemPosition() == 3) {

            type = "syrup";
        }

        if (medicineTypeSP.getSelectedItemPosition() == 4) {

            type = "Injection";

        }
        if (medicineTypeSP.getSelectedItemPosition() == 5) {

            type = "Ointment";

        }
        if (medicineTypeSP.getSelectedItemPosition() == 6) {

            type = "Eye Drop";
        }

        return type;

    }

    private void getSlotTime() {

        if (noOfTimesSP.getSelectedItemPosition() == 0) {

            firstSlotTime = firstSlotTV.getText().toString();
            secondSlotTime = "null";
            thirdSlotTime = "null";

        } else if (noOfTimesSP.getSelectedItemPosition() == 1) {

            firstSlotTime = firstSlotTV.getText().toString();
            secondSlotTime = secondSlotTV.getText().toString();
            thirdSlotTime = "null";

        } else if (noOfTimesSP.getSelectedItemPosition() == 2) {

            firstSlotTime = firstSlotTV.getText().toString();
            secondSlotTime = secondSlotTV.getText().toString();
            thirdSlotTime = thirdSlotTV.getText().toString();

        }
    }

    private void saveImageToDirectory() {

        if (cvMedicineImage.getVisibility() == View.VISIBLE) {

            imagePath = medName.toUpperCase() + "(" + Calendar.getInstance().getTime() + ")";
            BitmapDrawable drawable = (BitmapDrawable) medicineIV.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            ImageSaver imageSaver = new ImageSaver(this, this);
            imageSaver.saveImage(bitmap, imagePath);

        } else {

            imagePath = "null";
        }
    }

    private boolean checkValidity() {


        if (medNameET.getText().toString().equals("")) {

            medNameET.setError("Enter a medicine name");
            Toast.makeText(this, "Please enter a valid medicine name", Toast.LENGTH_SHORT).show();
            return false;


        } else if ((firstSlotLAYOUT.getVisibility() == View.VISIBLE &&
                secondSlotLAYOUT.getVisibility() == View.VISIBLE &&
                thirdSlotLAYOUT.getVisibility() == View.VISIBLE)
                &&
                ((firstSlotTV.getText().toString().contains("Set"))
                        || secondSlotTV.getText().toString().contains("Set")
                        || thirdSlotTV.getText().toString().contains("Set"))) {


            if (firstSlotTV.getText().toString().contains("Set")) {

                Toast.makeText(this, "Please enter a valid time in slot 1", Toast.LENGTH_SHORT).show();

            } else if (secondSlotTV.getText().toString().contains("Set")) {

                Toast.makeText(this, "Please enter a valid time in slot 2", Toast.LENGTH_SHORT).show();

            } else if (thirdSlotTV.getText().toString().contains("Set")) {

                Toast.makeText(this, "Please enter a valid time in slot 3", Toast.LENGTH_SHORT).show();
            }


            return false;


        } else if (((firstSlotLAYOUT.getVisibility() == View.VISIBLE &&
                secondSlotLAYOUT.getVisibility() == View.VISIBLE &&
                thirdSlotLAYOUT.getVisibility() == View.GONE))
                &&
                ((firstSlotTV.getText().toString().contains("Set"))
                        || secondSlotTV.getText().toString().contains("Set"))) {


            if (firstSlotTV.getText().toString().contains("Set")) {

                Toast.makeText(this, "Please enter a valid time in slot 1", Toast.LENGTH_SHORT).show();

            } else if (secondSlotTV.getText().toString().contains("Set")) {

                Toast.makeText(this, "Please enter a valid time in slot 2", Toast.LENGTH_SHORT).show();
            }


            return false;


        } else if (((firstSlotLAYOUT.getVisibility() == View.VISIBLE &&
                secondSlotLAYOUT.getVisibility() == View.GONE &&
                thirdSlotLAYOUT.getVisibility() == View.GONE))
                &&
                ((firstSlotTV.getText().toString().contains("Set")))) {


            Toast.makeText(this, "Please enter a valid time in slot 1", Toast.LENGTH_SHORT).show();
            return false;


        } else if (noOfDaysET.getText().toString().equals("")) {


            noOfDaysET.setError("this field is required");
            Toast.makeText(this, "Number of days cannot be empty", Toast.LENGTH_SHORT).show();
            return false;


        } else if (startDateTV.getText().toString().contains("Touch here to set date")) {

            Toast.makeText(this, "Please enter a valid date", Toast.LENGTH_SHORT).show();

            return false;

        } else if (medicineTypeSP.getSelectedItemPosition() == 0) {

            Toast.makeText(this, "Please select a valid medicine type", Toast.LENGTH_SHORT).show();

            return false;

        } else {

            return true;
        }


    }

    public boolean checkSpecificDayValidity() {

        if (cvSpecificDayOfWeek.getVisibility() == View.VISIBLE && cbSaturday.isChecked()) {

            return true;

        } else if (cvSpecificDayOfWeek.getVisibility() == View.VISIBLE && cbSunday.isChecked()) {

            return true;

        } else if (cvSpecificDayOfWeek.getVisibility() == View.VISIBLE && cbMonday.isChecked()) {

            return true;

        } else if (cvSpecificDayOfWeek.getVisibility() == View.VISIBLE && cbTuesday.isChecked()) {

            return true;

        } else if (cvSpecificDayOfWeek.getVisibility() == View.VISIBLE && cbWednesday.isChecked()) {

            return true;

        } else if (cvSpecificDayOfWeek.getVisibility() == View.VISIBLE && cbThursday.isChecked()) {

            return true;

        } else if (cvSpecificDayOfWeek.getVisibility() == View.VISIBLE && cbFriday.isChecked()) {

            return true;

        } else if (cvSpecificDayOfWeek.getVisibility() == View.GONE) {

            return true;

        } else {

            Toast.makeText(this, "Please select at least one specific day in week\nOr choose Everyday", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void takePhoto() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, StaticVariables.MY_CAMERA_PERMISSION_CODE);

            } else {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, StaticVariables.CAMERA_REQUEST);
            }

        } else {

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

                        if (number == 1) {

                            firstSlotTV.setText(formattedTime);

                        } else if (number == 2) {

                            secondSlotTV.setText(formattedTime);

                        } else if (number == 3) {

                            thirdSlotTV.setText(formattedTime);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                myTimeListener,
                hour,
                minute,
                false);

        try {

            timePickerDialog.setTitle(message);
            timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            timePickerDialog.show();

        } catch (Exception e) {

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    public void showDatePicker() {


        DialogFragment dFragment = new AddMedicineFragment.DatePickerFragment();
        dFragment.show(getFragmentManager(), "Date Picker");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == StaticVariables.CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //  Bitmap bMapScaled = Bitmap.createScaledBitmap(photo, 330, 189, true);
            cvMedicineImage.setVisibility(View.VISIBLE);
            medicineIV.setImageBitmap(photo);
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
            return dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            TextView tv = (TextView) getActivity().findViewById(R.id.start_date_TV);


            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);
            Date chosenDate = cal.getTime();

            DateFormat df_medium_uk = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
            String df_medium_uk_str = df_medium_uk.format(chosenDate);
            tv.setText(df_medium_uk_str);


        }
    }

    public String getDaysNameOfWeek() {

        String daysName = "";

        if (cbSaturday.isChecked()) {

            daysName = daysName + "Saturday, ";
            sat = true;

        }
        if (cbSunday.isChecked()) {

            daysName = daysName + "Sunday, ";
            sun = true;

        }
        if (cbMonday.isChecked()) {

            daysName = daysName + "Monday, ";
            mon = true;

        }
        if (cbTuesday.isChecked()) {

            daysName = daysName + "Tuesday, ";
            tue = true;

        }
        if (cbWednesday.isChecked()) {

            daysName = daysName + "Wednesday, ";
            wed = true;

        }
        if (cbThursday.isChecked()) {

            daysName = daysName + "Thursday, ";
            thu = true;

        }
        if (cbFriday.isChecked()) {

            daysName = daysName + "Friday, ";
            fri = true;

        }


        return daysName.substring(0, daysName.length() - 2);
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


            Map<String, Integer> perms = new HashMap<String, Integer>();
            // Initial
            perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

            if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // All Permissions Granted

                allPermission = true;

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

            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
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
                                this,
                                "This application cannot run without Camera and Storage " +
                                        "Permissions.\nYou may relaunch the App or allow permissions" +
                                        " from Application Settings",
                                Toast.LENGTH_LONG).show();

                        allPermission = false;


                        if (perms.get(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {


                        }


                        if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


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

