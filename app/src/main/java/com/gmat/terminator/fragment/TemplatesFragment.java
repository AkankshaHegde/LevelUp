package com.gmat.terminator.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gmat.terminator.R;
import com.gmat.terminator.activity.AddSectionInfoActivity;

public class TemplatesFragment extends Fragment implements View.OnClickListener {
     private TextView mAddTemplateBtn;
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
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_template_lyt);

        EditText noOfSections = (EditText) dialog.findViewById(R.id.no_of_sections);
        Button dialogButton = (Button) dialog.findViewById(R.id.proceed_btn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddSectionInfoActivity.class);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
