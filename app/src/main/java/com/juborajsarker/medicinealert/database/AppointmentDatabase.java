package com.juborajsarker.medicinealert.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.juborajsarker.medicinealert.model.AppointmentModel;

import java.util.ArrayList;
import java.util.List;

public class AppointmentDatabase extends SQLiteOpenHelper {

    Context context;


    private static final String DATABASE_NAME = "appointment_manager";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAME = "appointment_table";

    private static final String COLUMN_1 = "ID";
    private static final String COLUMN_2 = "TITLE";
    private static final String COLUMN_3 = "DOCTOR_NAME";
    private static final String COLUMN_4 = "DOCTOR_SPECIALITY";
    private static final String COLUMN_5 = "DATE";
    private static final String COLUMN_6 = "TIME";
    private static final String COLUMN_7 = "REMEMBER_BEFORE";
    private static final String COLUMN_8 = "REMEMBER_BEFORE_TIME_IN_MILLS";
    private static final String COLUMN_9 = "LOCATION";
    private static final String COLUMN_10 = "NOTES";
    private static final String COLUMN_11 = "REQUEST_CODE";


    public AppointmentDatabase(Context context){

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
                + COLUMN_7 + " TEXT,"
                + COLUMN_8 + " INTEGER,"
                + COLUMN_9 + " TEXT,"
                + COLUMN_10 + " TEXT,"
                + COLUMN_11 + " INTEGER" + ")";

        db.execSQL(CREATE_TABLE_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public void insertAppointment(AppointmentModel appointmentModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_2, appointmentModel.getAppointmentTitle());
        values.put(COLUMN_3, appointmentModel.getDoctorName());
        values.put(COLUMN_4, appointmentModel.getDoctorSpeciality());
        values.put(COLUMN_5, appointmentModel.getDate());
        values.put(COLUMN_6, appointmentModel.getTime());
        values.put(COLUMN_7, appointmentModel.getRememberBefore());
        values.put(COLUMN_8, appointmentModel.getRememberBeforeTimeInMills());
        values.put(COLUMN_9, appointmentModel.getLocation());
        values.put(COLUMN_10, appointmentModel.getNotes());
        values.put(COLUMN_11, appointmentModel.getRequestCode());

        db.insert(TABLE_NAME, null, values);
        db.close();

    }

    public int updateAppointment(AppointmentModel appointmentModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_2, appointmentModel.getAppointmentTitle());
        values.put(COLUMN_3, appointmentModel.getDoctorName());
        values.put(COLUMN_4, appointmentModel.getDoctorSpeciality());
        values.put(COLUMN_5, appointmentModel.getDate());
        values.put(COLUMN_6, appointmentModel.getTime());
        values.put(COLUMN_7, appointmentModel.getRememberBefore());
        values.put(COLUMN_8, appointmentModel.getRememberBeforeTimeInMills());
        values.put(COLUMN_9, appointmentModel.getLocation());
        values.put(COLUMN_10, appointmentModel.getNotes());
        values.put(COLUMN_11, appointmentModel.getRequestCode());

        return db.update(TABLE_NAME, values, COLUMN_1 + " = ?",
                new String[] { String.valueOf(appointmentModel.getId()) });

    }

    public List<AppointmentModel> getAllAppointment(){

        List<AppointmentModel> appointmentModelList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){

            do {

                AppointmentModel model = new AppointmentModel();
                model.setId(Integer.parseInt(cursor.getString(0)));
                model.setAppointmentTitle(cursor.getString(1));
                model.setDoctorName(cursor.getString(2));
                model.setDoctorSpeciality(cursor.getString(3));
                model.setDate(cursor.getString(4));
                model.setTime(cursor.getString(5));
                model.setRememberBefore(cursor.getString(6));
                model.setRememberBeforeTimeInMills(Long.parseLong(cursor.getString(7)));
                model.setLocation(cursor.getString(8));
                model.setNotes(cursor.getString(9));
                model.setRequestCode(Integer.parseInt(cursor.getString(10)));


                appointmentModelList.add(model);

            }while (cursor.moveToNext());
        }


        return  appointmentModelList;
    }


    public void deleteAppointment(AppointmentModel appointmentModel){


        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, COLUMN_1 + " = ?",
                new String[] { String.valueOf(appointmentModel.getId()) });

        db.close();
    }


    public List<AppointmentModel> getSelectedAppointment(String searchKeyword){

        List<AppointmentModel> appointmentModelList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE DATE=?" ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { searchKeyword });

        if (cursor.moveToFirst()){

            do {

                AppointmentModel model = new AppointmentModel();
                model.setId(Integer.parseInt(cursor.getString(0)));
                model.setAppointmentTitle(cursor.getString(1));
                model.setDoctorName(cursor.getString(2));
                model.setDoctorSpeciality(cursor.getString(3));
                model.setDate(cursor.getString(4));
                model.setTime(cursor.getString(5));
                model.setRememberBefore(cursor.getString(6));
                model.setRememberBeforeTimeInMills(Long.parseLong(cursor.getString(7)));
                model.setLocation(cursor.getString(8));
                model.setNotes(cursor.getString(9));
                model.setRequestCode(Integer.parseInt(cursor.getString(10)));


                appointmentModelList.add(model);

            }while (cursor.moveToNext());
        }

        return appointmentModelList;
    }


    public AppointmentModel selectWithID (String id){

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE ID=?" ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { id });

        if (cursor != null)
            cursor.moveToFirst();

        AppointmentModel model = new AppointmentModel();

        try {

            model.setId(Integer.parseInt(cursor.getString(0)));
            model.setAppointmentTitle(cursor.getString(1));
            model.setDoctorName(cursor.getString(2));
            model.setDoctorSpeciality(cursor.getString(3));
            model.setDate(cursor.getString(4));
            model.setTime(cursor.getString(5));
            model.setRememberBefore(cursor.getString(6));
            model.setRememberBeforeTimeInMills(Long.parseLong(cursor.getString(7)));
            model.setLocation(cursor.getString(8));
            model.setNotes(cursor.getString(9));
            model.setRequestCode(Integer.parseInt(cursor.getString(10)));


        }catch (Exception e){

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return model;

    }
}
