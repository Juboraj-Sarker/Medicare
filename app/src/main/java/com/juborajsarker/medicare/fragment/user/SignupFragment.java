package com.juborajsarker.medicare.fragment.user;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.activity.MainActivity;
import com.juborajsarker.medicare.model.DonorModel;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class SignupFragment extends Fragment {

    View view;

    EditText nameET, emailET, phoneET, cityET, passwordET, confPassET ;
    Spinner bgSP;
    Button signUpBTN;
    RadioButton yesRB, noRB;

    String name, email, phone, city, password, bloodGroup, country;
    String uid;

    private SharedPreferences sharedPreferences;
    boolean onlineRegister = false;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;



    public SignupFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_signup, container, false);


        sharedPreferences = getActivity().getSharedPreferences("registerStatus", MODE_PRIVATE);
        onlineRegister = sharedPreferences.getBoolean("isLoggedIn", false);

        firebaseAuth = FirebaseAuth.getInstance();

        MobileAds.initialize(getActivity().getApplicationContext(), getString(R.string.banner_home_footer_1));
        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("93448558CC721EBAD8FAAE5DA52596D3").build();
        mAdView.loadAd(adRequest);

        country = getUserCountry(getContext());

        init();
        setAction();



        return view;
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


    private void init() {

        nameET = (EditText) view.findViewById(R.id.nameET) ;
        emailET = (EditText) view.findViewById(R.id.emailET);
        phoneET = (EditText) view.findViewById(R.id.phoneET);
        cityET = (EditText) view.findViewById(R.id.cityET);
        passwordET = (EditText) view.findViewById(R.id.passwordET);
        confPassET = (EditText) view.findViewById(R.id.confPassET);

        bgSP = (Spinner) view.findViewById(R.id.blood_group_SP);

        signUpBTN = (Button) view.findViewById(R.id.signUpBTN);

        yesRB = (RadioButton) view.findViewById(R.id.yesRB);
        noRB = (RadioButton) view.findViewById(R.id.noRB);

        yesRB.setChecked(true);
    }


    private void setAction() {

        yesRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                    yesRB.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    noRB.setBackgroundColor(getResources().getColor(R.color.colorPrimary));


                }
            }
        });

        noRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                    yesRB.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    noRB.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                    showDialog();

                }
            }
        });



        signUpBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (!isEmpty()){

                    if (emailValidation() && passwordValidation() && bgValidation()){

                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Please wait .....");
                        progressDialog.show();



                        name = nameET.getText().toString();
                        email = emailET.getText().toString();
                        phone = phoneET.getText().toString();
                        city = cityET.getText().toString();
                        password = passwordET.getText().toString();
                        bloodGroup = getBloodGroup();


                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()){


                                            Toast.makeText(getContext(), "Successfully created user", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(getContext(), MainActivity.class);
                                            intent.putExtra("fragmentName", "home");

                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                            if (user != null){

                                                uid = user.getUid();

                                                if (yesRB.isChecked()){

                                                    registerAsDonor(uid);

                                                }else if (noRB.isChecked()){

                                                    registerAsUser(uid);
                                                }
                                            }



                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putBoolean("isLoggedIn", true);
                                            editor.putString("email", email);
                                            editor.putString("uid", uid);
                                            editor.commit();

                                            startActivity(intent);
                                            getActivity().finish();

                                        }else {

                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }

                                    }
                                });

                    }else {

                        if (!emailValidation()){

                            emailET.setError("This is not valid email type !!!");

                        }

                        if (!passwordValidation()){

                            confPassET.setError("Password not matched !!!");
                        }

                        if (!bgValidation()){

                            Toast.makeText(getContext(), "Please enter a valid blood group", Toast.LENGTH_SHORT).show();
                        }
                    }



                }else {

                    if (nameET.getText().toString().equals("")){

                        nameET.setError("This field is required !!!");
                    }

                    if (emailET.getText().toString().equals("")){

                        emailET.setError("This field is required !!!");
                    }

                    if (phoneET.getText().toString().equals("")){

                        phoneET.setError("This field is required !!!");
                    }

                    if (cityET.getText().toString().equals("")){

                        cityET.setError("This field is required !!!");
                    }

                    if (passwordET.getText().toString().equals("")){

                        passwordET.setError("This field is required !!!");
                    }

                    if (confPassET.getText().toString().equals("")){

                        confPassET.setError("This field is required !!!");
                    }
                }
            }
        });
    }


    private boolean isEmpty() {

        if (emailET.getText().toString().equals("")
                || passwordET.getText().toString().equals("")
                || confPassET.getText().toString().equals("")
                || nameET.getText().toString().equals("")
                || cityET.getText().toString().equals("")
                || phoneET.getText().toString().equals("")){

            return true;
        }

        else {

            return false;
        }
    }


    private boolean emailValidation() {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String inputEmail = emailET.getText().toString().trim();

        if (inputEmail.matches(emailPattern)){

            return true;
        }else {

            return false;
        }


    }

    private boolean passwordValidation() {

        if (passwordET.getText().toString().equals(confPassET.getText().toString())){

            return true;

        }else {

            return false;
        }

    }


    private boolean bgValidation() {

        if (bgSP.getSelectedItemPosition() == 0){

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

    private void showDialog(){

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle("The Blood Donor of today may be recipient of tomorrow")
                .setMessage("Do you know, Tears of a mother cannot save her Child, But your Blood can. \nBlood Donation will cost you nothing but it will save a life. So be a blood donor and create millions of smiles !!!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                       dialog.dismiss();


                    }
                }).show();

    }


    private void registerAsDonor(String uid){

        databaseReference = FirebaseDatabase.getInstance().getReference("User/Donor/" +
                country + "/" + city.toLowerCase() + "/" + bloodGroup);

        DatabaseReference databaseReferenceAll = FirebaseDatabase.getInstance().getReference("User/Donor/" +
                country + "/" + "All" + "/"+ bloodGroup);

        DonorModel donorModel = new DonorModel();
        donorModel.setUid(uid);
        donorModel.setName(name);
        donorModel.setEmail(email);
        donorModel.setPhoneNumber(phone);
        donorModel.setCity(city);
        donorModel.setCountry(country);
        donorModel.setPassword(password);
        donorModel.setBloodGroup(bloodGroup);
        donorModel.setDonor(true);

        databaseReference.child(uid).setValue(donorModel);
        databaseReferenceAll.child(uid).setValue(donorModel);


    }

    private void registerAsUser(String uid){

        databaseReference = FirebaseDatabase.getInstance().getReference("User/Recipient/" +
                country + "/" + city.toLowerCase());

        DonorModel donorModel = new DonorModel();
        donorModel.setUid(uid);
        donorModel.setName(name);
        donorModel.setEmail(email);
        donorModel.setPhoneNumber(phone);
        donorModel.setCity(city);
        donorModel.setCountry(country);
        donorModel.setPassword(password);
        donorModel.setBloodGroup(bloodGroup);
        donorModel.setDonor(false);

        databaseReference.child(uid).setValue(donorModel);

    }

}
