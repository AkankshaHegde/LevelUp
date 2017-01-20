package com.gmat.terminator.model;

import io.realm.RealmObject;

/**
 * Created by Akanksha Hegde on 14-01-2017.
 */

public class AccountModel extends RealmObject {
    private String firstName;
    private String lastName;
    private String userImg;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }
}
