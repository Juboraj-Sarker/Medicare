package com.juborajsarker.medicinealert.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.badoualy.datepicker.DatePickerTimeline;
import com.juborajsarker.medicinealert.R;
import com.juborajsarker.medicinealert.adapter.MedicineAdapter;
import com.juborajsarker.medicinealert.database.DatabaseHelper;
import com.juborajsarker.medicinealert.dataparser.GridSpacingItemDecoration;
import com.juborajsarker.medicinealert.model.MedicineModel;
import com.juborajsarker.medicinealert.model.StaticVariables;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


public class HomeFragment extends Fragment {

    View view;

    boolean allPermission;
    RecyclerView recyclerView;
    List<MedicineModel> medicineModelList;
    MedicineAdapter adapter;
    DatabaseHelper dbHelper;

    Calendar startDate;
    Calendar endDate;


    HorizontalCalendar horizontalCalendar;

    DatePickerTimeline timeline;



    public HomeFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        if (!allPermission){

            if (Build.VERSION.SDK_INT >= 23) {
                checkMultiplePermissions();
            }
        }



        setupCalender();


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        medicineModelList = new ArrayList<>();
        dbHelper = new DatabaseHelper(getContext());
        medicineModelList = dbHelper.getAllData("after_table");
        adapter = new MedicineAdapter(getContext(), medicineModelList, getActivity());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();












        return view;
    }

    private void setupCalender() {

        startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);




        horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .configure()
                .formatTopText("EEE")
                .showTopText(true)
                .showBottomText(false)
                .formatBottomText("MMM-yy")
                .end()
                .build();




        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                //do something
            }
        });


        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy) {

            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                return true;
            }
        });

    }


    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    private void checkMultiplePermissions() {

        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            List<String> permissionsList = new ArrayList<String>();

            if (!addPermission(permissionsList, Manifest.permission.CAMERA)) {
                permissionsNeeded.add("Camera");
            }

            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionsNeeded.add("Storage");
            }

            if (permissionsList.size() > 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        StaticVariables.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
        }
    }


    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23)

            if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);

                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case StaticVariables.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                    allPermission = true;

                    return;
                } else {
                    // Permission Denied
                    if (Build.VERSION.SDK_INT >= 23) {
                        Toast.makeText(
                                getContext(),
                                "This application cannot run without Camera and Storage " +
                                        "Permissions.\nYou may relaunch the app or allow permissions" +
                                        " from Application Settings",
                                Toast.LENGTH_LONG).show();

                        allPermission = false;


                        if (perms.get(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {


                        }


                        if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


                        }

                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
