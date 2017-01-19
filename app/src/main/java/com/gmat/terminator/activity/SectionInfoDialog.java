package com.gmat.terminator.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gmat.terminator.R;
import com.gmat.terminator.adapter.AddSectionNameAdapter;
import com.gmat.terminator.model.SectionModel;
import com.gmat.terminator.model.TemplateModel;
import com.gmat.terminator.utils.Constants;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Akanksha on 16-Jan-17.
 */

public class SectionInfoDialog extends AppCompatActivity implements View.OnClickListener {

    private EditText templateNameEditText, sectionCountEditText;
    private boolean isTemplate, hasSections;
    private String sectionName, templateName;
    private TextView mAddSection, mRemoveSection, mSectionCount;
    private LinearLayout mAddSectionLyt;
    private ArrayList<String> mSectionsArraylist;
    private AddSectionNameAdapter mAddSectionNameAdapter;
    private RelativeLayout mAddSectionHandlerLyt;
    private Button mProceedBtn;
    private RadioGroup group;
    private Realm mRealm;
    private TextInputLayout sectionNameLyt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_template_lyt);
        getSupportActionBar().hide();

        mRealm = Realm.getInstance(this);

        getDataFromIntent();
        //initialise views
        initializeViews();
    }

    private void initializeViews() {
        templateNameEditText = (EditText) findViewById(R.id.template_name);
        sectionCountEditText = (EditText) findViewById(R.id.no_of_sections);

        mSectionCount = (TextView) findViewById(R.id.section_count);
        mAddSectionLyt = (LinearLayout) findViewById(R.id.sections_linear_lyt);
        mAddSectionHandlerLyt = (RelativeLayout) findViewById(R.id.add_section_lyt);

        setDialogUI();

        //Initialize arraylist if it is null
        if(mSectionsArraylist == null) {
            mSectionsArraylist = new ArrayList<>();
        }
        //adapter
        mAddSectionNameAdapter = new AddSectionNameAdapter(this, mSectionsArraylist, null);
        addSectionView();

        mAddSection = (TextView) findViewById(R.id.increase_section);
        mAddSection.setOnClickListener(this);

        mRemoveSection = (TextView) findViewById(R.id.decrease_section);
        mRemoveSection.setOnClickListener(this);

        mProceedBtn = (Button) findViewById(R.id.proceed_btn);
        mProceedBtn.setOnClickListener(this);
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

    private void setDialogUI() {
        sectionNameLyt = (TextInputLayout) findViewById(R.id.input_layout_template_name);
        final TextInputLayout sectionCountLyt = (TextInputLayout) findViewById(R.id.input_layout_no_of_sections);
        TextView radionBtnLabel = (TextView) findViewById(R.id.radio_btn_label);
        group = (RadioGroup) findViewById(R.id.hasSubSectionsRadioGrp);

        setTitleEditTextHint(sectionNameLyt);
        sectionCountLyt.setHint("Number of Sub Sections");

        checkForSubSectionVisibility(group, sectionCountLyt);

        radionBtnLabel.setVisibility(View.VISIBLE);
        group.setVisibility(View.VISIBLE);
        sectionCountLyt.setVisibility(View.GONE);
        setRadioBtnBackground();
    }

    private void setTitleEditTextHint(TextInputLayout sectionNameLyt) {
        if(isTemplate) {
            sectionNameLyt.setHint("Template Name");
        } else {
            //sectionNameLyt.setHint("Section Name");
            sectionNameLyt.setVisibility(View.GONE);
        }
    }

    private void setRadioBtnBackground() {
        if (Build.VERSION.SDK_INT >= 21) {
            RadioButton positiveBtn = (RadioButton) findViewById(R.id.hasSubSection);
            RadioButton negativeBtn = (RadioButton) findViewById(R.id.hasNoSubSection);

            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_enabled}, //disabled
                            new int[]{android.R.attr.state_enabled} //enabled
                    },
                    new int[]{
                            getResources().getColor(R.color.colorPrimary)//disabled
                            ,getResources().getColor(R.color.colorPrimary) //enabled
                    }
            );
            positiveBtn.setButtonTintList(colorStateList);//set the color tint list
            negativeBtn.setButtonTintList(colorStateList);//set the color tint list
        }
    }

    private void checkForSubSectionVisibility(RadioGroup group, final TextInputLayout sectionCount) {
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.hasSubSection :
                        //sectionCount.setVisibility(View.VISIBLE);
                        mAddSectionHandlerLyt.setVisibility(View.VISIBLE);
                        mAddSectionLyt.setVisibility(View.VISIBLE);
                        hasSections = true;
                        break;
                    case R.id.hasNoSubSection:
                        //sectionCount.setVisibility(View.GONE);
                        mAddSectionHandlerLyt.setVisibility(View.GONE);
                        mAddSectionLyt.setVisibility(View.GONE);
                        hasSections = false;

                        break;
                }
            }
        });
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent.hasExtra(Constants.INTENT_EXTRA_SECTION_NAME)) {
            sectionName = intent.getStringExtra(Constants.INTENT_EXTRA_SECTION_NAME);
        }
        if(intent.hasExtra(Constants.INTENT_EXTRA_TEMPLATE_NAME)) {
            templateName = intent.getStringExtra(Constants.INTENT_EXTRA_TEMPLATE_NAME);
        }
        if(intent.hasExtra(Constants.INTENT_EXTRA_IS_TEMPLATE)) {
            isTemplate = intent.getBooleanExtra(Constants.INTENT_EXTRA_IS_TEMPLATE, false);
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
                feedListToDatabase();
        }
    }

    private void startSectionsActivity() {
        Intent intent = new Intent(SectionInfoDialog.this, AddSectionInfoActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_TEMPLATE_NAME, templateNameEditText.getText().toString());
        intent.putExtra(Constants.INTENT_EXTRA_SECTION_NAME, sectionName);
        startActivity(intent);
    }

    private void feedListToDatabase() {
        if (isTemplate) {
            TemplateModel templateModel = new TemplateModel();

            if (templateNameEditText != null &&
                    !TextUtils.isEmpty(templateNameEditText.getText().toString())) {
                templateModel.setTemplateName(templateNameEditText.getText().toString());
            } else {
                //add shake animation
                return;
            }

            if (hasSections) {
                if (mSectionsArraylist != null && mSectionsArraylist.size() > 0)
                    templateModel.setNoOfSections(mSectionsArraylist.size());

                RealmList<SectionModel> mSectionRealmList = new RealmList<>();
                for (int i = 0; i < mSectionsArraylist.size(); i++) {
                    SectionModel sectionModel = new SectionModel();
                    sectionModel.setmSectionName(mSectionsArraylist.get(i));
                    sectionModel.setmSectionId(mSectionsArraylist.get(i) + System.currentTimeMillis());
                    sectionModel.setmSectionsList(new RealmList<SectionModel>());
                    /*mRealm.beginTransaction();
                    mRealm.copyToRealm(sectionModel);
                    mRealm.commitTransaction();*/

                    mSectionRealmList.add(sectionModel);
                }

                templateModel.setmSectionsList(mSectionRealmList);

                mRealm.beginTransaction();
                mRealm.copyToRealm(templateModel);
                mRealm.commitTransaction();
            }
            startSectionsActivity();
        } else {

                    TemplateModel templateModel = mRealm.where(TemplateModel.class).equalTo("templateName", templateName).findFirst();//.where(SectionModel.class).equalTo("mSectionName", sectionName).findFirst();
                    Log.d("TAG", "template model");

                    RealmList<SectionModel> mSectionModelList = templateModel.getmSectionsList();

                    SectionModel sectionModel = null;
                    for (SectionModel model : mSectionModelList) {
                        if (model.getmSectionName().equalsIgnoreCase(sectionName)) {
                            sectionModel = model;
                        }
                    }
                    if (hasSections && sectionModel != null) {
                        if (mSectionsArraylist != null && mSectionsArraylist.size() > 0) {
                            mRealm.beginTransaction();

                            sectionModel.setmNoOfSubSections(String.valueOf(mSectionsArraylist.size()));

                            RealmList<SectionModel> mSectionRealmList = new RealmList<>();
                            for (int i = 0; i < mSectionsArraylist.size(); i++) {
                                SectionModel subSectionModel = new SectionModel();
                                subSectionModel.setmSectionName(mSectionsArraylist.get(i));
                                mSectionRealmList.add(subSectionModel);
                                subSectionModel.setmSectionId(sectionModel.getmSectionId());

                                sectionModel.getmSectionsList().add(subSectionModel);

                            }

                            mRealm.commitTransaction();
                        }

                    }
            startSectionsActivity();
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
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mRemoveSection.setTextColor(getResources().getColor(R.color.colorPrimary, null));
            } else {
                mRemoveSection.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        }
    }
}
