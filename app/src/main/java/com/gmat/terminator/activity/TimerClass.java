package com.gmat.terminator.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gmat.terminator.R;
import com.gmat.terminator.utils.Constants;

/**
 * Created by Akanksha on 13-Dec-16.
 */

public class TimerClass extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private int totalTime, totalQuestnCount;
    private TextView time;
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
    TextView questnCount, totalTimeTxt;
    RelativeLayout timerLyt;
    boolean isFirstClick = false;
    private int previousSecs = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setTitle("Test Timer");
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
        }
        computePerQuestionTime();
    }

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
                break;
            case R.id.timer_lyt:
                if(!butnstart.getText().toString().equalsIgnoreCase("Start")) {
                    handleTimerLytClick();
                }
        }
    }

    private void handleTimerLytClick() {
        if(totalQuestnCount != 0) {
            questCount++;
            if(questCount <= totalQuestnCount) {
                questnCount.setText(String.valueOf(questCount + "/" + totalQuestnCount));
                previousSecs = secs;
                if(questCount == totalQuestnCount) {
                    timeSwapBuff += timeInMilliseconds;
                    handler.removeCallbacks(updateTimer);
                    t = 2;
                    butnstart.setText("Start");
                    time.setTextColor(Color.WHITE);
                }
            }
        }
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

    private void handleStartBtnClick() {
        if(!isFirstClick) {
            isFirstClick = true;
            timerLyt.setBackgroundResource(R.drawable.timer_bg_green);
        }
        if (t == 1) {
            butnstart.setText("Pause");
            starttime = SystemClock.uptimeMillis();
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
}



