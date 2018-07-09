package com.juborajsarker.medicare.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.juborajsarker.medicare.model.DiaryModel;

import java.util.ArrayList;
import java.util.List;

public class DiaryDatabase extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "diary_manager";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "diary_table";

    private static final String COLUMN_1 = "ID";
    private static final String COLUMN_2 = "TITLE";
    private static final String COLUMN_3 = "DESCRIPTION";
    private static final String COLUMN_4 = "DATE";
    private static final String COLUMN_5 = "TIME";

    public DiaryDatabase(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_QUERY = "CREATE TABLE "
                + TABLE_NAME + "("
                + COLUMN_1 + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + COLUMN_2 + " TEXT,"
                + COLUMN_3 + " TEXT,"
                + COLUMN_4 + " TEXT,"
                + COLUMN_5 + " TEXT" + ")";

        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertDiary(DiaryModel diaryModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_2, diaryModel.getTitle());
        values.put(COLUMN_3, diaryModel.getDescription());
        values.put(COLUMN_4, diaryModel.getDate());
        values.put(COLUMN_5, diaryModel.getTime());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public int updateDiary(DiaryModel diaryModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_2, diaryModel.getTitle());
        values.put(COLUMN_3, diaryModel.getDescription());
        values.put(COLUMN_4, diaryModel.getDate());
        values.put(COLUMN_5, diaryModel.getTime());

        return db.update(TABLE_NAME, values, COLUMN_1 + " = ?",
                new String[]{String.valueOf(diaryModel.getId())});
    }

    public List<DiaryModel> getAllDiary() {

        List<DiaryModel> diaryModelList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {

                DiaryModel model = new DiaryModel();
                model.setId(Integer.parseInt(cursor.getString(0)));
                model.setTitle(cursor.getString(1));
                model.setDescription(cursor.getString(2));
                model.setDate(cursor.getString(3));
                model.setTime(cursor.getString(4));

                diaryModelList.add(model);


            } while (cursor.moveToNext());
        }


        return diaryModelList;
    }


    public void deleteDiary(DiaryModel diaryModel) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, COLUMN_1 + " = ?",
                new String[]{String.valueOf(diaryModel.getId())});

        db.close();
    }


    public List<DiaryModel> getSelectedDiary(String searchKeyword) {

        List<DiaryModel> diaryModelList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE TITLE=?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{searchKeyword});

        if (cursor.moveToFirst()) {

            do {

                DiaryModel model = new DiaryModel();
                model.setId(Integer.parseInt(cursor.getString(0)));
                model.setTitle(cursor.getString(1));
                model.setDescription(cursor.getString(2));
                model.setDate(cursor.getString(3));
                model.setTime(cursor.getString(4));

                diaryModelList.add(model);


            } while (cursor.moveToNext());
        }


        return diaryModelList;
    }


    public DiaryModel selectWithId(String id) {

        DiaryModel model = new DiaryModel();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE ID=?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        if (cursor != null)
            cursor.moveToFirst();


        try {


            model.setId(Integer.parseInt(cursor.getString(0)));
            model.setTitle(cursor.getString(1));
            model.setDescription(cursor.getString(2));
            model.setDate(cursor.getString(3));
            model.setTime(cursor.getString(4));


        } catch (Exception e) {

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return model;
    }


}
