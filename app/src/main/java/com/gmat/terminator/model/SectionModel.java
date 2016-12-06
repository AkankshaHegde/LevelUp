package com.gmat.terminator.model;

/**
 * Created by Akanksha on 06-Dec-16.
 */

public class SectionModel {
    private String mSectionName;
    private String mSectionType;
    private String mSectionTime;
    private String mNoOfQuestions;
    private String mTimePerSection;

    public String getSectionName() {
        return mSectionName;
    }

    public void setSectionName(String mSectionName) {
        this.mSectionName = mSectionName;
    }

    public String getSectionType() {
        return mSectionType;
    }

    public void setSectionType(String mSectionType) {
        this.mSectionType = mSectionType;
    }

    public String getSectionTime() {
        return mSectionTime;
    }

    public void setSectionTime(String mSectionTime) {
        this.mSectionTime = mSectionTime;
    }

    public String getNoOfQuestions() {
        return mNoOfQuestions;
    }

    public void setNoOfQuestions(String mNoOfQuestions) {
        this.mNoOfQuestions = mNoOfQuestions;
    }

    public String getTimePerSection() {
        return mTimePerSection;
    }

    public void setTimePerSection(String mTimePerSection) {
        this.mTimePerSection = mTimePerSection;
    }
}
