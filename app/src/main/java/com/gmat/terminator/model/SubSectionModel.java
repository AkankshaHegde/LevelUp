package com.gmat.terminator.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Akanksha Hegde on 12-12-2016.
 */

public class SubSectionModel extends RealmObject {
    @PrimaryKey
    private String subSecName;
    private String type;
    private int numberOfQuestions;
    private int totalMilliSeconds;
    private String templateName;
    private String mSectionName;

    public String getsubSecName() {
        return subSecName;
    }

    public void setsubSecName(String subSecName) {
        this.subSecName = subSecName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public int getTotalMilliSeconds() {
        return totalMilliSeconds;
    }

    public void setTotalMilliSeconds(int totalMilliSeconds) {
        this.totalMilliSeconds = totalMilliSeconds;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getmSectionName() {
        return mSectionName;
    }

    public void setmSectionName(String mSectionName) {
        this.mSectionName = mSectionName;
    }
}
