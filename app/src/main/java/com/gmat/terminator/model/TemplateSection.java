package com.gmat.terminator.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Akanksha on 11-Jan-17.
 */

public class TemplateSection extends RealmObject {

    @PrimaryKey
    private String sectionName;
    private int noOfQuestions;
    private String timePerSection;
    private String totalBreakTime;

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public int getNoOfQuestions() {
        return noOfQuestions;
    }

    public void setNoOfQuestions(int noOfQuestions) {
        this.noOfQuestions = noOfQuestions;
    }

    public String getTimePerSection() {
        return timePerSection;
    }

    public void setTimePerSection(String timePerSection) {
        this.timePerSection = timePerSection;
    }

    public String getTotalBreakTime() {
        return totalBreakTime;
    }

    public void setTotalBreakTime(String totalBreakTime) {
        this.totalBreakTime = totalBreakTime;
    }
}
