package com.gmat.terminator.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gmat.terminator.R;
import com.gmat.terminator.model.SectionModel;
import com.gmat.terminator.model.TemplateModel;
import com.gmat.terminator.utils.Constants;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;

/**
 * Created by Akanksha on 13-Dec-16.
 */

public class TimerActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private int totalTime, totalQuestnCount, timeTraversed, previousSectionTime, sectionQuestnCount;
    private TextView time, sectionNameTxt;
    private Button butnstart, butnreset;
    long starttime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedtime = 0L;
    int t = 1;
    int secs = 0;
    int mins = 0;
    int milliseconds = 0, questCount = 0;
    Handler handler = new Handler();
    int earlyTime, mediumTime, avgTimePerQuestn;
    TextView questnCount, totalTimeTxt, mSectionNameTxt, mSectionCompletedTxt, mTimeUpTxt;
    RelativeLayout timerLyt;
    boolean isFirstClick = false;
    private int previousSecs = 0;
    private Realm mRealm;
    private ArrayList<Integer> mSectionTimeList;
    private ArrayList<SectionModel> mSectionModelList;
    private CountDownTimer mReverseCountDownTimer, mBreakTimer;
    private long millisRemaing = 0, millisStarting = 0;
    private int sectionCount = 0;
    private TemplateModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_timer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setTitle("Test Timer");

        mRealm = Realm.getInstance(this);
        mSectionTimeList = new ArrayList<>();
        mSectionModelList = new ArrayList<>();
        //http://www.androidplus.org/2015/03/android-stopwatch-timer-app-tutorial-15.html
        //see for stopwatch of android to get some ideas of design

        initializeViews();
        getDataFromIntent();

    }

    private void initializeViews() {
        butnstart = (Button) findViewById(R.id.start);
        butnstart.setOnClickListener(this);

        butnreset = (Button) findViewById(R.id.reset);
        butnreset.setOnClickListener(this);

        time = (TextView) findViewById(R.id.timer);

        timerLyt = (RelativeLayout) findViewById(R.id.timer_lyt);
        timerLyt.setOnClickListener(this);

        totalTimeTxt = (TextView) findViewById(R.id.total_time);
        questnCount = (TextView) findViewById(R.id.question_countdown);

        mSectionNameTxt = (TextView) findViewById(R.id.section_name);
        mSectionCompletedTxt = (TextView) findViewById(R.id.section_done);
        mTimeUpTxt = (TextView) findViewById(R.id.timer_completed);
    }

    public Runnable updateTimer = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - starttime;
            updatedtime = timeSwapBuff + timeInMilliseconds;
            secs = (int) (updatedtime / 1000);
            setTimerBackgroundColor(secs);
            mins = secs / 60;
            secs = secs % 60;
            milliseconds = (int) (updatedtime % 1000);
            timeTraversed += updatedtime;
            time.setText("" + mins + ":" + String.format("%02d", secs) /*+ ":"
                    + String.format("%03d", milliseconds)*/);
            time.setTextColor(Color.WHITE);
            handler.postDelayed(this, 0);
        }};

    private void setTimerBackgroundColor(int inSecs) {
        if(inSecs != 0) {
            int secs = 0;
            if(previousSecs != 0) {
                secs = inSecs - previousSecs;
            } else {
                secs = inSecs;
            }
            if(avgTimePerQuestn != 0) {
                if(secs > 10) {
                    if(secs < (avgTimePerQuestn - 10) && !(secs >= avgTimePerQuestn)) {
                        timerLyt.setBackgroundResource(R.drawable.timer_bg_green);
                    } else if(secs <= avgTimePerQuestn && !(secs > avgTimePerQuestn)) {
                        timerLyt.setBackgroundResource(R.drawable.timer_bg_orange);
                    } else {
                        timerLyt.setBackgroundResource(R.drawable.timer_bg_red);
                    }
                }
            }
        }
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent != null) {
            if(intent.hasExtra(Constants.INTENT_EXTRA_TOTAL_TIME)) {
                totalTime = Integer.parseInt(intent.getStringExtra(Constants.INTENT_EXTRA_TOTAL_TIME));
                totalTimeTxt.setText(String.valueOf(totalTime) + ":00");
            }
            if(intent.hasExtra(Constants.INTENT_EXTRA_TOTAL_QUESTION_COUNT)) {
                totalQuestnCount = Integer.parseInt(intent.getStringExtra(Constants.INTENT_EXTRA_TOTAL_QUESTION_COUNT));
                questnCount.setText("0/" + String.valueOf(totalQuestnCount));
            }
            if(intent.hasExtra(Constants.INTENT_EXTRA_TEMPLATE_NAME)) {
                String templateName = intent.getStringExtra(Constants.INTENT_EXTRA_TEMPLATE_NAME);
                getSupportActionBar().setTitle(templateName);
                model = mRealm.where(TemplateModel.class).equalTo("templateName", templateName).findFirst();


                if(model != null && model.getmSectionsList()!= null && model.getmSectionsList().size() > 0) {
                    int size = model.getmSectionsList().size();
                    for(int i = 0; i < size; i++) {
                        mSectionTimeList.add(model.getmSectionsList().get(i).getmTimePerSection());
                        mSectionModelList.add(model.getmSectionsList().get(i));

                        totalQuestnCount = totalQuestnCount + Integer.parseInt(model.getmSectionsList().get(i).getmNoOfQuestions());
                        totalTime = totalTime + model.getmSectionsList().get(i).getmTimePerSection();

                        if (i != (size -1)) {
                            totalTime = totalTime + model.getBreakTime();
                        }
                    }

                    sectionQuestnCount = Integer.parseInt(model.getmSectionsList().get(0).getmNoOfQuestions());
                }
            }
        }
    }
    //computePerQuestionTime();


    private void computePerQuestionTime() {
        if(totalQuestnCount != 0 && totalTime != 0) {
            avgTimePerQuestn = (totalTime/totalQuestnCount)*60;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start :
                handleStartBtnClick();
                break;
            case R.id.reset:
                handleResetBtnClick();
                handleReverseCountdownTimerReset();
                break;
            case R.id.timer_lyt:
                //if(!butnstart.getText().toString().equalsIgnoreCase("Start")) {
                    handleTimerLytClick();
                //}
        }
    }

    private void handleTimerLytClick() {
        if(totalQuestnCount != 0) {
            questCount++;
            handleResetBtnClick();
            handleStartBtnClick();
            if(questCount <= totalQuestnCount) {
                questnCount.setText(String.valueOf(questCount + "/" + sectionQuestnCount));
                previousSecs = secs;
                if(questCount == totalQuestnCount) {
                    timeSwapBuff += timeInMilliseconds;
                    handler.removeCallbacks(updateTimer);
                    mReverseCountDownTimer.cancel();
                    //mReverseCountDownTimer.onFinish();
                    t = 2;
                    butnstart.setText("Start");
                    time.setTextColor(Color.WHITE);
                }
            }
        }

        for (int i = 0; i < mSectionModelList.size() ; i++) {
            if (i == sectionCount) {
                if (questCount == Integer.parseInt(mSectionModelList.get(i).getmNoOfQuestions())) {
                    setSectionAcknowledgementMsg(mSectionModelList.get(sectionCount));
                    sectionCount++;

                    if (i < mSectionModelList.size()) {
                        try {
                            totalQuestnCount = Integer.parseInt(mSectionModelList.get(i + 1).getmNoOfQuestions());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                            questCount = 0;

                            mSectionNameTxt.setText("Break Time");
                            questnCount.setVisibility(View.GONE);
                            totalTimeTxt.setVisibility(View.GONE);
                            butnstart.setVisibility(View.GONE);
                            butnreset.setVisibility(View.GONE);

                            handler.removeCallbacks(updateTimer);
                            mReverseCountDownTimer.cancel();
                            //mReverseCountDownTimer.onFinish();

                            if (sectionCount != mSectionModelList.size()) {
                                startBreakCountdownTimer(TimeUnit.MINUTES.toMillis(model.getBreakTime()),
                                        mSectionModelList.get(i + 1).getmSectionName());
                            }

                        if (sectionCount == mSectionModelList.size()) {
                            questnCount.setText(String.valueOf(questCount + "/" + sectionQuestnCount));
                            mSectionNameTxt.setText(mSectionModelList.get(i).getmSectionName());
                            questnCount.setVisibility(View.VISIBLE);
                            totalTimeTxt.setVisibility(View.VISIBLE);
                            butnstart.setVisibility(View.VISIBLE);
                            butnreset.setVisibility(View.VISIBLE);
                            mReverseCountDownTimer.cancel();
                            handler.removeCallbacks(updateTimer);


                        }

                    }
                }
            } else {
                if (i > sectionCount)
                return;
            }
        }
    }

    private void setSectionAcknowledgementMsg(SectionModel sectionModel) {
        long millisTraversed = millisStarting - millisRemaing;
        long minsTraversed = TimeUnit.MILLISECONDS.toMinutes(millisTraversed);
        if (minsTraversed <= sectionModel.getmTimePerSection()) {
            mSectionCompletedTxt.setText(sectionModel.getmSectionName() + " Completed!" + "\n");
        }
    }

    public void startBreakCountdownTimer(long milliSeconds, final String sectionName) {
        //long totalTimeINMillis = TimeUnit.MINUTES.toMillis(minutes);

        mBreakTimer = new CountDownTimer(milliSeconds, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                long remainingHrs = 0, remainingMins = 0, remainingSecs = 0;
                String remaingTime = "";

                //remaining Hrs
                remainingHrs = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                if (remainingHrs > 0) {
                    remaingTime = remaingTime + remainingHrs;
                }

                //remaining Mins
                remainingMins = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                if (!TextUtils.isEmpty(remaingTime)) {
                    remaingTime = remaingTime + ":";
                }
                remaingTime = remaingTime + remainingMins;

                //remaining Secs
                remainingSecs = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
                if (!TextUtils.isEmpty(remaingTime)) {
                    remaingTime = remaingTime + ":";
                }

                remaingTime = remaingTime + remainingSecs;
                time.setText(remaingTime);
            }

            @Override
            public void onFinish() {
                questnCount.setText(String.valueOf(questCount + "/" + sectionQuestnCount));
                mSectionNameTxt.setText(sectionName);
                questnCount.setVisibility(View.VISIBLE);
                totalTimeTxt.setVisibility(View.VISIBLE);
                butnstart.setVisibility(View.VISIBLE);
                butnreset.setVisibility(View.VISIBLE);

                if(timeTraversed != 0) {
                    startReverseCountdownTimer(millisRemaing);
                }
                handler.postDelayed(updateTimer, 0);
            }
        }.start();
    }


            private void handleResetBtnClick() {
        starttime = 0L;
        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
        updatedtime = 0L;
        t = 1;
        secs = 0;
        mins = 0;
        milliseconds = 0;
        butnstart.setText("Start");
        handler.removeCallbacks(updateTimer);
        time.setText("00:00");
    }

    private void handleReverseCountdownTimerReset() {
        millisRemaing = 0;
        mReverseCountDownTimer.cancel();
        //mReverseCountDownTimer.onFinish();
        totalTimeTxt.setText("00:00");
        questnCount.setText("0/0");
        questCount = 0;
        timeTraversed = 0;
    }

    private void handleStartBtnClick() {
        startTimerForSection();
        if(!isFirstClick) {
            isFirstClick = true;
            //timerLyt.setBackgroundResource(R.drawable.timer_bg_green);
        }
        if (t == 1) {
            butnstart.setText("Pause");
            starttime = SystemClock.uptimeMillis();
            questnCount.setText(String.valueOf(questCount + "/" + sectionQuestnCount));

            if(timeTraversed != 0) {
                startReverseCountdownTimer(millisRemaing);
            }
            handler.postDelayed(updateTimer, 0);
            t = 0;
        } else if(t == 2) {
            handleResetBtnClick();
            t = 1;
            handleStartBtnClick();
        } else {
            butnstart.setText("Start");
            time.setTextColor(Color.WHITE);
            timeSwapBuff += timeInMilliseconds;
            handler.removeCallbacks(updateTimer);
            t = 1;
        }
    }

    private void startTimerForSection() {
        if(mSectionTimeList != null && mSectionTimeList.size() > 0 &&
                mSectionModelList != null && mSectionModelList.size() > 0) {

            if(timeTraversed == 0) {
                mSectionNameTxt.setText(mSectionModelList.get(0).getmSectionName());
                //questnCount.setText(mSectionModelList.get(0).getmNoOfQuestions());
                totalTimeTxt.setText(convertTimeToHrsAndMins(totalTime) + ":00");
                questnCount.setText("0/" + mSectionModelList.get(0).getmNoOfQuestions());
                startReverseCountdownTimer(TimeUnit.MINUTES.toMillis(totalTime));
                handler.postDelayed(updateTimer, 0);
            }

            if (millisRemaing > 0) {
                if (butnstart.getText().toString().equalsIgnoreCase("Pause")) {
                    mReverseCountDownTimer.cancel();
                    //mReverseCountDownTimer.onFinish();
                }
            }
        }
    }

    private String convertTimeToHrsAndMins(int totalTime) {
        return TimeUnit.MINUTES.toHours(totalTime)+ ":" +
                (TimeUnit.MINUTES.toMinutes(totalTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MINUTES.toHours(totalTime)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void startReverseCountdownTimer(long milliSeconds) {
        //long totalTimeINMillis = TimeUnit.MINUTES.toMillis(minutes);
        millisStarting = milliSeconds;
        mReverseCountDownTimer = new CountDownTimer(milliSeconds, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                millisRemaing = millisUntilFinished;
                long remainingHrs = 0, remainingMins = 0, remainingSecs = 0;
                String remaingTime = "";

                //remaining Hrs
                remainingHrs = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                if (remainingHrs > 0) { remaingTime = remaingTime + remainingHrs; }

                //remaining Mins
                remainingMins = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                    if (!TextUtils.isEmpty(remaingTime)) {
                        remaingTime = remaingTime + ":" ;
                    }
                    remaingTime = remaingTime + remainingMins;

                //remaining Secs
                remainingSecs = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
                    if (!TextUtils.isEmpty(remaingTime)) {
                        remaingTime = remaingTime + ":" ;
                    }

                    remaingTime = remaingTime +  remainingSecs;
                    totalTimeTxt.setText(remaingTime);
            }

            public void onFinish() {
                mSectionNameTxt.setText("Times Up!");
            }
        }.start();
    }
}



