package com.gmat.terminator.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Akanksha on 09-Dec-16.
 */

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name

    private static final String IS_REGISTER_FIRST_TIME_LAUNCH = "IsRegistrationFirstTimeLaunch";
    private static final String IS_WELCOME_FIRST_TIME_LAUNCH = "IsWelcomeFirstTimeLaunch";

    public PrefManager(Context context, String prefName) {
        this._context = context;
        pref = _context.getSharedPreferences(prefName, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setWelcomeFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_WELCOME_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public void setRegistrationFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_REGISTER_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isRegisterFirstTimeLaunch() {
        return pref.getBoolean(IS_REGISTER_FIRST_TIME_LAUNCH, true);
    }

    public boolean isWelcomeFirstTimeLaunch() {
        return pref.getBoolean(IS_WELCOME_FIRST_TIME_LAUNCH, true);
    }
}
