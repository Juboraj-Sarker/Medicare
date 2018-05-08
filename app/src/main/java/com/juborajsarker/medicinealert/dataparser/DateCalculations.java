package com.juborajsarker.medicinealert.dataparser;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateCalculations {

    public   String addDays(String fromDate, String noOfdays) {

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
}
