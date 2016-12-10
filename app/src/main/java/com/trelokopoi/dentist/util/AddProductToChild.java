package com.trelokopoi.dentist.util;

public class AddProductToChild {


    String childName = null;
    boolean selected = false;
    String gender = null;

    public AddProductToChild(String childName, boolean selected, String gender) {
        super();
        this.childName = childName;
        this.selected = selected;
        this.gender = gender;
    }

    public String getChildName() {
        return childName;
    }
    public void setName(String childName) {
        this.childName = childName;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getChildGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}

