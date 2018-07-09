package com.juborajsarker.medicare.activity.diary;

import android.app.AlertDialog;
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

import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.database.DiaryDatabase;
import com.juborajsarker.medicare.model.DiaryModel;

public class DetailsDiaryActivity extends AppCompatActivity {

    TextView titleTV, descriptionTV;
    Button editBTN, deleteBTN;

    int id;
    DiaryModel diaryModel;
    DiaryDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_diary);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        diaryModel = new DiaryModel();
        database = new DiaryDatabase(this);
        diaryModel = database.selectWithId(String.valueOf(id));

        init();
    }

    private void init() {

        titleTV = (TextView) findViewById(R.id.title_TV);
        descriptionTV = (TextView) findViewById(R.id.description_TV);

        titleTV.setText(diaryModel.getTitle());
        descriptionTV.setText(diaryModel.getDescription());

        editBTN = (Button) findViewById(R.id.btn_edit);
        deleteBTN = (Button) findViewById(R.id.btn_delete);

        editBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailsDiaryActivity.this, EditDiaryActivity.class);
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
                    builder = new AlertDialog.Builder(DetailsDiaryActivity.this, android.R.style.Theme_Material_Light_Dialog);
                } else {
                    builder = new AlertDialog.Builder(DetailsDiaryActivity.this);
                }
                builder.setTitle("Continue with deletion")
                        .setMessage("Are you sure you want to really delete this data?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                deleteFile(diaryModel);


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

    private void deleteFile(DiaryModel diaryModel) {

        database.deleteDiary(diaryModel);
        super.onBackPressed();
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
