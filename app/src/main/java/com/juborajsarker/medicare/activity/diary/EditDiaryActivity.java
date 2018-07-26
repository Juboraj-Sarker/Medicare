package com.juborajsarker.medicare.activity.diary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.database.DiaryDatabase;
import com.juborajsarker.medicare.model.DiaryModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditDiaryActivity extends AppCompatActivity {

    EditText titleET, descriptionET;
    Button saveBTN;

    String title, description, times, date;
    Calendar calendar;
    int id, flag;

    DiaryDatabase diaryDatabase;
    DiaryModel diaryModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_home_footer_1));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("93448558CC721EBAD8FAAE5DA52596D3").build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        flag = intent.getIntExtra("flag", -1);

         diaryModel = new DiaryModel();
         diaryDatabase = new DiaryDatabase(this);

        diaryModel = diaryDatabase.selectWithId(String.valueOf(id));

        init();
        setData(diaryModel);

    }

    private void setData(DiaryModel diaryModel) {

        titleET.setText(diaryModel.getTitle());
        descriptionET.setText(diaryModel.getDescription());
    }


    private void init() {

        titleET = (EditText) findViewById(R.id.title_ET);
        descriptionET = (EditText) findViewById(R.id.description_ET);

        saveBTN = (Button) findViewById(R.id.save_BTN);

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkValidity()){

                    title = titleET.getText().toString();
                    description = descriptionET.getText().toString();

                    Calendar cal = Calendar.getInstance();
                    Date chosenDate = cal.getTime();

                    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
                    date = dateFormat.format(chosenDate);

                    calendar = Calendar.getInstance();

                    SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a");
                    String strTime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
                    Date time = null;
                    try {
                        time = sdf24.parse(strTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    times = sdf12.format(time);


                    DiaryModel model = new DiaryModel();
                    model.setId(id);
                    model.setTitle(title);
                    model.setDescription(description);
                    model.setDate(date);
                    model.setTime(times);


                    diaryDatabase.updateDiary(model);
                    if (flag == 1){

                        startActivity(new Intent(EditDiaryActivity.this, DiaryActivity.class));
                        Toast.makeText(EditDiaryActivity.this, "Successfully edited", Toast.LENGTH_SHORT).show();
                        finish();

                    }else {

                        EditDiaryActivity.super.onBackPressed();
                        Toast.makeText(EditDiaryActivity.this, "Successfully edited", Toast.LENGTH_SHORT).show();
                    }




                }
            }
        });
    }


    private boolean checkValidity(){

        if (titleET.getText().toString().equals("")){

            titleET.setError("This field is required !!!");
            return false;

        }else if (descriptionET.getText().toString().equals("")){

            descriptionET.setError("This field is required !!!");
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
