package com.gmat.terminator.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmat.terminator.R;
import com.gmat.terminator.activity.QuestionCountActivity;
import com.gmat.terminator.activity.SelectTopicActivity;
import com.gmat.terminator.activity.TimerActivity;
import com.gmat.terminator.adapter.SectionsAdapter;
import com.gmat.terminator.interfaces.ISectionClickListener;
import com.gmat.terminator.model.SectionModel;
import com.gmat.terminator.other.RealmController;
import com.gmat.terminator.utils.AppUtility;
import com.gmat.terminator.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;

/**
 * Created by Akanksha on 16-Nov-16.
 */

public class QuestionTypeFragment extends Fragment implements ISectionClickListener {
    private RecyclerView mSectionsList;
    private SectionsAdapter mSectionsAdapter;
    private Realm mRealm;
    private String mTopicName;
    private ArrayList<String> mTopicList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_test, null);

        //get realm instance
        mRealm = RealmController.with(this).getRealm();

        getDataFromIntent();
        initializeViews(view);
        return view;
    }

    private void getDataFromIntent() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(Constants.INTENT_EXTRA_TOPIC_NAME)) {
                mTopicName = (String) bundle.get(Constants.INTENT_EXTRA_TOPIC_NAME);
            }
            if (bundle.containsKey(Constants.INTENT_EXTRA_TOPIC_LIST)) {
                mTopicList =  bundle.getStringArrayList(Constants.INTENT_EXTRA_TOPIC_LIST);
            }
        }
    }

    private void initializeViews(View view) {
        mSectionsList = (RecyclerView) view.findViewById(R.id.sections_list);
        int gridUnit = AppUtility.getScreenGridUnitBy32(getActivity());
        mSectionsList.setPadding(gridUnit, gridUnit, gridUnit, gridUnit);
        mSectionsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<String> mSectionsArrayList;
        if(mTopicList == null) {
            String[] mSectionsArray = getResources().getStringArray(R.array.sections_array);
            mSectionsArrayList = new ArrayList<String>(Arrays.asList(mSectionsArray));
        } else {
            mSectionsArrayList = mTopicList;
        }

        /*//add top section names
        ArrayList<SectionModel> sections = new ArrayList<>();
        for(int i=0 ; i < mSectionsArrayList.size(); i++) {
            sections.add(setRealmData("TopSections", mSectionsArrayList.get(i)));
        }

        for (SectionModel section : sections) {
            // Persist your data easily
            mRealm.beginTransaction();
            mRealm.copyToRealm(section);
            mRealm.commitTransaction();
        }*/

        mSectionsAdapter = new SectionsAdapter(getActivity(), mSectionsArrayList, this);
        mSectionsList.setAdapter(mSectionsAdapter);
    }

    private SectionModel setRealmData(String sectionType, String sectionName) {
        SectionModel section = new SectionModel();
        //section.setmSectionId(sectionType + sectionName);
        section.setmSectionName(sectionName);
        section.setmSectionType(sectionType);
        return section;
    }

    @Override
    public void onSectionClicked(String sectionName, String sectionType) {
        handleTopicSelection(sectionName);
    }

    private void handleTopicSelection(String sectionName) {
        String [] mSectionsArray;
        if (sectionName.equalsIgnoreCase("Quantitative")) {
            mSectionsArray = getResources().getStringArray(R.array.section_quantitative);
            handleMoreSubsections(sectionName, mSectionsArray);
        } else if (sectionName.equalsIgnoreCase("Verbal")) {
            mSectionsArray = getResources().getStringArray(R.array.section_verbal);
            handleMoreSubsections(sectionName, mSectionsArray);
        } else if (sectionName.equalsIgnoreCase("Algebra")) {
            mSectionsArray = getResources().getStringArray(R.array.section_algebra);
            handleMoreSubsections(sectionName, mSectionsArray);
        } else if (sectionName.equalsIgnoreCase("Geometry")) {
            mSectionsArray = getResources().getStringArray(R.array.section_geometry);
            handleMoreSubsections(sectionName, mSectionsArray);
        } else if (sectionName.equalsIgnoreCase("Number Properties")) {
            mSectionsArray = getResources().getStringArray(R.array.section_number_properties);
            handleMoreSubsections(sectionName, mSectionsArray);
        } else if (sectionName.equalsIgnoreCase("Word Problems")) {
            mSectionsArray = getResources().getStringArray(R.array.section_word_problems);
            handleMoreSubsections(sectionName, mSectionsArray);
        } else if (sectionName.equalsIgnoreCase("Fractions, Decimals & Percents")) {
            mSectionsArray = getResources().getStringArray(R.array.section_fractions_decimals);
            handleMoreSubsections(sectionName, mSectionsArray);
        } else if (sectionName.equalsIgnoreCase("Critical Reasoning")) {
            mSectionsArray = getResources().getStringArray(R.array.section_critical_reasoning);
            handleMoreSubsections(sectionName, mSectionsArray);
        } else if (sectionName.equalsIgnoreCase("Reading Comprehension")) {
            mSectionsArray = getResources().getStringArray(R.array.section_reading_comprehension);
            handleMoreSubsections(sectionName, mSectionsArray);
        } else if (sectionName.equalsIgnoreCase("Sentence Correction")) {
            mSectionsArray = getResources().getStringArray(R.array.section_sentence_correction);
            handleMoreSubsections(sectionName, mSectionsArray);
        } else {
            showQuestionCountDialog(sectionName);
        }
    }

    private void handleMoreSubsections(String sectionName, String[] mSectionsArray) {
        ArrayList<String> mSectionsArrayList = new ArrayList<String>(Arrays.asList(mSectionsArray));
        Intent intent = new Intent(getActivity(), SelectTopicActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_TOPIC_NAME, sectionName);
        intent.putStringArrayListExtra(Constants.INTENT_EXTRA_TOPIC_LIST, mSectionsArrayList);
        startActivity(intent);
    }

    private void showQuestionCountDialog(String sectionName) {
      /*  AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        final EditText edittext = new EditText(getActivity());
        int gridUnit = AppUtility.getScreenGridUnit(getActivity());
        edittext.setPadding(gridUnit, 0, gridUnit, 0);
        alert.setMessage("Enter the average time in minutes to your question");
        //alert.setTitle("Enter Your Title");

        alert.setView(edittext);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = edittext.getText().toString();
                startTimerActivity(value);
            }
        });

        alert.show();*/

        Intent i = new Intent(getActivity(), QuestionCountActivity.class);
        i.putExtra(Constants.INTENT_EXTRA_TOPIC_NAME, sectionName);
        startActivity(i);

    }

    private void startTimerActivity(String value) {
        Intent i = new Intent(getActivity(), TimerActivity.class);
        i.putExtra(Constants.INTENT_EXTRA_TOTAL_TIME, value);
        startActivity(i);
    }
}
