package com.gmat.terminator.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Akanksha on 06-Dec-16.
 */

public class SectionModel extends RealmObject{
    @PrimaryKey
    private String mSectionId;
    private String mSectionName;
    private String mSectionType;
    private String mNoOfQuestions;
    private String mTimePerSection;

    public String getmSectionId() {
        return mSectionId;
    }

    public void setmSectionId(String mSectionId) {
        this.mSectionId = mSectionId;
    }

    public String getmSectionName() {
        return mSectionName;
    }

    public void setmSectionName(String mSectionName) {
        this.mSectionName = mSectionName;
    }

    public String getmSectionType() {
        return mSectionType;
    }

    public void setmSectionType(String mSectionType) {
        this.mSectionType = mSectionType;
    }

    public String getmNoOfQuestions() {
        return mNoOfQuestions;
    }

    public void setmNoOfQuestions(String mNoOfQuestions) {
        this.mNoOfQuestions = mNoOfQuestions;
    }

    public String getmTimePerSection() {
        return mTimePerSection;
    }

    public void setmTimePerSection(String mTimePerSection) {
        this.mTimePerSection = mTimePerSection;
    }
}
