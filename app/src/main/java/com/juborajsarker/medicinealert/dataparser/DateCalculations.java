package com.juborajsarker.medicinealert.dataparser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateCalculations {

    String daysName;

    public String addDays(String fromDate, String noOfdays) {



        Calendar c = Calendar.getInstance();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);

        try {
            Date myDate = df.parse(fromDate.trim());
            c.setTime(myDate);
            c.add(Calendar.DATE, Integer.parseInt(noOfdays));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String toDate = df.format(c.getTime());
        return toDate;

    }

    public   String subtractDays(String fromDate, String noOfdays) {

        Calendar c = Calendar.getInstance();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
        try {
            Date myDate = df.parse(fromDate.trim());
            c.setTime(myDate);
            c.add(Calendar.DATE, (Integer.parseInt(noOfdays)*-1));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String toDate = df.format(c.getTime());
        return toDate;

    }


    public String daysNameOfWeek(String inputDate){

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
        try {

            Date date = df.parse(inputDate.trim());
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
             daysName  = outFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return daysName;
    }
}
