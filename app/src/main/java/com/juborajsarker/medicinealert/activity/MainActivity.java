package com.juborajsarker.medicinealert.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.juborajsarker.medicinealert.R;
import com.juborajsarker.medicinealert.fragment.AddMedicineFragment;
import com.juborajsarker.medicinealert.fragment.HomeFragment;
import com.juborajsarker.medicinealert.fragment.MoreFragment;
import com.juborajsarker.medicinealert.fragment.NotificationFragment;
import com.juborajsarker.medicinealert.java_class.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    

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
                case R.id.navigation_addMedicine:{

                    for(Fragment fragment:getSupportFragmentManager().getFragments()){

                        if(fragment!=null) {

                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        }
                    }


                    fragmentManager.beginTransaction().add(R.id.container, new AddMedicineFragment()).commit();
                    setTitle("Add Medicine");

                    return true;

                }
                case R.id.navigation_notifications:{

                    for(Fragment fragment:getSupportFragmentManager().getFragments()){

                        if(fragment!=null) {

                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        }
                    }


                    fragmentManager.beginTransaction().add(R.id.container, new NotificationFragment()).commit();
                    setTitle("Notification");

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






        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        setTitle("Home");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
