package com.trelokopoi.dentist.util;

public class AddProductToChild {


    String childName = null;
    boolean selected = false;

    public AddProductToChild(String childName, boolean selected) {
        super();
        this.childName = childName;
        this.selected = selected;
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

}

