package com.juborajsarker.medicare.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.juborajsarker.medicare.model.DoctorModel;

import java.util.ArrayList;
import java.util.List;

public class DoctorDatabase extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "doctor_manager";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "doctor_table";

    private static final String COLUMN_1 = "ID";
    private static final String COLUMN_2 = "DOCTOR_NAME";
    private static final String COLUMN_3 = "DOCTOR_PHONE";
    private static final String COLUMN_4 = "DOCTOR_SPECIALITY";
    private static final String COLUMN_5 = "DOCTOR_ADDRESS";
    private static final String COLUMN_6 = "DOCTOR_CHAMBER";
    private static final String COLUMN_7 = "DOCTOR_EMAIL";

    public DoctorDatabase(Context context){

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
                + COLUMN_5 + " TEXT,"
                + COLUMN_6 + " TEXT,"
                + COLUMN_7 + " TEXT" + ")";

        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertDoctor(DoctorModel doctorModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_2, doctorModel.getName());
        values.put(COLUMN_3, doctorModel.getPhoneNumber());
        values.put(COLUMN_4, doctorModel.getSpeciality());
        values.put(COLUMN_5, doctorModel.getAddress());
        values.put(COLUMN_6, doctorModel.getChamber());
        values.put(COLUMN_7, doctorModel.getEmail());

        db.insert(TABLE_NAME, null, values);
        db.close();

    }

    public int updateDoctor(DoctorModel doctorModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_2, doctorModel.getName());
        values.put(COLUMN_3, doctorModel.getPhoneNumber());
        values.put(COLUMN_4, doctorModel.getSpeciality());
        values.put(COLUMN_5, doctorModel.getAddress());
        values.put(COLUMN_6, doctorModel.getChamber());
        values.put(COLUMN_7, doctorModel.getEmail());

        return db.update(TABLE_NAME, values, COLUMN_1 + " = ?",
                new String[] { String.valueOf(doctorModel.getId()) });

    }


    public List<DoctorModel> getAllDoctor(){

        List<DoctorModel> doctorModelList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){

            do {

                DoctorModel model = new DoctorModel();
                model.setId(Integer.parseInt(cursor.getString(0)));
                model.setName(cursor.getString(1));
                model.setPhoneNumber(cursor.getString(2));
                model.setSpeciality(cursor.getString(3));
                model.setAddress(cursor.getString(4));
                model.setChamber(cursor.getString(5));
                model.setEmail(cursor.getString(6));

                doctorModelList.add(model);

            }while (cursor.moveToNext());
        }

        return doctorModelList;
    }


    public void deleteDoctor(DoctorModel doctorModel){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, COLUMN_1 + " = ?",
                new String[] { String.valueOf(doctorModel.getId()) });

        db.close();
    }


    public List<DoctorModel> getSearchedDoctor(String searchKeyword){

        List<DoctorModel> doctorModelList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE DOCTOR_NAME=?" ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { searchKeyword });

        if (cursor.moveToFirst()){

            do {

                DoctorModel model = new DoctorModel();
                model.setId(Integer.parseInt(cursor.getString(0)));
                model.setName(cursor.getString(1));
                model.setPhoneNumber(cursor.getString(2));
                model.setSpeciality(cursor.getString(3));
                model.setAddress(cursor.getString(4));
                model.setChamber(cursor.getString(5));
                model.setEmail(cursor.getString(6));

                doctorModelList.add(model);

            }while (cursor.moveToNext());
        }


        return doctorModelList;

    }

    public DoctorModel selectWithID(String id){

        DoctorModel doctorModel = new DoctorModel();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE ID=?" ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { id });

        if (cursor != null)
            cursor.moveToFirst();


        try {

            doctorModel.setId(Integer.parseInt(cursor.getString(0)));
            doctorModel.setName(cursor.getString(1));
            doctorModel.setPhoneNumber(cursor.getString(2));
            doctorModel.setSpeciality(cursor.getString(3));
            doctorModel.setAddress(cursor.getString(4));
            doctorModel.setChamber(cursor.getString(5));
            doctorModel.setEmail(cursor.getString(6));


        }catch (Exception e){

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return doctorModel;
    }
}


