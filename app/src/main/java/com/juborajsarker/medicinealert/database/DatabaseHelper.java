package com.juborajsarker.medicinealert.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.juborajsarker.medicinealert.model.MedicineModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    Context context;

    private static final String DATABASE_NAME = "passwordManager";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "medicine_table";

    private static final String COLUMN_0 = "ID";
    private static final String COLUMN_1 = "DATE";
    private static final String COLUMN_2 = "MEDICINE_NAME";
    private static final String COLUMN_3 = "IMAGE_PATH";
    private static final String COLUMN_4 = "NO_OF_SLOT";
    private static final String COLUMN_5 = "FIRST_SLOT";
    private static final String COLUMN_6 = "SECOND_SLOT";
    private static final String COLUMN_7 = "THIRD_SLOT";
    private static final String COLUMN_8 = "NUMBER_OF_DAYS";
    private static final String COLUMN_9 = "IS_EVERYDAY";
    private static final String COLUMN_10 = "IS_SPECIFIC_DAYS_OF_WEEK";
    private static final String COLUMN_11 = "IS_DAYS_INTERVAL";
    private static final String COLUMN_12 = "DAYS_NAME_OF_WEEK";
    private static final String COLUMN_13 = "DAYS_iNTERVAL";
    private static final String COLUMN_14 = "START_DATE";
    private static final String COLUMN_15 = "STATUS";


    public DatabaseHelper(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_QUERY = "CREATE TABLE "
                + TABLE_NAME + "("
                + COLUMN_0 + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + COLUMN_1 + " TEXT,"
                + COLUMN_2 + " TEXT,"
                + COLUMN_3 + " TEXT,"
                + COLUMN_4 + " INTEGER,"
                + COLUMN_5 + " TEXT,"
                + COLUMN_6 + " TEXT,"
                + COLUMN_7 + " TEXT,"
                + COLUMN_8 + " INTEGER,"
                + COLUMN_9 + " BOOLEAN,"
                + COLUMN_10 + " BOOLEAN,"
                + COLUMN_11 + " BOOLEAN,"
                + COLUMN_12 + " TEXT,"
                + COLUMN_13 + " INTEGER,"
                + COLUMN_14 + " TEXT,"
                + COLUMN_15 + " TEXT" + ")";

        db.execSQL(CREATE_TABLE_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }



    public void insertData(MedicineModel medicineModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_1, medicineModel.getDate());
        values.put(COLUMN_2, medicineModel.getMedicineName());
        values.put(COLUMN_3, medicineModel.getImagePath());
        values.put(COLUMN_4, medicineModel.getNumberOfSlot());
        values.put(COLUMN_5, medicineModel.getFirstSlotTime());
        values.put(COLUMN_6, medicineModel.getSecondSlotTime());
        values.put(COLUMN_7, medicineModel.getThirdSlotTime());
        values.put(COLUMN_8, medicineModel.getNumberOfDays());
        values.put(COLUMN_9, medicineModel.isEveryday());
        values.put(COLUMN_10, medicineModel.isSpecificDaysOfWeek());
        values.put(COLUMN_11, medicineModel.isDaysInterval());
        values.put(COLUMN_12, medicineModel.getDaysNameOfWeek());
        values.put(COLUMN_13, medicineModel.getDaysInterval());
        values.put(COLUMN_14, medicineModel.getStartDate());
        values.put(COLUMN_15, medicineModel.getStatus());


        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    public MedicineModel getSingleMedicine (String date){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[] {
                        COLUMN_1,
                        COLUMN_2,
                        COLUMN_3,},
                COLUMN_1 + "=?",
                new String[] { date  }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();


        MedicineModel medicineModel = new MedicineModel();

        try {
            medicineModel.setId(Integer.parseInt(cursor.getString(0)));
            medicineModel.setDate(cursor.getString(1));
            medicineModel.setMedicineName(cursor.getString(2));
            medicineModel.setImagePath(cursor.getString(3));
            medicineModel.setNumberOfSlot(Integer.parseInt(cursor.getString(4)));
            medicineModel.setFirstSlotTime(cursor.getString(5));
            medicineModel.setSecondSlotTime(cursor.getString(6));
            medicineModel.setThirdSlotTime(cursor.getString(7));
            medicineModel.setNumberOfDays(Integer.parseInt(cursor.getString(8)));
            medicineModel.setEveryday(Boolean.parseBoolean(cursor.getString(9)));
            medicineModel.setSpecificDaysOfWeek(Boolean.parseBoolean(cursor.getString(10)));
            medicineModel.setDaysInterval(Boolean.parseBoolean(cursor.getString(11)));
            medicineModel.setDaysNameOfWeek(cursor.getString(12));
            medicineModel.setDaysInterval(Integer.parseInt(cursor.getString(13)));
            medicineModel.setStatus(cursor.getString(14));
            medicineModel.setStartDate(cursor.getString(15));

        }catch (Exception e){

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }




        return medicineModel;
    }


    public List<MedicineModel> getAllData (){

        List<MedicineModel> medicineModelList = new ArrayList<MedicineModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){

            do {

                MedicineModel medicineModel = new MedicineModel();
                medicineModel.setId(Integer.parseInt(cursor.getString(0)));
                medicineModel.setDate(cursor.getString(1));
                medicineModel.setMedicineName(cursor.getString(2));
                medicineModel.setImagePath(cursor.getString(3));
                medicineModel.setNumberOfSlot(Integer.parseInt(cursor.getString(4)));
                medicineModel.setFirstSlotTime(cursor.getString(5));
                medicineModel.setSecondSlotTime(cursor.getString(6));
                medicineModel.setThirdSlotTime(cursor.getString(7));
                medicineModel.setNumberOfDays(Integer.parseInt(cursor.getString(8)));
                medicineModel.setEveryday(Boolean.parseBoolean(cursor.getString(9)));
                medicineModel.setSpecificDaysOfWeek(Boolean.parseBoolean(cursor.getString(10)));
                medicineModel.setDaysInterval(Boolean.parseBoolean(cursor.getString(11)));
                medicineModel.setDaysNameOfWeek(cursor.getString(12));
                medicineModel.setDaysInterval(Integer.parseInt(cursor.getString(13)));
                medicineModel.setStatus(cursor.getString(14));
                medicineModel.setStartDate(cursor.getString(15));

                medicineModelList.add(medicineModel);

            }while (cursor.moveToNext());
        }



        return medicineModelList;
    }


    public int updateData (MedicineModel medicineModel){


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_1, medicineModel.getDate());
        values.put(COLUMN_2, medicineModel.getMedicineName());
        values.put(COLUMN_3, medicineModel.getImagePath());
        values.put(COLUMN_4, medicineModel.getNumberOfSlot());
        values.put(COLUMN_5, medicineModel.getFirstSlotTime());
        values.put(COLUMN_6, medicineModel.getSecondSlotTime());
        values.put(COLUMN_7, medicineModel.getThirdSlotTime());
        values.put(COLUMN_8, medicineModel.getNumberOfDays());
        values.put(COLUMN_9, medicineModel.isEveryday());
        values.put(COLUMN_10, medicineModel.isSpecificDaysOfWeek());
        values.put(COLUMN_11, medicineModel.isDaysInterval());
        values.put(COLUMN_12, medicineModel.getDaysNameOfWeek());
        values.put(COLUMN_13, medicineModel.getDaysInterval());
        values.put(COLUMN_14, medicineModel.getStartDate());
        values.put(COLUMN_15, medicineModel.getStatus());




        return db.update(TABLE_NAME, values, COLUMN_0 + " = ?",
                new String[] { String.valueOf(medicineModel.getId()) });
    }


    public void deleteData(MedicineModel medicineModel) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, COLUMN_0 + " = ?",
                new String[] { String.valueOf(medicineModel.getId()) });

        db.close();
    }
}
