package com.juborajsarker.medicare.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.juborajsarker.medicare.R;
import com.juborajsarker.medicare.activity.medicines.AddMedicineActivity;
import com.juborajsarker.medicare.adapter.MedicineAdapter;
import com.juborajsarker.medicare.database.MedicineDatabase;
import com.juborajsarker.medicare.dataparser.DateCalculations;
import com.juborajsarker.medicare.dataparser.GridSpacingItemDecoration;
import com.juborajsarker.medicare.model.MedicineModel;
import com.juborajsarker.medicare.myInterface.StaticVariables;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


public class MedicineFragment extends Fragment {

    View view;

    FloatingActionButton fab;

    boolean allPermission;
    RecyclerView recyclerViewBeforeMeal, recyclerViewAfterMeal;
    List<MedicineModel> medicineModelListBeforeMeal, medicineModelListAfterMeal;
    MedicineAdapter adapter;
    MedicineDatabase dbHelper;

    Calendar startDate;
    Calendar endDate;


    HorizontalCalendar horizontalCalendar;
    ImageView leftIV, rightIV;
    TextView dateTV, beforeTV, afterTV;
    boolean firstStart = true;
    int position = 5;




    public MedicineFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_medicine, container, false);

        if (!allPermission){

            if (Build.VERSION.SDK_INT >= 23) {
                checkMultiplePermissions();
            }
        }


        setupCalender();
        init();




        return view;
    }

    private void init() {

        Date currentDate = Calendar.getInstance().getTime();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
        String searchQuery = df.format(currentDate);

        fab = (FloatingActionButton) view.findViewById(R.id.fab_add);

        leftIV = (ImageView) view.findViewById(R.id.leftIV);
        rightIV = (ImageView) view.findViewById(R.id.rightIV);
        dateTV = (TextView) view.findViewById(R.id.dateTV);
        beforeTV = (TextView) view.findViewById(R.id.beforeTV);
        afterTV = (TextView) view.findViewById(R.id.afterTV);


        recyclerViewBeforeMeal = (RecyclerView) view.findViewById(R.id.recyclerView_before_meal);
        recyclerViewAfterMeal = (RecyclerView) view.findViewById(R.id.recyclerView_after_meal);

        RecyclerView.LayoutManager layoutManagerBeforeMeal = new GridLayoutManager(getContext(), 1);
        recyclerViewBeforeMeal.setLayoutManager(layoutManagerBeforeMeal);
        recyclerViewBeforeMeal.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(2), true));
        recyclerViewBeforeMeal.setItemAnimator(new DefaultItemAnimator());


        RecyclerView.LayoutManager layoutManagerAfterMeal = new GridLayoutManager(getContext(), 1);
        recyclerViewAfterMeal.setLayoutManager(layoutManagerAfterMeal);
        recyclerViewAfterMeal.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(2), true));
        recyclerViewAfterMeal.setItemAnimator(new DefaultItemAnimator());

        medicineModelListBeforeMeal = new ArrayList<>();
        medicineModelListBeforeMeal.clear();
        dbHelper = new MedicineDatabase(getContext());
        medicineModelListBeforeMeal = dbHelper.getSelectedList(searchQuery, "before_table");
        adapter = new MedicineAdapter(getContext(), medicineModelListBeforeMeal, getActivity(),
                "before_table", recyclerViewBeforeMeal , adapter, searchQuery);
        adapter.notifyDataSetChanged();
        recyclerViewBeforeMeal.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        beforeTV.setText("Before Meal (" + medicineModelListBeforeMeal.size() + ")");



        medicineModelListAfterMeal = new ArrayList<>();
        medicineModelListAfterMeal.clear();
        dbHelper = new MedicineDatabase(getContext());
        medicineModelListAfterMeal = dbHelper.getSelectedList(searchQuery, "after_table");
        adapter = new MedicineAdapter(getContext(), medicineModelListAfterMeal, getActivity(),
                "after_table", recyclerViewAfterMeal, adapter, searchQuery);
        adapter.notifyDataSetChanged();
        recyclerViewAfterMeal.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        afterTV.setText("After Meal ("  + medicineModelListAfterMeal.size() + ")");



        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM - yyyy");
        String formattedCurrentDate = sdf.format(currentDate);

        int sizeBefore = medicineModelListBeforeMeal.size();
        int sizeAfter = medicineModelListAfterMeal.size();
        int sum = sizeBefore + sizeAfter;

        dateTV.setText("Today " +formattedCurrentDate + " (total " + sum + " medicine)");


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), AddMedicineActivity.class));
            }
        });



        leftIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setupCurrentDate();

            }
        });

        rightIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setupCurrentDate();

            }
        });


    }

    private void setupCurrentDate() {


        horizontalCalendar.goToday(true);


    }

    private void setupCalender() {

        startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -3);

        endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 3);




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

                imageViewIssue(date, position);
                prepareDataForRecyclerView(date);
                setTextViewDate(date);


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

    private void prepareDataForRecyclerView(Calendar date) {

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
        Date selectedDate = date.getTime();
        String searchQuery = df.format(selectedDate);



        medicineModelListBeforeMeal = new ArrayList<>();
        medicineModelListBeforeMeal.clear();
        dbHelper = new MedicineDatabase(getContext());
        medicineModelListBeforeMeal = dbHelper.getSelectedList(searchQuery, "before_table");
        adapter = new MedicineAdapter(getContext(), medicineModelListBeforeMeal, getActivity(),
                "before_table", recyclerViewBeforeMeal, adapter, searchQuery);
        adapter.notifyDataSetChanged();
        recyclerViewBeforeMeal.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        beforeTV.setText("Before Meal ("  + medicineModelListBeforeMeal.size() + ")");




        medicineModelListAfterMeal = new ArrayList<>();
        medicineModelListAfterMeal.clear();
        dbHelper = new MedicineDatabase(getContext());
        medicineModelListAfterMeal = dbHelper.getSelectedList(searchQuery, "after_table");
        adapter = new MedicineAdapter(getContext(), medicineModelListAfterMeal, getActivity(),
                "after_table", recyclerViewAfterMeal, adapter, searchQuery);
        adapter.notifyDataSetChanged();
        recyclerViewAfterMeal.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        afterTV.setText("After Meal ("  + medicineModelListAfterMeal.size() + ")");

    }

    private void imageViewIssue(Calendar date, int position) {

        Date selectedDate = date.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM - yyyy");
        String formattedDate = sdf.format(selectedDate);

        String result = new DateCalculations().compareDate(formattedDate);

        if (result.equals("big")){

            leftIV.setVisibility(View.VISIBLE);
            rightIV.setVisibility(View.GONE);


        }else if (result.equals("small")){

            rightIV.setVisibility(View.VISIBLE);
            leftIV.setVisibility(View.GONE);

        }

    }

    private void setTextViewDate(Calendar date) {

        Date currentDate = Calendar.getInstance().getTime();
        Date myDate = date.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM - yyyy");
        SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM - yyyy");

        String formattedCurrentDate = sdf.format(currentDate);
        String formattedDate = df.format(myDate);

        if (formattedCurrentDate.equals(formattedDate)){

            int sizeBefore = medicineModelListBeforeMeal.size();
            int sizeAfter = medicineModelListAfterMeal.size();
            int sum = sizeBefore + sizeAfter;

            dateTV.setText("Today " + formattedCurrentDate + " (total " + sum + " medicine)" );

            leftIV.setVisibility(View.GONE);
            rightIV.setVisibility(View.GONE);

        }else {



            String addDate = new DateCalculations().addForHorizontalCalender(formattedCurrentDate, "1");
            String subDate = new DateCalculations().subForHorizontalCalender(formattedCurrentDate, "1");

            if (addDate.equals(formattedDate)){

                int sizeBefore = medicineModelListBeforeMeal.size();
                int sizeAfter = medicineModelListAfterMeal.size();
                int sum = sizeBefore + sizeAfter;

                dateTV.setText("Tomorrow " + formattedDate + " (total " + sum + " medicine)");

            }else if (subDate.equals(formattedDate)){

                int sizeBefore = medicineModelListBeforeMeal.size();
                int sizeAfter = medicineModelListAfterMeal.size();
                int sum = sizeBefore + sizeAfter;

                dateTV.setText("Yesterday " + formattedDate + " (total " + sum + " medicine)");

            }else {

                int sizeBefore = medicineModelListBeforeMeal.size();
                int sizeAfter = medicineModelListAfterMeal.size();
                int sum = sizeBefore + sizeAfter;

                dateTV.setText(formattedDate + " (total " + sum + " medicine)");
            }




        }

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
