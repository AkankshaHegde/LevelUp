package com.gmat.terminator.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmat.terminator.R;
import com.gmat.terminator.adapter.SectionsAdapter;
import com.gmat.terminator.interfaces.ISectionClickListener;
import com.gmat.terminator.model.SectionModel;
import com.gmat.terminator.other.RealmController;
import com.gmat.terminator.utils.AppUtility;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;

/**
 * Created by Akanksha on 16-Nov-16.
 */

public class CreateTestFragment extends Fragment implements ISectionClickListener {
    private RecyclerView mSectionsList;
    private SectionsAdapter mSectionsAdapter;
    private Realm mRealm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_test, null);

        //get realm instance
        mRealm = RealmController.with(this).getRealm();

        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        mSectionsList = (RecyclerView) view.findViewById(R.id.sections_list);
        int gridUnit = AppUtility.getScreenGridUnitBy32(getActivity());
        mSectionsList.setPadding(gridUnit, gridUnit, gridUnit, gridUnit);
        mSectionsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        String [] mSectionsArray = getResources().getStringArray(R.array.sections_array);
        ArrayList<String> mSectionsArrayList = new ArrayList<String>(Arrays.asList(mSectionsArray));

        for(int i=0 ; i < mSectionsArrayList.size(); i++) {
            addItemToDatabase(mSectionsArrayList.get(i));
        }

        mSectionsAdapter = new SectionsAdapter(getActivity(), mSectionsArrayList, this);
        mSectionsList.setAdapter(mSectionsAdapter);
    }

    private void addItemToDatabase(String sectionName) {
        mRealm.beginTransaction();
        SectionModel section = mRealm.createObject(SectionModel.class);
        section.setSectionType("Topics");
        section.setSectionName(sectionName);
        mRealm.commitTransaction();
    }

    @Override
    public void onSectionClicked(String sectionName, String sectionType) {

    }
}
