package com.juborajsarker.medicare.activity.doctors;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.database.DoctorDatabase;
import com.juborajsarker.medicare.model.DoctorModel;

public class AddDoctorActivity extends AppCompatActivity {

    EditText nameET, phoneET, emailET, specialityET, addressET, chamberET;
    Button saveBTN;

    String name, phone, email, speciality, address, chamber;
    DoctorDatabase doctorDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        init();


    }

    private void init() {

        nameET = (EditText) findViewById(R.id.doctor_name_ET);
        phoneET = (EditText) findViewById(R.id.doctor_phone_ET);
        emailET = (EditText) findViewById(R.id.doctor_email_ET);
        specialityET = (EditText) findViewById(R.id.doctor_speciality_ET);
        addressET = (EditText) findViewById(R.id.doctor_address_ET);
        chamberET = (EditText) findViewById(R.id.doctor_chamber_ET);

        saveBTN = (Button) findViewById(R.id.save_BTN);
        doctorDatabase = new DoctorDatabase(this);

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkValidity()){

                    name = nameET.getText().toString();
                    phone = phoneET.getText().toString();

                    if (emailET.getText().toString().equals("")){

                        email = "null";

                    }else {

                        email = emailET.getText().toString();

                    }


                    if (specialityET.getText().toString().equals("")){

                        speciality = "null";

                    }else {

                        speciality = specialityET.getText().toString();

                    }


                    if (addressET.getText().toString().equals("")){

                        address = "null";

                    }else {

                        address = addressET.getText().toString();

                    }

                    if (chamberET.getText().toString().equals("")){

                        chamber = "null";

                    }else {

                        chamber = chamberET.getText().toString();

                    }


                    DoctorModel doctorModel = new DoctorModel();
                    doctorModel.setId(0);
                    doctorModel.setName(name);
                    doctorModel.setPhoneNumber(phone);
                    doctorModel.setSpeciality(speciality);
                    doctorModel.setAddress(address);
                    doctorModel.setChamber(chamber);
                    doctorModel.setEmail(email);

                    doctorDatabase.insertDoctor(doctorModel);
                    Toast.makeText(AddDoctorActivity.this, "New Doctor added successfully", Toast.LENGTH_SHORT).show();
                    AddDoctorActivity.super.onBackPressed();



                }
            }
        });

    }


    private boolean checkValidity(){

        if (nameET.getText().toString().equals("")){

            nameET.setError("This field is required");
            return false;

        }else if (phoneET.getText().toString().equals("")){

            phoneET.setError("This field is required");
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
}
