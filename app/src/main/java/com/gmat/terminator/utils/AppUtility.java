package com.gmat.terminator.utils;

import android.os.Build;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Akanksha on 30-Nov-16.
 */

public class AppUtility {

    public static long getDateDiff(Date dateToday, Date dateSelected, TimeUnit timeUnit, Locale appLocale) {
        long diffInMillies = 0;
        diffInMillies = dateToday.getTime() - dateSelected.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    public static int getOSVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static String getTodayDate() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH)+ "/" +  (c.get(Calendar.MONTH) + 1)  + "/" + c.get(Calendar.YEAR);
    }

    public static String getYesterdayDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        return c.get(Calendar.DAY_OF_MONTH)+ "/" +  (c.get(Calendar.MONTH) + 1)  + "/" + c.get(Calendar.YEAR);
    }

    public static long getCountdownTimerMillis(String selectedDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date date = null;
        long millis = 0;
        try {
            date = sdf.parse(selectedDate);

            Calendar c = Calendar.getInstance();
            String todayDate = sdf.format(c.getTime());
            Date currentTime = sdf.parse(todayDate);

            millis = getDateDiff(date, currentTime, TimeUnit.MILLISECONDS, Locale.getDefault());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("Date - Time in milliseconds : " + millis);
        return millis;

    }

}
