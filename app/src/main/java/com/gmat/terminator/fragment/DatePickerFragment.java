package com.gmat.terminator.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.DatePicker;

import com.gmat.terminator.interfaces.IDateSelection;
import com.gmat.terminator.utils.AppUtility;
import com.gmat.terminator.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    IDateSelection mListener;
    Date mMaxDate = null;
    Date mMinDate = null;
    private String dateFormat;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        //final Calendar c = Calendar.getInstance();
        onAttach(getParentFragment());
        String defaultDate = getArguments().getString(Constants.KEY_DEFAULT_DOB);
        SimpleDateFormat df = null;
         dateFormat = null;
        String minDate = null;
        String maxDate = null;
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(Constants.MAX_DATE)) {
                maxDate = (String) bundle.get(Constants.MAX_DATE);
            }
            if (bundle.containsKey(Constants.MIN_DATE)) {
                minDate = (String) bundle.get(Constants.MIN_DATE);
            }
            if (bundle.containsKey(Constants.DATE_FORMAT)) {
                dateFormat = (String) bundle.get(Constants.DATE_FORMAT);
            }
        }
        if (dateFormat != null) {
            df = new SimpleDateFormat(dateFormat);
        } else {
            df = new SimpleDateFormat("dd/MM/yyyy");
        }
        try {
            if (maxDate != null)
                mMaxDate = df.parse(maxDate);
            if (minDate != null)
                mMinDate = df.parse(minDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(df.parse(defaultDate));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        java.text.SimpleDateFormat df1 = new java.text.SimpleDateFormat("MM");
        int year = cal.get(Calendar.YEAR);
        int month = Integer.parseInt(df1.format(cal.getTime()));
        if (month == 0) {
            month = 0;
        } else {
            month = month - 1;
        }
        int day = cal.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        // request a window without the title
        if (AppUtility.getOSVersion() >= Build.VERSION_CODES.HONEYCOMB) {
            Calendar calendar = Calendar.getInstance();
            if (mMinDate != null) {
                datePickerDialog.getDatePicker().setMinDate(mMinDate.getTime());
            }
        }
        return datePickerDialog;
    }


    public void onAttach(Fragment fragment) {
        try {
            if (fragment instanceof IDateSelection) {
                mListener = (IDateSelection) fragment;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListener (IDateSelection listener) {
        mListener = listener;
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        try {
            if (activity instanceof IDateSelection) {
                mListener = (IDateSelection) activity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //to fix the bug of event firing twice
        //http://stackoverflow.com/questions/12436073/datepicker-ondatechangedlistener-called-twice
        if (view.isShown()) {
            if (mListener != null) {
                if(dateFormat!=null){
                    String dateOldString = day + "/" + (month + 1) + "/" + year;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date oldDate = sdf.parse(dateOldString);
                        SimpleDateFormat newSdf = new SimpleDateFormat(dateFormat);
                        String newDateString = newSdf.format(oldDate);
                        mListener.setSelectedDate(newDateString);
                    }catch (ParseException e){
                        mListener.setSelectedDate(day + "/" + (month + 1) + "/" + year);
                    }
                }else{
                    mListener.setSelectedDate(day + "/" + (month + 1) + "/" + year);
                }
            }
        }
    }


}
