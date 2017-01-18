package com.gmat.terminator.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gmat.terminator.R;
import com.gmat.terminator.adapter.AddSectionAdapter;
import com.gmat.terminator.interfaces.ISectionClickListener;
import com.gmat.terminator.model.SectionModel;
import com.gmat.terminator.model.TemplateModel;
import com.gmat.terminator.utils.AppUtility;
import com.gmat.terminator.utils.Constants;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Akanksha Hegde on 12-01-2017.
 */

public class AddSectionInfoActivity extends AppCompatActivity implements ISectionClickListener {
    private Toolbar toolbar;
    private String templateName;
    private int sectionCount;
    private AddSectionAdapter mAddSectionAdapter;
    private ArrayList<String> mSectionsArrayList;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_section_info_activity);
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
        RecyclerView mSectionsList = (RecyclerView) findViewById(R.id.sections_list);
        int gridUnit = AppUtility.getScreenGridUnitBy32(this);
        mSectionsList.setPadding(gridUnit, gridUnit, gridUnit, gridUnit);
        mSectionsList.setLayoutManager(new LinearLayoutManager(this));

        mAddSectionAdapter = new AddSectionAdapter(this, mSectionsArrayList, this);
        mSectionsList.setAdapter(mAddSectionAdapter);

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        templateName = intent.getStringExtra(Constants.INTENT_EXTRA_TEMPLATE_NAME);
        getSupportActionBar().setTitle(templateName);
        TemplateModel templateModel = mRealm.where(TemplateModel.class).equalTo("templateName", templateName).findFirst();

        RealmList<SectionModel> sectionModelList = templateModel.getmSectionsList();
        mSectionsArrayList = new ArrayList<>();

        for (SectionModel model : sectionModelList) {
            mSectionsArrayList.add(model.getmSectionName());
        }
        //sectionCount = Integer.parseInt(intent.getStringExtra(Constants.INTENT_EXTRA_SECTION_COUNT));

        /*for(int i = 0; i < sectionCount; i++) {
            mSectionsArrayList.add("Section " + i);
        }*/
    }

    private void showAddTemplateDialog(final String sectionName) {
        /*AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        final AlertDialog alertDialog = dialogBuilder.create();

        //create custom view for the dialog
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_template_lyt, null);

        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);

        final EditText templateNameEditText = (EditText) dialogView.findViewById(R.id.template_name);
        final EditText sectionCount = (EditText) dialogView.findViewById(R.id.no_of_sections);

        setDialogUI(dialogView);
        Button dialogButton = (Button) dialogView.findViewById(R.id.proceed_btn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddSectionInfoActivity.this, AddSectionInfoActivity.class);
                i.putExtra(Constants.INTENT_EXTRA_TEMPLATE_NAME, templateNameEditText.getText().toString());
                i.putExtra(Constants.INTENT_EXTRA_SECTION_COUNT, sectionCount.getText().toString());
                setDataToDatabase(templateNameEditText.getText().toString(), sectionCount.getText().toString(), templateName
                , sectionName);
                startActivity(i);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();*/

        Intent i = new Intent(this, SectionInfoDialog.class);
        i.putExtra(Constants.INTENT_EXTRA_IS_TEMPLATE, false);
        i.putExtra(Constants.INTENT_EXTRA_TEMPLATE_NAME, templateName);
        i.putExtra(Constants.INTENT_EXTRA_SECTION_NAME, sectionName);

        startActivity(i);
    }

    private void setDataToDatabase(String sectionName, String subSectionCount, String templateName, String previousSecName) {
        SectionModel sectionModel = new SectionModel();
        sectionModel.setmSectionName(sectionName);
        sectionModel.setmNoOfSubSections(subSectionCount);
        sectionModel.setTemplateName(templateName);

        mRealm.beginTransaction();
        mRealm.copyToRealm(sectionModel);
        mRealm.commitTransaction();

        updateListView(sectionName, previousSecName);
    }

    private void updateListView(String sectionName, String previousSecName) {
        for(int i = 0; i < mSectionsArrayList.size(); i++) {
            if(mSectionsArrayList.get(i).equalsIgnoreCase(previousSecName)) {
                mSectionsArrayList.set(i, sectionName);
            }
        }
        mAddSectionAdapter.notifyDataSetChanged();
    }

    private void setDialogUI(View dialogView) {
        TextInputLayout sectionNameLyt = (TextInputLayout) dialogView.findViewById(R.id.input_layout_template_name);
        final TextInputLayout sectionCountLyt = (TextInputLayout) dialogView.findViewById(R.id.input_layout_no_of_sections);
        TextView radionBtnLabel = (TextView) dialogView.findViewById(R.id.radio_btn_label);
        RadioGroup group = (RadioGroup) dialogView.findViewById(R.id.hasSubSectionsRadioGrp);

        sectionNameLyt.setHint("Section Name");
        sectionCountLyt.setHint("Number of Sub Sections");

        checkForSubSectionVisibility(group, sectionCountLyt);

        radionBtnLabel.setVisibility(View.VISIBLE);
        group.setVisibility(View.VISIBLE);
        sectionCountLyt.setVisibility(View.GONE);
        setRadioBtnBackground(dialogView);
    }

    private void setRadioBtnBackground(View dialogView) {
        if (Build.VERSION.SDK_INT >= 21) {
            RadioButton positiveBtn = (RadioButton) dialogView.findViewById(R.id.hasSubSection);
            RadioButton negativeBtn = (RadioButton) dialogView.findViewById(R.id.hasNoSubSection);

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
                        sectionCount.setVisibility(View.VISIBLE);
                        break;
                    case R.id.hasNoSubSection:
                        sectionCount.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    @Override
    public void onSectionClicked(String sectionName, String sectionType) {
        showAddTemplateDialog(sectionName);
    }
}
