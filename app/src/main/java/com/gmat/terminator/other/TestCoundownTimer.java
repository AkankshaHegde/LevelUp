package com.gmat.terminator.other;

import android.os.CountDownTimer;

import com.gmat.terminator.interfaces.ICountdownTime;

/**
 * Created by Akanksha on 01-Dec-16.
 */

public class TestCoundownTimer extends CountDownTimer {
    private ICountdownTime mCountDownTimeListener;

    public TestCoundownTimer(long millisInFuture, long countDownInterval,ICountdownTime inCountdownListener) {
        super(millisInFuture, countDownInterval);
        mCountDownTimeListener = inCountdownListener;
    }

    @Override
    public void onTick(long l) {
        mCountDownTimeListener.setRemainingTime(l);
    }

    @Override
    public void onFinish() {
        mCountDownTimeListener.setRemainingTime(0);
    }

}