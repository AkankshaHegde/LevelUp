package com.gmat.terminator.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmat.terminator.R;
import com.gmat.terminator.adapter.AddSectionNameAdapter;
import com.gmat.terminator.interfaces.SectionClickListener;
import com.gmat.terminator.model.SectionModel;
import com.gmat.terminator.model.TemplateModel;
import com.gmat.terminator.utils.Constants;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Akanksha on 18-Jan-17.
 */

public class AddTemplateActivity extends AppCompatActivity implements View.OnClickListener, SectionClickListener {
    private Toolbar toolbar;
    private EditText breaktimeEditText, mSectionCount;
    private TextView mAddSection, mRemoveSection, mAddBreakTime, mRemoveBreaktime;
    private LinearLayout mAddSectionLyt;
    private ArrayList<String> mSectionsArraylist;
    private ArrayList<SectionModel> mSectionModelList;
    private AddSectionNameAdapter mAddSectionNameAdapter;
    private Button mProceedBtn;
    private Realm mRealm;
    //private TextView mSectionCount;
    private String templateName;
    private LinearLayout mBreaktimeLyt;
    private Animation mAnimationShake;

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
        mSectionModelList = new ArrayList<>();
        breaktimeEditText = (EditText) findViewById(R.id.break_time);
        mBreaktimeLyt = (LinearLayout) findViewById(R.id.input_layout_break_time);

        mSectionCount = (EditText) findViewById(R.id.section_count);

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

        mAddBreakTime = (TextView) findViewById(R.id.increase_break_section);
        mAddBreakTime.setOnClickListener(this);

        mRemoveBreaktime = (TextView) findViewById(R.id.decrease_break_section);
        mRemoveBreaktime.setOnClickListener(this);

