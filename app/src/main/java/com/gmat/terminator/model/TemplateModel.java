package com.gmat.terminator.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Akanksha on 11-Jan-17.
 */

public class TemplateModel extends RealmObject {

    @PrimaryKey
    private String templateName;
    private String noOfSections;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getNoOfSections() {
        return noOfSections;
    }

    public void setNoOfSections(String noOfSections) {
        this.noOfSections = noOfSections;
    }
}
