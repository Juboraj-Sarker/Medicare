package com.juborajsarker.medicinealert.broadcastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.juborajsarker.medicinealert.database.DatabaseHelper;
import com.juborajsarker.medicinealert.model.MedicineModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SampleBootReceiver extends BroadcastReceiver {

    int requestCode = 0;
    public SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            sharedPreferences = context.getSharedPreferences("alarmRequestCode", MODE_PRIVATE);

            List<MedicineModel> beforeList = new ArrayList<>();
            List<MedicineModel> afterList = new ArrayList<>();
            DatabaseHelper dbHelper = new DatabaseHelper(context);

            beforeList = dbHelper.getAllData("before_table");
            afterList = dbHelper.getAllData("after_table");

            for (int i = 0; i< beforeList.size(); i++){

                MedicineModel medicineModel = beforeList.get(i);
                String date = medicineModel.getDate();
                String firstSlotTime = medicineModel.getFirstSlotTime();
                String secondSlotTime = medicineModel.getSecondSlotTime();
                String thirdSlotTime = medicineModel.getThirdSlotTime();


                String combine = date + " " + firstSlotTime;
                setAlarm(combine, context, medicineModel);

                if (!secondSlotTime.equals("null")){

                    String combine2 = date + " " + secondSlotTime;
                    setAlarm(combine2, context, medicineModel);
                }

                if (!thirdSlotTime.equals("null")){

                    String combine3 = date + " " + thirdSlotTime;
                    setAlarm(combine3, context, medicineModel);
                }

            }









            for (int i = 0; i< afterList.size(); i++){

                MedicineModel medicineModel = afterList.get(i);
                String date = medicineModel.getDate();
                String firstSlotTime = medicineModel.getFirstSlotTime();
                String secondSlotTime = medicineModel.getSecondSlotTime();
                String thirdSlotTime = medicineModel.getThirdSlotTime();


                String combine = date + " " + firstSlotTime;
                setAlarm(combine, context, medicineModel);

                if (!secondSlotTime.equals("null")){

                    String combine2 = date + " " + secondSlotTime;
                    setAlarm(combine2, context, medicineModel);
                }

                if (!thirdSlotTime.equals("null")){

                    String combine3 = date + " " + thirdSlotTime;
                    setAlarm(combine3, context, medicineModel);

                }

            }



        }

    }

    private void setAlarm(String combine, Context context, MedicineModel medicineModel) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm aaa");

        Calendar calendar = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();

        try {
            calendar.setTime(sdf.parse(combine));

            int dateForAlarm = calendar.get(Calendar.DAY_OF_MONTH);
            int monthForAlarm = calendar.get(Calendar.MONTH);
            int yearForAlarm = calendar.get(Calendar.YEAR);
            int hourForAlarm = calendar.get(Calendar.HOUR_OF_DAY);
            int minuteForAlarm = calendar.get(Calendar.MINUTE);
            int secondForAlarm = 0;

            cal.set(yearForAlarm, monthForAlarm, dateForAlarm, hourForAlarm, minuteForAlarm, secondForAlarm);


            Log.d("calender",
                    "calender" +"\n"+
                            "Date " +dateForAlarm +"\n"+
                            "Month " + monthForAlarm +"\n"+
                            "Year " + yearForAlarm + "\n"+
                            "Hour: " + hourForAlarm +"\n" +
                            "Minute " + minuteForAlarm + "\n" +
                            "TimeMills " + cal.getTimeInMillis() + "\n" +
                            "Current TimeMills " + Calendar.getInstance().getTimeInMillis()+"\n"+
                            "Combine " + combine);


        } catch (ParseException e) {
            e.printStackTrace();
        }



        if (Calendar.getInstance().getTimeInMillis() >= cal.getTimeInMillis()){

            Log.d("before", "before current time");

        }else {

            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("medName", medicineModel.getMedicineName());
            intent.putExtra("imagePath", medicineModel.getImagePath());
            intent.putExtra("mealStatus", medicineModel.getMedicineMeal());
            intent.putExtra("time", combine);
            intent.putExtra("medType", medicineModel.getMedicineType());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);



            requestCode++;


            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("requestCodeValue", requestCode);
            editor.commit();
        }


    }
}
