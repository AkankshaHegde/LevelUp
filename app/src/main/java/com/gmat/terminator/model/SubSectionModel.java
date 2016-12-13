package com.gmat.terminator.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Akanksha Hegde on 12-12-2016.
 */

public class SubSectionModel extends RealmObject {
    @PrimaryKey
    private String subSecId;
    private String sectionId;
    private String name;
    private String type;
    private int numberOfQuestions;
    private int totalMilliSeconds;

    public String getsubSecId() {
        return subSecId;
    }

    public void setsubSecId(String subSectionId) {
        this.subSecId = subSectionId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
