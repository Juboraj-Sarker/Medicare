package com.juborajsarker.medicare.activity.diary;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.database.DiaryDatabase;
import com.juborajsarker.medicare.model.DiaryModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddDiaryActivity extends AppCompatActivity {

    EditText titleET, descriptionET;
    Button saveBTN;

    String title, description, times, date;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        init();
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


                    DiaryModel diaryModel = new DiaryModel();
                    diaryModel.setId(0);
                    diaryModel.setTitle(title);
                    diaryModel.setDescription(description);
                    diaryModel.setDate(date);
                    diaryModel.setTime(times);

                    DiaryDatabase database = new DiaryDatabase(AddDiaryActivity.this);
                    database.insertDiary(diaryModel);
                    AddDiaryActivity.super.onBackPressed();




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
