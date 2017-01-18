package com.gmat.terminator.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmat.terminator.R;
import com.gmat.terminator.adapter.AddSectionNameAdapter;
import com.gmat.terminator.interfaces.SectionClickListener;
import com.gmat.terminator.utils.Constants;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by Akanksha on 18-Jan-17.
 */

public class AddTemplateActivity extends AppCompatActivity implements View.OnClickListener, SectionClickListener {
    private Toolbar toolbar;
    private EditText breaktimeEditText;
    private TextView mAddSection, mRemoveSection;
    private LinearLayout mAddSectionLyt;
    private ArrayList<String> mSectionsArraylist;
    private AddSectionNameAdapter mAddSectionNameAdapter;
    private Button mProceedBtn;
    private Realm mRealm;
    private TextView mSectionCount;
    private String templateName;
    private TextInputLayout mBreaktimeLyt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_template_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        mRealm = Realm.getInstance(this);
        getDataFromIntent();

        initializeViews();
    }

    private void initializeViews() {
        breaktimeEditText = (EditText) findViewById(R.id.break_time);
        mBreaktimeLyt = (TextInputLayout) findViewById(R.id.input_layout_break_time);

        mSectionCount = (TextView) findViewById(R.id.section_count);

        mAddSectionLyt = (LinearLayout) findViewById(R.id.sections_linear_lyt);

        //Initialize arraylist if it is null
        if(mSectionsArraylist == null) {
            mSectionsArraylist = new ArrayList<>();
        }
        //adapter
        mAddSectionNameAdapter = new AddSectionNameAdapter(this, mSectionsArraylist, this);
        addSectionView();

        mAddSection = (TextView) findViewById(R.id.increase_section);
        mAddSection.setOnClickListener(this);

        mRemoveSection = (TextView) findViewById(R.id.decrease_section);
        mRemoveSection.setOnClickListener(this);

        mProceedBtn = (Button) findViewById(R.id.add_template_btn);
        mProceedBtn.setOnClickListener(this);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();

        if(intent.hasExtra(Constants.INTENT_EXTRA_TEMPLATE_NAME)) {
            templateName = intent.getStringExtra(Constants.INTENT_EXTRA_TEMPLATE_NAME);
            getSupportActionBar().setTitle(templateName);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.increase_section:
                handleIncrementSectionClick();
                break;
            case R.id.decrease_section:
                handleDeccrementSectionClick();
                break;
            case R.id.proceed_btn:
                //feedListToDatabase();
        }
    }

    private void handleIncrementSectionClick() {
        if(mSectionCount != null && mSectionCount.getText()!= null) {
            int sectionCount = Integer.parseInt(mSectionCount.getText().toString());
            if(sectionCount >= 1) {
                sectionCount++;
                disableDecrementBtn(false);

                mSectionCount.setText(sectionCount+"");
                addSectionView();
            }
        }
    }

    private void handleDeccrementSectionClick() {
        if(mSectionCount != null && mSectionCount.getText()!= null) {
            int sectionCount = Integer.parseInt(mSectionCount.getText().toString());
            if(sectionCount > 1) {
                sectionCount--;
                mSectionCount.setText(sectionCount+"");

                if(sectionCount == 1) {
                    disableDecrementBtn(true);
                } else {
                    disableDecrementBtn(false);
                }
                removeSectionView();
            } else {
                disableDecrementBtn(true);
            }
        }
    }

    private void disableDecrementBtn(boolean isDisable) {
        if(isDisable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mRemoveSection.setTextColor(getResources().getColor(R.color.gray, null));
            } else {
                mRemoveSection.setTextColor(getResources().getColor(R.color.gray));
            }
            mBreaktimeLyt.setVisibility(View.GONE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mRemoveSection.setTextColor(getResources().getColor(R.color.colorPrimary, null));
            } else {
                mRemoveSection.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            mBreaktimeLyt.setVisibility(View.VISIBLE);

        }
    }

    private void addSectionView() {
        mSectionsArraylist.add("");

        addChildToLinearLayout();
    }

    private void addChildToLinearLayout() {
        mAddSectionNameAdapter.addMoreSections(mSectionsArraylist);
        int mChildCount = mAddSectionNameAdapter.getCount();

        //remove previous views before adding the new view
        mAddSectionLyt.removeAllViews();

        for (int i = 0; i < mChildCount; i++) {
            View item = mAddSectionNameAdapter.getView(i, null, null);
            mAddSectionLyt.addView(item);
        }
    }

    private void removeSectionView() {
        try {
            //remove item from arraylist on click of remove btn
            if(mSectionsArraylist != null && mSectionsArraylist.size() > 0) {
                mSectionsArraylist.remove(mSectionsArraylist.size() - 1);
            }
            //update linear layout
            addChildToLinearLayout();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSectionClick(int position) {
        showSectionDetailsDialog(position);
    }

    private void showSectionDetailsDialog(final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        final AlertDialog alertDialog = dialogBuilder.create();

        //create custom view for the dialog
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.single_section_dialog_lyt, null);

        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);

        final EditText sectionName = (EditText) dialogView.findViewById(R.id.section_name);
        final EditText no_of_questions = (EditText) dialogView.findViewById(R.id.no_of_sections);
        final EditText total_time = (EditText) dialogView.findViewById(R.id.total_time);

        Button dialogButton = (Button) dialogView.findViewById(R.id.done_btn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTemplateData(sectionName.getText().toString(), position);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void updateTemplateData(String sectionName, int position) {
        mSectionsArraylist.set(position, sectionName);
        addChildToLinearLayout();
    }
}
