package com.juborajsarker.medicare.activity.appointments;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.activity.MainActivity;
import com.juborajsarker.medicare.broadcastReceiver.AlarmReceiver;
import com.juborajsarker.medicare.database.AppointmentDatabase;
import com.juborajsarker.medicare.model.AppointmentModel;

public class DetailsAppointmentActivity extends AppCompatActivity {

    TextView titleTV, dateTV, doctorNameTV, doctorSpecialityTV, locationTV, rememberBeforeTV, notesTV;
    Button editBTN, deleteBTN;

    int id;
    AppointmentModel appointmentModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_appointment);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){

            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_home_footer_1));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("93448558CC721EBAD8FAAE5DA52596D3").build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        init();
        setData();


    }



    private void init() {

        titleTV = (TextView) findViewById(R.id.title_TV);
        dateTV = (TextView) findViewById(R.id.date_TV);
        doctorNameTV = (TextView) findViewById(R.id.doctor_name_TV);
        doctorSpecialityTV = (TextView) findViewById(R.id.doctor_speciality_TV);
        locationTV = (TextView) findViewById(R.id.location_TV);
        rememberBeforeTV = (TextView) findViewById(R.id.remember_before_TV);
        notesTV = (TextView) findViewById(R.id.notes_TV);

        editBTN = (Button) findViewById(R.id.btn_edit);
        deleteBTN = (Button) findViewById(R.id.btn_delete);



        editBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(DetailsAppointmentActivity.this, EditAppointmentActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);


            }
        });

        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(DetailsAppointmentActivity.this, android.R.style.Theme_Material_Light_Dialog);
                } else {
                    builder = new AlertDialog.Builder(DetailsAppointmentActivity.this);
                }
                builder.setTitle("Continue with deletion")
                        .setMessage("Are you sure you want to really delete this data?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                appointmentModel = new AppointmentModel();
                                AppointmentDatabase database = new AppointmentDatabase(DetailsAppointmentActivity.this);
                                appointmentModel = database.selectWithID(String.valueOf(id));
                                deleteAlarm(appointmentModel.getRequestCode());
                                database.deleteAppointment(appointmentModel);

                                Intent intent = new Intent(DetailsAppointmentActivity.this, MainActivity.class);
                                intent.putExtra("open", "appointment");
                                startActivity(intent);
                                Toast.makeText(DetailsAppointmentActivity.this, "Appointment deleted successfully", Toast.LENGTH_SHORT).show();



                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();






            }
        });
    }

    private void deleteAlarm(int requestCode) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);
        alarmManager.cancel(pendingIntent);
    }


    private void setData() {

        AppointmentDatabase database = new AppointmentDatabase(this);
        appointmentModel = new AppointmentModel();
        appointmentModel = database.selectWithID(String.valueOf(id));

        titleTV.setText(appointmentModel.getAppointmentTitle());
        dateTV.setText(appointmentModel.getDate() + " at " + appointmentModel.getTime());
        doctorNameTV.setText(appointmentModel.getDoctorName());
        doctorSpecialityTV.setText(appointmentModel.getDoctorSpeciality());
        locationTV.setText(appointmentModel.getLocation());
        rememberBeforeTV.setText(appointmentModel.getRememberBefore());
        notesTV.setText(appointmentModel.getNotes());




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
