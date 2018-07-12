package com.juborajsarker.medicare.activity.blood;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.model.DonorModel;

import java.util.Locale;

public class DonorInputActivity extends AppCompatActivity {

    EditText nameET, phoneET, emailET, cityET;
    Spinner bgSP;
    TextView messageTV;
    Button inputBTN;
    String name, phone, email, city, bloodGroup, country;


    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceAll;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_input);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        init();
    }

    private void init() {

        nameET = (EditText) findViewById(R.id.nameET);
        phoneET = (EditText) findViewById(R.id.phoneET);
        emailET = (EditText) findViewById(R.id.emailET);
        cityET = (EditText) findViewById(R.id.cityET);

        messageTV = (TextView) findViewById(R.id.messageTV);

        bgSP = (Spinner) findViewById(R.id.blood_group_SP);

        inputBTN = (Button) findViewById(R.id.inputBTN);

        inputBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if (checkValidity()){

                    progressDialog = new ProgressDialog(DonorInputActivity.this);
                    progressDialog.setMessage("Connecting with server. Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();


                    name = nameET.getText().toString();
                    phone = phoneET.getText().toString();
                    if (emailET.getText().toString().equals("")){

                        email = "Not Found";

                    }else {

                        email = emailET.getText().toString();
                    }

                    city = cityET.getText().toString();
                    bloodGroup = getBloodGroup();
                    country = getUserCountry(DonorInputActivity.this);


                    inputData();

                }

            }
        });
    }



    private boolean checkValidity(){

        if (nameET.getText().toString().equals("") || Character.isWhitespace(nameET.getText().toString().charAt(0))){

            if (nameET.getText().toString().equals("")){

                nameET.setError("Donor's name is required !!!");

            }else if (Character.isWhitespace(nameET.getText().toString().charAt(0))){

                nameET.setError("Name cannot start with a space");
            }

            return false;

        }else if (phoneET.getText().toString().equals("") || Character.isWhitespace(phoneET.getText().toString().charAt(0))){

            if (phoneET.getText().toString().equals("")){

                phoneET.setError("Phone number is required !!!");

            }else if (Character.isWhitespace(phoneET.getText().toString().charAt(0))){

                phoneET.setError("Phone number cannot start with a space");
            }

            return false;

        }else if (cityET.getText().toString().equals("") || Character.isWhitespace(cityET.getText().toString().charAt(0))){

            if (cityET.getText().toString().equals("")){

                cityET.setError("City name is required !!!");

            }else if (Character.isWhitespace(cityET.getText().toString().charAt(0))){

                cityET.setError("City name cannot start with a space");
            }

            return false;

        }else if (emailET.getText().toString().length() > 0 && Character.isWhitespace(emailET.getText().toString().charAt(0))){

            emailET.setError("Email cannot start with a space");
            return false;

        }else if (bgSP.getSelectedItemPosition() == 0){

            Toast.makeText(this, "Please select a valid blood group", Toast.LENGTH_SHORT).show();
            return false;

        }else {

            return true;
        }

    }

    public String getBloodGroup (){

        String bg = "";

        switch (bgSP.getSelectedItemPosition()){

            case 1:{

                bg = "A+";
                break;

            }case 2:{

                bg = "A-";
                break;

            }case 3:{

                bg = "B+";
                break;

            }case 4:{

                bg = "B-";
                break;

            }case 5:{

                bg = "AB+";
                break;

            }case 6:{

                bg = "AB-";
                break;

            }case 7:{

                bg = "O+";
                break;

            }case 8:{

                bg = "O-";
                break;

            }
        }

        return bg;
    }



    private void inputData() {


        databaseReference = FirebaseDatabase.getInstance().getReference("User/Donor/" +
                country + "/" + city.toLowerCase() + "/" + bloodGroup);

        databaseReferenceAll = FirebaseDatabase.getInstance().getReference("User/Donor/" +
                country + "/" + "All" + "/"+ bloodGroup);

        final DonorModel donorModel = new DonorModel();
        donorModel.setUid("null");
        donorModel.setName(name);
        donorModel.setEmail(email);
        donorModel.setPhoneNumber(phone);
        donorModel.setCity(city);
        donorModel.setCountry(country);
        donorModel.setPassword("null");
        donorModel.setBloodGroup(bloodGroup);
        donorModel.setDonor(true);

        databaseReferenceAll.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(phone)){

                    phoneET.setError("This phone number is already registered");
                    progressDialog.dismiss();
                    messageTV.setVisibility(View.GONE);

                }else {

                    databaseReference.child(phone).setValue(donorModel);
                    databaseReferenceAll.child(phone).setValue(donorModel);
                    progressDialog.dismiss();
                    Toast.makeText(DonorInputActivity.this, "Successfully inserted data into cloud", Toast.LENGTH_SHORT).show();
                   // DonorInputActivity.super.onBackPressed();

                    nameET.setText("");
                    phoneET.setText("");
                    cityET.setText("");
                    emailET.setText("");
                    bgSP.setSelection(0);
                    messageTV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




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
