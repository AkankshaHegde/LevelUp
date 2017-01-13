package com.gmat.terminator.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gmat.terminator.R;
import com.gmat.terminator.adapter.AddSectionAdapter;
import com.gmat.terminator.interfaces.ISectionClickListener;
import com.gmat.terminator.utils.AppUtility;
import com.gmat.terminator.utils.Constants;

import java.util.ArrayList;

/**
 * Created by Akanksha Hegde on 12-01-2017.
 */

public class AddSectionInfoActivity extends AppCompatActivity implements ISectionClickListener {
    private Toolbar toolbar;
    private String templateName;
    private int sectionCount;
    private AddSectionAdapter mAddSectionAdapter;
    private ArrayList<String> mSectionsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_section_info_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

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
        sectionCount = Integer.parseInt(intent.getStringExtra(Constants.INTENT_EXTRA_SECTION_COUNT));
        mSectionsArrayList = new ArrayList<>();

        for(int i = 0; i < sectionCount; i++) {
            mSectionsArrayList.add("Section " + i);
        }
    }

    private void showAddTemplateDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        final AlertDialog alertDialog = dialogBuilder.create();

        //create custom view for the dialog
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_template_lyt, null);

        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);

        final EditText templateName = (EditText) dialogView.findViewById(R.id.template_name);
        final EditText sectionCount = (EditText) dialogView.findViewById(R.id.no_of_sections);

        TextInputLayout sectionNameLyt = (TextInputLayout) dialogView.findViewById(R.id.input_layout_template_name);
        TextInputLayout sectionCountLyt = (TextInputLayout) dialogView.findViewById(R.id.input_layout_no_of_sections);

        sectionNameLyt.setHint("Section Name");
        sectionCountLyt.setHint("Number of Sub Sections");

        Button dialogButton = (Button) dialogView.findViewById(R.id.proceed_btn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddSectionInfoActivity.this, AddSectionInfoActivity.class);
                i.putExtra(Constants.INTENT_EXTRA_TEMPLATE_NAME, templateName.getText().toString());
                i.putExtra(Constants.INTENT_EXTRA_SECTION_COUNT, sectionCount.getText().toString());
                startActivity(i);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onSectionClicked(String sectionName, String sectionType) {
        showAddTemplateDialog();
    }
}
