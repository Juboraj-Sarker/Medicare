package com.juborajsarker.medicinealert.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juborajsarker.medicinealert.R;
import com.juborajsarker.medicinealert.activity.AddAppointmentActivity;
import com.juborajsarker.medicinealert.dataparser.DateCalculations;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


public class AppointmentFragment extends Fragment {

    View view;

    Calendar startDate;
    Calendar endDate;


    HorizontalCalendar horizontalCalendar;
    ImageView leftIV, rightIV;
    TextView dateTV, appointmentTV;
    FloatingActionButton fabAdd;
    RecyclerView recyclerView;


    public AppointmentFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_appointment, container, false);

        init();
        setupCalender();

        return view;
    }

    private void init() {

        Date currentDate = Calendar.getInstance().getTime();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
        String searchQuery = df.format(currentDate);

        leftIV = (ImageView) view.findViewById(R.id.leftIV);
        rightIV = (ImageView) view.findViewById(R.id.rightIV);
        dateTV = (TextView) view.findViewById(R.id.dateTV);
        appointmentTV = (TextView) view.findViewById(R.id.appointment_TV);
        recyclerView = (RecyclerView) view.findViewById(R.id.appointment_RV);
        fabAdd = (FloatingActionButton) view.findViewById(R.id.fab_add);



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

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), AddAppointmentActivity.class));
            }
        });


        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM - yyyy");
        String formattedCurrentDate = sdf.format(currentDate);

        dateTV.setText("Today " +formattedCurrentDate );
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

            dateTV.setText("Today " + formattedCurrentDate );

            leftIV.setVisibility(View.GONE);
            rightIV.setVisibility(View.GONE);

        }else {



            String addDate = new DateCalculations().addForHorizontalCalender(formattedCurrentDate, "1");
            String subDate = new DateCalculations().subForHorizontalCalender(formattedCurrentDate, "1");

            if (addDate.equals(formattedDate)){


                dateTV.setText("Tomorrow " + formattedDate);

            }else if (subDate.equals(formattedDate)){



                dateTV.setText("Yesterday " + formattedDate );

            }else {

                dateTV.setText(formattedDate );
            }




        }

    }

}
