package com.juborajsarker.medicinealert.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.juborajsarker.medicinealert.R;
import com.juborajsarker.medicinealert.fragment.AppointmentFragment;
import com.juborajsarker.medicinealert.fragment.HomeFragment;
import com.juborajsarker.medicinealert.fragment.MedicineFragment;
import com.juborajsarker.medicinealert.fragment.MoreFragment;
import com.juborajsarker.medicinealert.java_class.BottomNavigationViewHelper;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    boolean doubleBackToExitPressedOnce = false;
    

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:{


                    for(Fragment fragment:getSupportFragmentManager().getFragments()){

                        if(fragment!=null) {

                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        }
                    }


                    fragmentManager.beginTransaction().add(R.id.container, new HomeFragment()).commit();
                    setTitle("Home");

                    return true;

                }
                case R.id.navigation_medicine:{

                    for(Fragment fragment:getSupportFragmentManager().getFragments()){

                        if(fragment!=null) {

                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        }
                    }


                    fragmentManager.beginTransaction().add(R.id.container, new MedicineFragment()).commit();
                    setTitle("Medicine");

                    return true;

                }
                case R.id.navigation_appointment:{

                    for(Fragment fragment:getSupportFragmentManager().getFragments()){

                        if(fragment!=null) {

                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        }
                    }


                    fragmentManager.beginTransaction().add(R.id.container, new AppointmentFragment()).commit();
                    setTitle("Appointments");

                    return true;

                }

                case R.id.navigation_more:{

                    for(Fragment fragment:getSupportFragmentManager().getFragments()){

                        if(fragment!=null) {

                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        }
                    }


                    fragmentManager.beginTransaction().add(R.id.container, new MoreFragment()).commit();
                    setTitle("More");

                    return true;

                }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){

            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        Intent intent = getIntent();
        String whichOpen = intent.getStringExtra("open");

        if ("appointment".equals(whichOpen)){

            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, new AppointmentFragment()).commit();
            setTitle("Appointments");

            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
            BottomNavigationViewHelper.disableShiftMode(navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            navigation.setSelectedItemId(R.id.navigation_appointment);

        }else if ("medicine".equals(whichOpen)){


            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, new MedicineFragment()).commit();
            setTitle("Medicine");

            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
            BottomNavigationViewHelper.disableShiftMode(navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            navigation.setSelectedItemId(R.id.navigation_medicine);


        }else {

            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, new HomeFragment()).commit();
            setTitle("Home");

            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
            BottomNavigationViewHelper.disableShiftMode(navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("Thanks for using my app")
                        .setMessage("Are you sure you want to really exit?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                AppExit();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();



            default:
                return super.onOptionsItemSelected(item);
        }


    }


    public void AppExit() {

        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }



    public void rateApp(MenuItem item) {

        rateApps();

    }


    public void shareApp(MenuItem item) {

        ApplicationInfo app = getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.createChooser(intent,"Age Calculator");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
        startActivity(Intent.createChooser(intent, "share Age Calculator using"));

    }





    public void moreApps(MenuItem item) {


        Intent intent;
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=6155570899607409709&hl"));
        startActivity(intent);

    }



    public void goToAboutActivity(MenuItem item) {


        startActivity(new Intent(MainActivity.this, AboutActivity.class));
    }


    public void rateApps() {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }


    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
