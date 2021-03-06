package com.juborajsarker.medicare.fragment.user;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.activity.more.AmbulanceActivity;
import com.juborajsarker.medicare.activity.user.ForgetPasswordActivity;

import static android.content.Context.MODE_PRIVATE;


public class SigninFragment extends Fragment {

    View view;

    EditText emailET, passwordET;
    Button signInBTN;
    TextView forgotPassTV;

    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;

    String uid = "";
    boolean onlineRegister;
    String email, password;


    public SigninFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_signin, container, false);

        sharedPreferences = getActivity().getSharedPreferences("registerStatus", MODE_PRIVATE);
        onlineRegister = sharedPreferences.getBoolean("onlineRegisterStatus", false);

        firebaseAuth = FirebaseAuth.getInstance();


        MobileAds.initialize(getActivity().getApplicationContext(), getString(R.string.banner_home_footer_1));
        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("93448558CC721EBAD8FAAE5DA52596D3").build();
        mAdView.loadAd(adRequest);

        init();
        setAction();


        return view;
    }


    private void init() {

        emailET = (EditText) view.findViewById(R.id.emailET);
        passwordET = (EditText) view.findViewById(R.id.passwordET);

        signInBTN = (Button) view.findViewById(R.id.btnSignIn);

        forgotPassTV = (TextView) view.findViewById(R.id.forgotPass_TV);

    }

    private void setAction() {

        signInBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (emptyValidation() && emailValidation()){


                    signIn();

                }else {

                    if (!emptyValidation()){

                        if (emailET.getText().toString().equals("")){

                            emailET.setError("This field is required !!!");

                        }

                        if (passwordET.getText().toString().equals("")){

                            passwordET.setError("This field is required !!!");
                        }
                    }

                    if (!emailValidation()){

                        emailET.setError("Email type is not valid !!!");
                    }
                }


            }
        });


        forgotPassTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getContext(), ForgetPasswordActivity.class));
            }
        });
    }




    private boolean emptyValidation() {

        if (emailET.getText().toString().equals("") || passwordET.getText().toString().equals("")){

            return false;

        }else {

            return true;
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



    private void signIn() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Checking information\nPlease wait.....");
        progressDialog.show();

        email = emailET.getText().toString();
        password = passwordET.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){


                            Toast.makeText(getContext(), "Login Successful !!!", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if (user != null){

                                uid = user.getUid();

                            }


                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.putString("email", email);
                            editor.putString("uid", uid);
                            editor.commit();

                            progressDialog.dismiss();

                            Intent intent = new Intent(getContext(), AmbulanceActivity.class);
                            startActivity(intent);
                            getActivity().finish();

                        }else {

                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                });


    }

}
