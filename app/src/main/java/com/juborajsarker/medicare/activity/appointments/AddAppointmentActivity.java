package com.juborajsarker.medicare.activity.appointments;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.activity.MainActivity;
import com.juborajsarker.medicare.broadcastReceiver.AlarmReceiver;
import com.juborajsarker.medicare.database.AppointmentDatabase;
import com.juborajsarker.medicare.model.AppointmentModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddAppointmentActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    EditText appTitleET, doctorNameET, doctorSpecialityET, rememberBeforeET, locationET, notesET;
    TextView dateTV, timeTV;
    RadioButton hourRB, minuteRB;
    Button addAppointmentBTN;


    Calendar myCalender;
    String formattedTime;
    String appTitle, docName, docSpeciality, date, time, rememberBefore, location, notes;
    long rememberBeforeTimeInMills;
    int lastRequestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){

            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_home_footer_1));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("93448558CC721EBAD8FAAE5DA52596D3").build();
        mAdView.loadAd(adRequest);


        sharedPreferences = getSharedPreferences("alarmRequestCode", MODE_PRIVATE);
        lastRequestCode = sharedPreferences.getInt("requestCodeValue", 1);

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


                showDatePicker();




            }
        });

        timeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showHourPicker("Select appointment time");
            }
        });


        addAppointmentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkValidity()){

                    collectData();
                    String combine = date +" " + time;
                    setAlarm(combine);





                }


            }
        });

    }

    private void insertIntoDatabase() {

        AppointmentModel appointmentModel = new AppointmentModel();

        appointmentModel.setId(0);
        appointmentModel.setAppointmentTitle(appTitle);
        appointmentModel.setDoctorName(docName);
        appointmentModel.setDoctorSpeciality(docSpeciality);
        appointmentModel.setDate(date);
        appointmentModel.setTime(time);
        appointmentModel.setRememberBefore(rememberBefore);
        appointmentModel.setRememberBeforeTimeInMills(rememberBeforeTimeInMills);
        appointmentModel.setLocation(location);
        appointmentModel.setNotes(notes);
        appointmentModel.setRequestCode(lastRequestCode);

        AppointmentDatabase database = new AppointmentDatabase(AddAppointmentActivity.this);
        database.insertAppointment(appointmentModel);


        Intent intent = new Intent(AddAppointmentActivity.this, MainActivity.class);
        intent.putExtra("open", "appointment");
        startActivity(intent);
    }


    private void collectData() {

        appTitle = appTitleET.getText().toString();
        docName = doctorNameET.getText().toString();

        if (doctorSpecialityET.getText().toString().equals("")){

            docSpeciality = "null";

        }else {

            docSpeciality = doctorSpecialityET.getText().toString();

        }

        date = dateTV.getText().toString();
        time = timeTV.getText().toString();
        rememberBefore = rememberBeforeET.getText().toString();
        location = locationET.getText().toString();

        if (notesET.getText().toString().equals("")){

            notes = "null";

        }else {

            notes = notesET.getText().toString();
        }

        if (hourRB.isChecked()){

            rememberBeforeTimeInMills = Long.parseLong(rememberBefore) * 3600000;

        }else if (minuteRB.isChecked()){

            rememberBeforeTimeInMills = Long.parseLong(rememberBefore) * 60000;
        }
    }


    private void setAlarm(String combine) {

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
        intent.putExtra("appointment", "true");
        intent.putExtra("doctorName", docName);
        intent.putExtra("appTime", time);
        intent.putExtra("location", location);


        lastRequestCode ++;

        if (Calendar.getInstance().getTimeInMillis() >= (cal.getTimeInMillis() - rememberBeforeTimeInMills) ){

            Log.d("before", "before current time");
            Toast.makeText(this, "Please select a future date time", Toast.LENGTH_SHORT).show();

        }else {

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, lastRequestCode, intent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - rememberBeforeTimeInMills , pendingIntent);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("requestCodeValue", lastRequestCode);
            editor.commit();

            insertIntoDatabase();
        }





    }

    private boolean checkValidity(){

        if (appTitleET.getText().toString().equals("")){

            appTitleET.setError("This field is required !!!");
            return false;

        }else if (doctorNameET.getText().toString().equals("")){

            doctorNameET.setError("This field is required !!!");
            return false;

        }else if (locationET.getText().toString().equals("")){

            locationET.setError("This field is required !!!");
            return false;

        }else if (rememberBeforeET.getText().toString().equals("")){

            rememberBeforeET.setError("This field is required !!!");
            return false;

        }else if (dateTV.getText().toString().equals("Select date")){

            Toast.makeText(this, "Select a valid date", Toast.LENGTH_SHORT).show();
            return false;

        }else if (timeTV.getText().toString().equals("Select time")){

            Toast.makeText(this, "Select a valid time", Toast.LENGTH_SHORT).show();
            return false;

        }else {

            return true;
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

            TextView tv = (TextView) getActivity().findViewById(R.id.date_TV);


            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);
            Date chosenDate = cal.getTime();

            DateFormat df_medium_uk = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
            String df_medium_uk_str = df_medium_uk.format(chosenDate);
            tv.setText(df_medium_uk_str);


        }
    }

    public void showHourPicker(String message) {


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

                        timeTV.setText(formattedTime);

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


        DialogFragment dFragment = new DatePickerFragment();
        dFragment.show(getFragmentManager(), "Date Picker");

    }
}
