package com.gmat.terminator.utils;

import android.os.Build;

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
        diffInMillies = dateSelected.getTime() - dateToday.getTime();
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
}
