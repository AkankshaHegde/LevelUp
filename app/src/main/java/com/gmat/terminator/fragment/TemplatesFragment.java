package com.gmat.terminator.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gmat.terminator.R;
import com.gmat.terminator.activity.AddSectionInfoActivity;
import com.gmat.terminator.model.SectionModel;
import com.gmat.terminator.model.TemplateModel;
import com.gmat.terminator.utils.Constants;

import java.util.ArrayList;

import io.realm.Realm;

public class TemplatesFragment extends Fragment implements View.OnClickListener {
     private TextView mAddTemplateBtn;
    private Realm mRealm;

    public TemplatesFragment() {
        // Required empty public constructor
    }

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
        mAddTemplateBtn = (TextView) view.findViewById(R.id.add_template_btn);
        mAddTemplateBtn.setOnClickListener(this);
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

        Button dialogButton = (Button) dialogView.findViewById(R.id.proceed_btn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddSectionInfoActivity.class);
                i.putExtra(Constants.INTENT_EXTRA_TEMPLATE_NAME, templateName.getText().toString());
                i.putExtra(Constants.INTENT_EXTRA_SECTION_COUNT, sectionCount.getText().toString());
                addDataToRealm(templateName.getText().toString(), sectionCount.getText().toString());
                startActivity(i);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void addDataToRealm(String templateName, String sectionCount) {
        TemplateModel model = new TemplateModel();
        model.setTemplateName(templateName);
        model.setNoOfSections(sectionCount);

        /*for(int i=0; i < Integer.parseInt(sectionCount); i++) {
            SectionModel sectionModel = new SectionModel();
            sectionModel.setTemplateName(templateName);
            sectionModel.setmSectionName("Section " + (i+1));

            mRealm.beginTransaction();
            mRealm.copyToRealm(sectionModel);
            mRealm.commitTransaction();
        }
*/
        mRealm.beginTransaction();
        mRealm.copyToRealm(model);
        mRealm.commitTransaction();
    }
}
