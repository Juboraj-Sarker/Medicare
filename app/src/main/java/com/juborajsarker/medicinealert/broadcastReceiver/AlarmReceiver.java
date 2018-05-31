package com.juborajsarker.medicinealert.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.juborajsarker.medicinealert.activity.NotificationActivity;
import com.juborajsarker.medicinealert.notificationManager.NotificationHelper;
import com.juborajsarker.medicinealert.service.AlarmRing;

public class AlarmReceiver extends BroadcastReceiver {

    NotificationHelper helper;

    @Override
    public void onReceive(Context context, Intent intent) {



        String medName = intent.getStringExtra("medName");
        String time = intent.getStringExtra("time");
        String mealStatus = intent.getStringExtra("mealStatus");
        String type = intent.getStringExtra("medType");
        String med = intent.getStringExtra("med");

        String appointment = intent.getStringExtra("appointment");
        String doctorName = intent.getStringExtra("doctorName");
        String appTime = intent.getStringExtra("appTime");
        String location = intent.getStringExtra("location");



        helper = new NotificationHelper(context);

        int taken = intent.getIntExtra("taken", -5);
        int details = intent.getIntExtra("details", -7);
        int cancel = intent.getIntExtra("cancel", -9);


        int appOk = intent.getIntExtra("appOk", -88);
        int appCancel = intent.getIntExtra("appCancel", -99);

        int main = intent.getIntExtra("main", -100);


        if (main == 100){

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);


            helper.getManager().cancelAll();

            Intent notificationIntent = new Intent(context, NotificationActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(notificationIntent);


        }



        if (taken == 11){

            Toast.makeText(context, "Taken", Toast.LENGTH_SHORT).show();

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);


            helper.getManager().cancelAll();

        }


        if (cancel == 22){

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);

            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();

            helper.getManager().cancelAll();
        }


        if (details == 33){

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);

            Toast.makeText(context, "Details", Toast.LENGTH_SHORT).show();

            helper.getManager().cancelAll();

        }

        if (appOk == 88){

            Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);


            helper.getManager().cancelAll();
        }

        if (appCancel == 99){

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);

            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();

            helper.getManager().cancelAll();

        }






        if (  "true".equals(med) ){

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);

            if (context.stopService(stopIntent)){

                context.stopService(stopIntent);
            }


            Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            Intent startIntent = new Intent(context, AlarmRing.class);
            startIntent.putExtra("ringtone_uri", ""+ringtoneUri);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                context.startForegroundService(startIntent);

                showNotification(medName.toUpperCase() + " (" + type + ")",
                        "it's time to take " +medName +" at " + time + " (" + mealStatus + ")");


            }else {

                showNotification(medName.toUpperCase() + " (" + type + ")",
                        "it's time to take " +medName +" at " + time + " (" + mealStatus + ")");
                context.startService(startIntent);


            }



        }



        if ("true".equals(appointment)){

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);

            if (context.stopService(stopIntent)){

                context.stopService(stopIntent);
            }


            Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            Intent startIntent = new Intent(context, AlarmRing.class);
            startIntent.putExtra("ringtone_uri", ""+ringtoneUri);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                context.startForegroundService(startIntent);

                showNotificationForAppointment("Appointment today !!! ", "Get ready for appointment to "
                        + doctorName + " at " + appTime + " in" + location);



            }else {


                context.startService(startIntent);

                showNotificationForAppointment("Appointment today !!! ", "Get ready for appointment to "
                        + doctorName + " at " + appTime + " in " + location);


            }
        }











//        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
//        ringtone.play();




    }

    private void showNotification(String title, String message) {

        NotificationCompat.Builder notification = helper.getChannelNotification(title,message);
        helper.getManager().notify(1, notification.build());



    }

    private void showNotificationForAppointment(String title, String message) {

        NotificationCompat.Builder notification = helper.getAppointmentNotification(title,message);
        helper.getManager().notify(1, notification.build());



    }



}
