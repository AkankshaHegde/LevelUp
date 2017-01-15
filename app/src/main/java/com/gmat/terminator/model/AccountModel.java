package com.gmat.terminator.model;

import io.realm.RealmObject;

/**
 * Created by Akanksha Hegde on 14-01-2017.
 */

public class AccountModel extends RealmObject {
    private String firstName;
    private String lastName;

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
}
