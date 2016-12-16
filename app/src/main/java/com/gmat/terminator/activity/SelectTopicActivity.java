package com.gmat.terminator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.gmat.terminator.R;
import com.gmat.terminator.fragment.CreateTestFragment;
import com.gmat.terminator.utils.Constants;

import java.util.ArrayList;

/**
 * Created by Akanksha on 12-Dec-16.
 */

public class SelectTopicActivity extends AppCompatActivity {
    private CreateTestFragment mCreateTestFragment;
    private final String TAG_TEST_FRAGMENT = "test_fragment";
    private String mTopicName;
    private ArrayList<String> mTopicList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_topic_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        getDataFromIntent();

        initializeFragment();
    }

    private void initializeFragment() {
        mCreateTestFragment = new CreateTestFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_EXTRA_TOPIC_NAME, mTopicName);
        bundle.putStringArrayList(Constants.INTENT_EXTRA_TOPIC_LIST, mTopicList);
        mCreateTestFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.topics_container, mCreateTestFragment, TAG_TEST_FRAGMENT).commit();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent != null) {
            if(intent.hasExtra(Constants.INTENT_EXTRA_TOPIC_NAME)) {
                mTopicName = intent.getStringExtra(Constants.INTENT_EXTRA_TOPIC_NAME);
                toolbar.setTitle(mTopicName);
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
