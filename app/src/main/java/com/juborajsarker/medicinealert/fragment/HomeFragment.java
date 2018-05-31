package com.juborajsarker.medicinealert.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juborajsarker.medicinealert.R;
import com.juborajsarker.medicinealert.activity.MainActivity;
import com.juborajsarker.medicinealert.database.AppointmentDatabase;
import com.juborajsarker.medicinealert.database.MedicineDatabase;
import com.juborajsarker.medicinealert.dataparser.DateCalculations;
import com.juborajsarker.medicinealert.model.AppointmentModel;
import com.juborajsarker.medicinealert.model.MedicineModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


public class HomeFragment extends Fragment {

    View view;
    FragmentManager fragmentManager;
    Calendar startDate;
    Calendar endDate;


    HorizontalCalendar horizontalCalendar;
    ImageView leftIV, rightIV;
    TextView dateTV, totalMedicineTV, beforeMealTV, afterMealTV, totalAppointmentTV;
    CardView medicineCV, appointmentCV;


    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

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
        totalMedicineTV = (TextView) view.findViewById(R.id.total_medicine_TV);
        totalAppointmentTV = (TextView) view.findViewById(R.id.total_appointment_TV);
        beforeMealTV = (TextView) view.findViewById(R.id.total_before_meal_TV);
        afterMealTV = (TextView) view.findViewById(R.id.total_after_meal_TV);

        medicineCV = (CardView) view.findViewById(R.id.medicine_CV);
        appointmentCV = (CardView) view.findViewById(R.id.appointment_CV);

        medicineCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("open", "medicine");
                startActivity(intent);
                getActivity().finish();



            }
        });


        appointmentCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("open", "appointment");
                startActivity(intent);
                getActivity().finish();

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


        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM - yyyy");
        String formattedCurrentDate = sdf.format(currentDate);
        dateTV.setText("Today " +formattedCurrentDate );


        prepareForView(searchQuery);



    }

    private void prepareForView(String search) {

        AppointmentDatabase appointmentDatabase = new AppointmentDatabase(getContext());
        MedicineDatabase medicineDatabase = new MedicineDatabase(getContext());

        List<MedicineModel> beforeMedicineList = medicineDatabase.getSelectedList(search, "before_table");
        List<MedicineModel> afterModelList = medicineDatabase.getSelectedList(search, "after_table");

        List<AppointmentModel> appointmentModelList = appointmentDatabase.getSelectedAppointment(search);

        int beforeSize = beforeMedicineList.size();
        int afterSize = afterModelList.size();
        int appointSize = appointmentModelList.size();
        int totalMedicine = beforeSize + afterSize;

        beforeMealTV.setText("Before meal " + beforeSize + " medicine");
        afterMealTV.setText("After Meal " + afterSize + " medicine");

        totalAppointmentTV.setText("Total " + appointSize + " Appointment");
        totalMedicineTV.setText("Total " + totalMedicine + " Medicine");
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

                Date currentDate = date.getTime();
                DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
                String searchQuery = df.format(currentDate);

                imageViewIssue(date, position);
                setTextViewDate(date);
                prepareForView(searchQuery);



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


    private void setupCurrentDate() {


        horizontalCalendar.goToday(true);


    }


}
