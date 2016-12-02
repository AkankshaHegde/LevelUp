package com.gmat.terminator.fragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gmat.terminator.R;
import com.gmat.terminator.interfaces.ICountdownTime;
import com.gmat.terminator.interfaces.IDateSelection;
import com.gmat.terminator.other.TestCoundownTimer;
import com.gmat.terminator.utils.AppUtility;
import com.gmat.terminator.utils.Constants;

import java.util.Calendar;

public class TestCountdownFragment extends Fragment implements View.OnClickListener, IDateSelection, ICountdownTime {
    private TextView mSelectedDate, mSelectedTime, mCountdownTime;
    LinearLayout mCountDownTimerLyt, mPickdateLyt, mDateSelectorLyt, mTimeSelectorLyt;
    private final long interval = 1 * 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, null);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        mPickdateLyt = (LinearLayout) view.findViewById(R.id.pick_date_lyt);
        mCountDownTimerLyt = (LinearLayout) view.findViewById(R.id.test_countdown_lyt);
        mSelectedTime = (TextView) view.findViewById(R.id.selected_time_value);
        mSelectedDate = (TextView) view.findViewById(R.id.selected_date_value);
        mCountdownTime = (TextView) view.findViewById(R.id.test_countdown_value);

        mPickdateLyt.setOnClickListener(this);
        mDateSelectorLyt = (LinearLayout) view.findViewById(R.id.pick_date_initial_lyt);
        mDateSelectorLyt.setOnClickListener(this);
        mTimeSelectorLyt = (LinearLayout) view.findViewById(R.id.pick_time_initial_lyt);
        mTimeSelectorLyt.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.pick_date_lyt:
                break;
            case R.id.pick_date_initial_lyt:
                handlePickDateLytClick();
                break;
            case R.id.pick_time_initial_lyt:
                handlePickTimeLytClick();
                break;
        }
    }

    private void handlePickTimeLytClick() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog
                .OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int
                    selectedMinute) {
                mSelectedTime.setText(selectedHour + ":" + selectedMinute);
                mPickdateLyt.setVisibility(View.VISIBLE);
                SlideToAbove();

                long startTime = AppUtility.getCountdownTimerMillis(mSelectedDate.getText().toString() + " " +
                                selectedHour + ":" + selectedMinute + ":" + "00");
                TestCoundownTimer countDownTimer = new TestCoundownTimer(startTime, interval, TestCountdownFragment.this);
                //mCountdownTime.setText(mCountdownTime.getText() + String.valueOf(startTime / 1000));
                countDownTimer.start();

                mCountDownTimerLyt.setVisibility(View.VISIBLE);
                //text.setText(text.getText() + String.valueOf(startTime / 1000));
            }
        }, hour, minute, false);//true if 24 hour time
        mTimePicker.setTitle("");
        mTimePicker.show();
    }

    private void handlePickDateLytClick() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setListener(TestCountdownFragment.this);
        Bundle arg = new Bundle();
        arg.putString(Constants.KEY_DEFAULT_DOB, AppUtility.getTodayDate());
        arg.putString(Constants.MIN_DATE, AppUtility.getTodayDate());

        newFragment.setArguments(arg);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void setSelectedDate(String date) {
        mDateSelectorLyt.setVisibility(View.GONE);
        mTimeSelectorLyt.setVisibility(View.VISIBLE);
        mSelectedDate.setText(date);
        handlePickTimeLytClick();
    }

    @Override
    public void setRemainingTime(long millis) {
        String countDownTime = getCountdownTimeValue(millis);
        mCountdownTime.setText(String.valueOf(countDownTime));
    }

    private String getCountdownTimeValue(long millis) {
        int weeks = (int) (millis / (1000*60*60*24*7));
        int days = (int) (millis / (1000*60*60*24));
        int hours   = (int) ((millis / (1000*60*60*60)) % 24);
        int minutes = (int) ((millis / (1000*60)) % 60);
        int seconds = (int) (millis / 1000) % 60 ;

        String mCountdownValue = "";
        if(weeks > 0) {
            mCountdownValue = mCountdownValue + weeks;
        }
        if(days > 0) {
            if(!TextUtils.isEmpty(mCountdownValue)) {
                mCountdownValue = mCountdownValue + " : " + days;
            } else {
                mCountdownValue = mCountdownValue + days;
            }
        }
        if(hours > 0) {
            if(!TextUtils.isEmpty(mCountdownValue)) {
                mCountdownValue = mCountdownValue + " : " + hours ;
            } else {
                mCountdownValue = mCountdownValue + hours;
            }
        }
        if(minutes > 0) {
            if(!TextUtils.isEmpty(mCountdownValue)) {
                mCountdownValue = mCountdownValue + " : " + minutes ;
            } else {
                mCountdownValue = mCountdownValue + minutes;
            }
        }
        if(seconds > 0) {
            if(!TextUtils.isEmpty(mCountdownValue)) {
                mCountdownValue = mCountdownValue + " : " + seconds;
            } else {
                mCountdownValue = mCountdownValue + seconds;
            }
        }

        return mCountdownValue;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void SlideToAbove() {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -5.0f);

        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        mTimeSelectorLyt.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mTimeSelectorLyt.clearAnimation();

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        mTimeSelectorLyt.getWidth(), mTimeSelectorLyt.getHeight());
                // lp.setMargins(0, 0, 0, 0);
                lp.gravity = Gravity.TOP;
                mTimeSelectorLyt.setLayoutParams(lp);
                mTimeSelectorLyt.setVisibility(View.GONE);
            }

        });

    }
}
