package com.gmat.terminator.realm;

import io.realm.RealmObject;

/**
 * Created by Akanksha Hegde on 12-12-2016.
 */

public class SubSectionModel extends RealmObject {
    private String id;
    private String name;
    private String type;
    private int numberOfQuestions;
    private int totalMilliSeconds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
