package com.juborajsarker.medicare.activity.blood;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.juborajsarker.medicare.R;

public class DonorDetailsActivity extends AppCompatActivity {

    TextView nameTV, bgTV, cityTV, emailTV, phoneTV, helpTV;
    ImageView copyPhoneIV, copyEmailIV;
    Button callBTN, smsBTN, emailBTN;
    public static final int MY_PERMISSIONS_REQUEST_CALL = 55;

    String name, city, bloodGroup, phone, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_details);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_home_footer_1));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("93448558CC721EBAD8FAAE5DA52596D3").build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        bloodGroup = intent.getStringExtra("bloodGroup");
        city = intent.getStringExtra("city");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");

        init();
        setData();


    }

    private void setData() {

        nameTV.setText(name);
        bgTV.setText(bloodGroup);
        cityTV.setText(city);
        emailTV.setText(email);
        phoneTV.setText(phone);
    }

    private void init() {

        nameTV = (TextView) findViewById(R.id.nameTV);
        bgTV = (TextView) findViewById(R.id.bg_TV);
        cityTV = (TextView) findViewById(R.id.city_TV);
        emailTV = (TextView) findViewById(R.id.emailTV);
        phoneTV = (TextView) findViewById(R.id.phoneTV);
        helpTV = (TextView) findViewById(R.id.helpTV);

        copyPhoneIV = (ImageView) findViewById(R.id.phone_copy_IV);
        copyEmailIV = (ImageView) findViewById(R.id.email_copy_IV);

        callBTN = (Button) findViewById(R.id.btn_call);
        smsBTN = (Button) findViewById(R.id.btn_sms);
        emailBTN = (Button) findViewById(R.id.btn_email);


        copyEmailIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!emailTV.getText().toString().equals("Not Found")){

                    copyItem(emailTV.getText().toString());

                }else {

                    Toast.makeText(DonorDetailsActivity.this, "No email found", Toast.LENGTH_SHORT).show();
                }

            }
        });


        copyPhoneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                copyItem(phoneTV.getText().toString());
            }
        });


        callBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeCall(phoneTV.getText().toString());
            }
        });

        smsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendSms(phoneTV.getText().toString());
            }
        });

        emailBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!emailTV.getText().toString().equals("Not Found")){

                    sendEmail(emailTV.getText().toString());

                }else {

                    Toast.makeText(DonorDetailsActivity.this, "No email found", Toast.LENGTH_SHORT).show();
                }

            }
        });



        helpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DonorDetailsActivity.this, DonorInputActivity.class));
            }
        });
    }

    private void sendEmail(String email) {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Type email here");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private void sendSms(String phoneNumber) {

        Uri uri = Uri.parse("smsto:"+ phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", "");
        startActivity(intent);

    }

    private void makeCall(String phoneNumber) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));

        if (!checkCallPermission()) {

            checkCallPermission();
            Toast.makeText(this, "Please give call permission to make call", Toast.LENGTH_SHORT).show();

        }else {

            startActivity(callIntent);
        }
    }

    private void copyItem(String item) {

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipe = ClipData.newPlainText("phone", item);

        if (clipboard != null) {
            clipboard.setPrimaryClip(clipe);
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }



    private boolean checkCallPermission() {


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL);


            return false;

        } else {

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
