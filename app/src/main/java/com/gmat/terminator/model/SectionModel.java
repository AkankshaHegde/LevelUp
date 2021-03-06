package com.gmat.terminator.model;

import io.realm.RealmList;
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
    private int mTimePerSection;
    private String mNoOfSubSections;
    private int perQuestnTimeInSecs;
    private String templateName;
    private int questnIntervalInSecs;
    private RealmList<SectionModel> mSectionsList;

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

    public int getmTimePerSection() {
        return mTimePerSection;
    }

    public void setmTimePerSection(int mTimePerSection) {
        this.mTimePerSection = mTimePerSection;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getmNoOfSubSections() {
        return mNoOfSubSections;
    }

    public void setmNoOfSubSections(String mNoOfSubSections) {
        this.mNoOfSubSections = mNoOfSubSections;
    }

    public RealmList<SectionModel> getmSectionsList() {
        return mSectionsList;
    }

    public void setmSectionsList(RealmList<SectionModel> mSectionsList) {
        this.mSectionsList = mSectionsList;
    }

    public int getPerQuestnTimeInSecs() {
        return perQuestnTimeInSecs;
    }

    public void setPerQuestnTimeInSecs(int perQuestnTimeInSecs) {
        this.perQuestnTimeInSecs = perQuestnTimeInSecs;
    }

    public int getQuestnIntervalInSecs() {
        return questnIntervalInSecs;
    }

    public void setQuestnIntervalInSecs(int questnIntervalInSecs) {
        this.questnIntervalInSecs = questnIntervalInSecs;
    }
}
