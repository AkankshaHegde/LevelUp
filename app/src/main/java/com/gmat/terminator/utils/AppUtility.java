package com.gmat.terminator.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
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
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String todayDate = todayFormat.format(new Date());
            Date currentTime = sdf.parse(todayDate);

            millis = getDateDiff(date, currentTime, TimeUnit.MILLISECONDS, Locale.getDefault());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("Date - Time in milliseconds : " + millis);
        return millis;

    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int width;
        if (activity != null && !activity.isFinishing()) {
            activity.getWindowManager().getDefaultDisplay()
                    .getMetrics(displayMetrics);
            width = displayMetrics.widthPixels;
        } else {
            width = 0;
        }
        return width;
    }

    public static int getScreenGridUnit(Context context) {
        return getScreenWidth((Activity) context)
                / Constants.NUMBER_OF_BOX_IN_ROW;
    }

    public static int getScreenGridUnitBy32(Context context) {
        return getScreenWidth((Activity) context) / 32;
    }

    public static int setRandomColor() {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;
    }

    public static void setRobotoMediumFont(Context context, TextView view,
                                           int style) {
        if (view == null)
            return;


        if (getOSVersion() >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            view.setTypeface(Typeface.create(Constants.FONT_FAMILY_SANS_SERIF_MEDIUM, Typeface.NORMAL));
        } else {
            view.setTypeface(null, Typeface.NORMAL);
        }


        //view.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));

    }

    public static void setRobotoBoldFont(Context context, TextView view,
                                         int style) {
        if (view == null)
            return;


        if (getOSVersion() >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            view.setTypeface(Typeface.create(Constants.FONT_FAMILY_SANS_SERIF, Typeface.BOLD));
        } else {
            view.setTypeface(null, Typeface.BOLD);
        }
    }

    public static String getPackageName(Context context) {
        String packageName = "";
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            packageName = packageInfo.packageName;
            return packageName;
        } catch (PackageManager.NameNotFoundException ex) {

        }
        return packageName;
    }
}
