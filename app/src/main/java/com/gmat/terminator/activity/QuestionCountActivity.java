package com.gmat.terminator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gmat.terminator.R;
import com.gmat.terminator.utils.Constants;

import static com.gmat.terminator.R.id.second_name;

/**
 * Created by Akanksha on 15-Dec-16.
 */

public class QuestionCountActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private EditText mFirstName, mLastName;
    private Button mProceedBtn;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quest_count);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        getDataFromIntent();
        initializeViews();
    }

    private void getDataFromIntent() {

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
        Intent i = new Intent(QuestionCountActivity.this, TimerActivity.class);
        i.putExtra(Constants.INTENT_EXTRA_TOTAL_QUESTION_COUNT, mFirstName.getText().toString());
        i.putExtra(Constants.INTENT_EXTRA_TOTAL_TIME, mLastName.getText().toString());
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