        mAnimationShake = AnimationUtils.loadAnimation(this, R.anim.shake);

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
            case R.id.add_template_btn:
                feedListToDatabase();
                break;
            case R.id.increase_break_section :
                handleIncrementBreakClick();
                break;
            case R.id.decrease_break_section:
                handleDecrementBreakClick();
                break;
        }
    }

    private void feedListToDatabase() {
        if(mSectionModelList.size() == 0) {
            showAlertDialog();
            return;
        }

        if(!TextUtils.isEmpty(templateName)) {
            TemplateModel model = /*mRealm.where(TemplateModel.class).equalTo("templateName", templateName).findFirst()*/new TemplateModel();
            model.setTemplateName(templateName);
            model.setNoOfSections(mSectionsArraylist.size());
            model.setBreakTime(Integer.parseInt(breaktimeEditText.getText().toString()));

            RealmList<SectionModel> mSectionList = new RealmList<>();
            for(int i = 0; i < mSectionModelList.size(); i++) {
                SectionModel sectionModel = new SectionModel();
                sectionModel.setmSectionId(mSectionModelList.get(i).getmSectionName() + System.currentTimeMillis());
                sectionModel.setmTimePerSection(mSectionModelList.get(i).getmTimePerSection());
                sectionModel.setmNoOfQuestions(mSectionModelList.get(i).getmNoOfSubSections());
                sectionModel.setmSectionName(mSectionModelList.get(i).getmSectionName());

                mSectionList.add(sectionModel);
            }

            model.setmSectionsList(mSectionList);

            mRealm.beginTransaction();
            mRealm.copyToRealmOrUpdate(model);
            mRealm.commitTransaction();

            TemplateModel finalmodel = mRealm.where(TemplateModel.class).equalTo("templateName", templateName).findFirst();
            if(finalmodel != null) {
                Log.d("TAG", finalmodel.getTemplateName());
            }

            passDataToPreviousActivity();
        }
    }

    public void showAlertDialog() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.enter_section_details));
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        alertDialogBuilder.setCancelable(true);
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void passDataToPreviousActivity() {
        Intent data = new Intent();
        data.putExtra(Constants.INTENT_EXTRA_TEMPLATE_NAME, templateName);
        setResult(RESULT_OK, data);
        finish();
    }

    private void handleDecrementBreakClick() {
        if(breaktimeEditText != null && breaktimeEditText.getText()!= null) {
            int breaktime = Integer.parseInt(breaktimeEditText.getText().toString());
            if(breaktime > 1) {
                breaktime--;
                breaktimeEditText.setText(breaktime+"");

                if(breaktime == 1) {
                    disableDecrementBtn(mRemoveBreaktime, true);
                } else {
                    disableDecrementBtn(mRemoveBreaktime, false);
                }
            } else {
                disableDecrementBtn(mRemoveBreaktime, true);
            }
        }
    }

    private void handleIncrementBreakClick() {
        if(breaktimeEditText != null && breaktimeEditText.getText()!= null) {
            int breaktime = Integer.parseInt(breaktimeEditText.getText().toString());
            if(breaktime >= 1) {
                breaktime++;
                disableDecrementBtn(mRemoveBreaktime, false);
                mBreaktimeLyt.setVisibility(View.VISIBLE);

                breaktimeEditText.setText(breaktime+"");
            }
        }
    }

    private void handleIncrementSectionClick() {
        if(mSectionCount != null && mSectionCount.getText()!= null) {
            int sectionCount = Integer.parseInt(mSectionCount.getText().toString());
            if(sectionCount >= 1) {
                sectionCount++;
                disableDecrementBtn(mRemoveSection, false);

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
                    disableDecrementBtn(mRemoveSection, true);
                } else {
                    disableDecrementBtn(mRemoveSection, false);
                }
                removeSectionView();
            } else {
                disableDecrementBtn(mRemoveSection, true);
            }
        }
    }

    private void disableDecrementBtn(TextView decrementBtn, boolean isDisable) {
        if(isDisable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decrementBtn.setTextColor(getResources().getColor(R.color.gray, null));
            } else {
                decrementBtn.setTextColor(getResources().getColor(R.color.gray));
            }
            mBreaktimeLyt.setVisibility(View.GONE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decrementBtn.setTextColor(getResources().getColor(R.color.colorPrimary, null));
            } else {
                decrementBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
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
                validateEntries(sectionName.getText().toString(), no_of_questions.getText().toString(),
                        total_time.getText().toString(), position, alertDialog, dialogView);

            }
        });

        alertDialog.show();
    }

    private void validateEntries(String sectionName, String questnCount, String totalTime, int position,
                                 AlertDialog alertDialog, View dialogView) {

        final TextInputLayout sectionNameLyt = (TextInputLayout) dialogView.findViewById(R.id.input_layout_section_name);
        final TextInputLayout questnCountLyt = (TextInputLayout) dialogView.findViewById(R.id.input_layout_no_of_questions);
        final TextInputLayout totalTimeLyt = (TextInputLayout) dialogView.findViewById(R.id.input_layout_total_time);

        if(TextUtils.isEmpty(sectionName)) {
            sectionNameLyt.startAnimation(mAnimationShake);
            return;
        }
        if(TextUtils.isEmpty(questnCount)) {
            questnCountLyt.startAnimation(mAnimationShake);
            return;
        }
        if(TextUtils.isEmpty(totalTime)) {
            totalTimeLyt.startAnimation(mAnimationShake);
            return;
        }

        createSectionModelList(sectionName, questnCount, totalTime);
        updateTemplateData(sectionName, position);
        alertDialog.dismiss();

    }

    private void createSectionModelList(String sectionName, String questnCount, String totalTime) {
        SectionModel model = new SectionModel();
        model.setmSectionName(sectionName);
        model.setmNoOfQuestions(questnCount);
        model.setmTimePerSection(totalTime);
        mSectionModelList.add(model);
    }

    private void updateTemplateData(String sectionName, int position) {
        mSectionsArraylist.set(position, sectionName);
        addChildToLinearLayout();
    }
}
