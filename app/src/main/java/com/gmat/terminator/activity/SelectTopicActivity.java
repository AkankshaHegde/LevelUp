package com.gmat.terminator.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.gmat.terminator.R;
import com.gmat.terminator.fragment.CreateTestFragment;
import com.gmat.terminator.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Akanksha on 12-Dec-16.
 */

public class SelectTopicActivity extends AppCompatActivity {
    private CreateTestFragment mCreateTestFragment;
    private final String TAG_TEST_FRAGMENT = "test_fragment";
    private String mTopicName;
    private ArrayList<String> mTopicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Show status bar
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_topic);
        getDataFromIntent();

        initializeFragment();
    }

    private void initializeFragment() {
        mCreateTestFragment = new CreateTestFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_EXTRA_TOPIC_NAME, mTopicName);
        bundle.putSerializable(Constants.INTENT_EXTRA_TOPIC_LIST, mTopicList);
        mCreateTestFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.topics_container, mCreateTestFragment, TAG_TEST_FRAGMENT).commit();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent != null) {
            if(intent.hasExtra(Constants.INTENT_EXTRA_TOPIC_NAME)) {
                mTopicName = intent.getStringExtra(Constants.INTENT_EXTRA_TOPIC_NAME);
            }
            if(intent.hasExtra(Constants.INTENT_EXTRA_TOPIC_LIST)) {
                mTopicList = intent.getStringArrayListExtra(Constants.INTENT_EXTRA_TOPIC_LIST);
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
