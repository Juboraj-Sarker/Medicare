package com.juborajsarker.medicare.activity.doctors;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.database.DoctorDatabase;
import com.juborajsarker.medicare.model.DoctorModel;

public class DetailsDoctorActivity extends AppCompatActivity {

    TextView nameTV, phoneTV, emailTV, specialityTV, addressTV, chamberTV;
    ImageView copyEmailIV, copyPhoneIV;
    Button editBTN, deleteBTN;

    int id;
    DoctorModel model;
    DoctorDatabase doctorDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_doctor);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        model = new DoctorModel();
        doctorDatabase = new DoctorDatabase(this);
        model = doctorDatabase.selectWithID(String.valueOf(id));

        init();
        setData(model);


    }

    private void init() {

        nameTV = (TextView) findViewById(R.id.doctor_name_TV);
        phoneTV = (TextView) findViewById(R.id.doctor_phone_TV);
        emailTV = (TextView) findViewById(R.id.doctor_email_TV);
        specialityTV = (TextView) findViewById(R.id.doctor_speciality_TV);
        addressTV = (TextView) findViewById(R.id.doctor_address_TV);
        chamberTV = (TextView) findViewById(R.id.doctor_chamber_TV);

        copyEmailIV = (ImageView) findViewById(R.id.email_copy_IV);
        copyPhoneIV = (ImageView) findViewById(R.id.phone_copy_IV);

        editBTN = (Button) findViewById(R.id.btn_edit);
        deleteBTN = (Button) findViewById(R.id.btn_delete);

        editBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailsDoctorActivity.this, EditDoctorActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("flag", 1);
                startActivity(intent);
                finish();
            }
        });

        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(DetailsDoctorActivity.this, android.R.style.Theme_Material_Light_Dialog);
                } else {
                    builder = new AlertDialog.Builder(DetailsDoctorActivity.this);
                }
                builder.setTitle("Continue with deletion")
                        .setMessage("Are you sure you want to really delete this data?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                deleteFile(model);


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


        copyPhoneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String s = phoneTV.getText().toString();
                ClipData clipe = ClipData.newPlainText("phone", s);
                clipboard.setPrimaryClip(clipe);
                Toast.makeText(DetailsDoctorActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();

            }
        });


        copyEmailIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                if (emailTV.getText().toString().equals("null")){

                    Toast.makeText(DetailsDoctorActivity.this, "No Email Address found", Toast.LENGTH_SHORT).show();

                }else {

                    String s = emailTV.getText().toString();
                    ClipData clipe = ClipData.newPlainText("email", s);
                    clipboard.setPrimaryClip(clipe);
                    Toast.makeText(DetailsDoctorActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    private void setData(DoctorModel model) {

        nameTV.setText(model.getName());
        phoneTV.setText(model.getPhoneNumber());
        emailTV.setText(model.getEmail());
        specialityTV.setText(model.getSpeciality());
        addressTV.setText(model.getAddress());
        chamberTV.setText(model.getChamber());
    }


    private void deleteFile(DoctorModel model) {

        doctorDatabase.deleteDoctor(model);
        super.onBackPressed();
        Toast.makeText(this, "Successfully deleted !!!", Toast.LENGTH_SHORT).show();
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
