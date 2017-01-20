package com.gmat.terminator.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gmat.terminator.R;
import com.gmat.terminator.activity.AddTemplateActivity;
import com.gmat.terminator.activity.TimerActivity;
import com.gmat.terminator.adapter.AddSectionAdapter;
import com.gmat.terminator.interfaces.ISectionClickListener;
import com.gmat.terminator.model.TemplateModel;
import com.gmat.terminator.utils.AppUtility;
import com.gmat.terminator.utils.Constants;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Akanksha on 16-Nov-16.
 */

public class CreateTestFragment extends Fragment implements View.OnClickListener, ISectionClickListener {
    private TextView mAddTemplateBtn, mChooseExistingLabel;
    private Realm mRealm;
    private RecyclerView mExistingTemplatesList;
    private ArrayList<String> mTemplateArrayList;
    private AddSectionAdapter mAddSectionAdapter;
    private Animation mAnimationShake;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_templates, null);

        mRealm = Realm.getInstance(getActivity());
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        mTemplateArrayList = new ArrayList<>();
        mAnimationShake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        mChooseExistingLabel = (TextView) view.findViewById(R.id.choose_template_txt);
        AppUtility.setRobotoMediumFont(getActivity(), mChooseExistingLabel, Typeface.NORMAL);

        mAddTemplateBtn = (TextView) view.findViewById(R.id.add_template_btn);
        AppUtility.setRobotoMediumFont(getActivity(), mAddTemplateBtn, Typeface.NORMAL);
        mAddTemplateBtn.setOnClickListener(this);

        mExistingTemplatesList = (RecyclerView) view.findViewById(R.id.existing_templates_list);
        int gridUnit = AppUtility.getScreenGridUnitBy32(getActivity());
        mExistingTemplatesList.setPadding(gridUnit, gridUnit, gridUnit, gridUnit);
        mExistingTemplatesList.setLayoutManager(new LinearLayoutManager(getActivity()));

        RealmResults<TemplateModel> templateList = mRealm.allObjects(TemplateModel.class);
        for(TemplateModel model : templateList) {
            mTemplateArrayList.add(model.getTemplateName());
            mChooseExistingLabel.setVisibility(View.VISIBLE);
        }

        mAddSectionAdapter = new AddSectionAdapter(getActivity(), mTemplateArrayList, this);
        mExistingTemplatesList.setAdapter(mAddSectionAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_template_btn:
                showAddTemplateDialog();
                break;
        }
    }

    private void showAddTemplateDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        final AlertDialog alertDialog = dialogBuilder.create();

        //create custom view for the dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_template_lyt, null);

        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);

        final EditText templateName = (EditText) dialogView.findViewById(R.id.template_name);
        final EditText sectionCount = (EditText) dialogView.findViewById(R.id.no_of_sections);
        final TextInputLayout mTemplateNameLyt = (TextInputLayout) dialogView.findViewById(R.id.input_layout_template_name);

        Button dialogButton = (Button) dialogView.findViewById(R.id.proceed_btn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEntry(templateName.getText().toString(), alertDialog, mTemplateNameLyt);

            }
        });

        alertDialog.show();
    }

    private void validateEntry(String templateName, AlertDialog alertDialog, TextInputLayout mTemplateNameLyt) {
        if (TextUtils.isEmpty(templateName)) {
            mTemplateNameLyt.startAnimation(mAnimationShake);
            return;
        }

        Intent i = new Intent(getActivity(), AddTemplateActivity.class);
        i.putExtra(Constants.INTENT_EXTRA_TEMPLATE_NAME, templateName);
        startActivityForResult(i, Constants.REQUEST_CODE_TEMPLATE);
        alertDialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_TEMPLATE) {
            if (resultCode == RESULT_OK) {
                RealmResults<TemplateModel> templateList = mRealm.allObjects(TemplateModel.class);
                if(templateList == null) return;

                mTemplateArrayList.clear();
                for(TemplateModel model : templateList) {
                    mTemplateArrayList.add(model.getTemplateName());
                    mChooseExistingLabel.setVisibility(View.VISIBLE);
                }
                mAddSectionAdapter.notifyDataSetChanged();
            }
        }
    }

    private void addDataToRealm(String templateName, String sectionCount) {
        /*TemplateModel model = new TemplateModel();
        model.setTemplateName(templateName);
        //model.setNoOfSections(sectionCount);

        *//*for(int i=0; i < Integer.parseInt(sectionCount); i++) {
            SectionModel sectionModel = new SectionModel();
            sectionModel.setTemplateName(templateName);
            sectionModel.setmSectionName("Section " + (i+1));

            mRealm.beginTransaction();
            mRealm.copyToRealm(sectionModel);
            mRealm.commitTransaction();
        }
*//*
        mRealm.beginTransaction();
        mRealm.copyToRealm(model);
        mRealm.commitTransaction();*/
    }

    @Override
    public void onSectionClicked(String sectionName, String sectionType) {
        Intent intent = new Intent(getActivity(), TimerActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_TEMPLATE_NAME, sectionName);
        startActivity(intent);
    }
}
