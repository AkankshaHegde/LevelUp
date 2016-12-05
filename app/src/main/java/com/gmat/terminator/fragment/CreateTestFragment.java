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

import java.util.ArrayList;

/**
 * Created by Akanksha on 16-Nov-16.
 */

public class CreateTestFragment extends Fragment {
    private RecyclerView mSectionsList;
    private SectionsAdapter mSectionsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_test, null);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        mSectionsList = (RecyclerView) view.findViewById(R.id.sections_list);
        mSectionsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<String> mSectionsArrayList = new ArrayList<>(R.array.sections_array);
        mSectionsAdapter = new SectionsAdapter(getActivity(), mSectionsArrayList);
        mSectionsList.setAdapter(mSectionsAdapter);
    }


}
