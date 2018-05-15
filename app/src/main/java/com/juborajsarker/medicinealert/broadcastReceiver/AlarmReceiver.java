package com.juborajsarker.medicinealert.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.juborajsarker.medicinealert.notificationManager.NotificationHelper;
import com.juborajsarker.medicinealert.service.AlarmRing;

import java.io.File;

public class AlarmReceiver extends BroadcastReceiver {
    NotificationHelper helper;


    @Override
    public void onReceive(Context context, Intent intent) {



        String medName = intent.getStringExtra("medName");
        String time = intent.getStringExtra("time");
        String mealStatus = intent.getStringExtra("mealStatus");
        String imagePath = intent.getStringExtra("imagePath");
        String type = intent.getStringExtra("medType");

        helper = new NotificationHelper(context);

        int taken = intent.getIntExtra("taken", -5);
        int cancel = intent.getIntExtra("cancel", -9);

        if (taken == 11){

            Toast.makeText(context, "Taken", Toast.LENGTH_SHORT).show();

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);


            helper.getManager().cancelAll();

        } if (cancel == 22){

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);

            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();

            helper.getManager().cancelAll();
        }


        if ((taken == -5) && (cancel == -9) ){

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


            }else {

                context.startService(startIntent);
            }


            sendOnChannel(medName.toUpperCase() + " (" + type + ")",
                    "it's time to take " +medName +" at " + time + " (" + mealStatus + ")" , getBitmap(imagePath));
        }











//        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
//        ringtone.play();




    }

    private void sendOnChannel(String title, String message, Bitmap bitmap) {

        NotificationCompat.Builder nb = helper.getChannelNotification(title,message, bitmap).setAutoCancel(true);
        helper.getManager().notify(1, nb.build());



    }



    public Bitmap getBitmap(String fileName) {

        Bitmap bMapScaled = null;

        File imageFile = new File(Environment.getExternalStorageDirectory()
                + File.separator + "MedicineAlert" + "/" + fileName);

        if (imageFile.exists()) {

             Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
             bMapScaled = Bitmap.createScaledBitmap(bitmap, 330, 330, true);


        }

        return bMapScaled;
    }



}
