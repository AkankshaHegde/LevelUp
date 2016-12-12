package com.gmat.terminator.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gmat.terminator.R;
import com.gmat.terminator.utils.Constants;
import com.gmat.terminator.utils.SecureSharedPrefs;

import static com.gmat.terminator.R.id.second_name;

/**
 * Created by Akanksha on 09-Dec-16.
 */

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher{
    private EditText mFirstName, mLastName;
    private Button mProceedBtn;
    private SecureSharedPrefs prefs;
    private boolean mIsFirstLaunch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Checking for first time launch - before calling setContentView()
        prefs = new SecureSharedPrefs(getApplicationContext());
        mIsFirstLaunch = prefs.getBoolean(Constants.PREF_NAME_REGISTRATION, false);

        if (mIsFirstLaunch) {
            launchHomeScreen();
            finish();
        } else {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(Constants.PREF_NAME_REGISTRATION, true);
            editor.commit();
        }


        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_registration);

        initializeViews();
    }

    private void launchHomeScreen() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void initializeViews() {
        mFirstName = (EditText) findViewById(R.id.first_name);
        mFirstName.addTextChangedListener(this);
        mLastName = (EditText) findViewById(second_name);
        mLastName.addTextChangedListener(this);
        mProceedBtn = (Button) findViewById(R.id.proceed_btn);
        mProceedBtn.setOnClickListener(this);
    }

    private void handleProceedBtnVisibility() {
        if(!TextUtils.isEmpty(mFirstName.getText().toString()) &&
                !TextUtils.isEmpty(mLastName.getText().toString())) {
            mProceedBtn.setVisibility(View.VISIBLE);
        } else {
            mProceedBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.proceed_btn :
                handleProceedBtnClick();
                break;
        }
    }

    private void handleProceedBtnClick() {
        Intent i = new Intent(RegistrationActivity.this, MainActivity.class);
        i.putExtra(Constants.INTENT_EXTRA_FIRST_NAME, mFirstName.getText().toString());
        i.putExtra(Constants.INTENT_EXTRA_LAST_NAME, mLastName.getText().toString());
        startActivity(i);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        handleProceedBtnVisibility();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
