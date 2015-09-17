package com.ciandt.app.freeroom.model;

/**
 * Created by Garage on 10/09/15.
 */
public class Building {
    private String name;
    private String parameter;
    private boolean selected;

    public Building(String name, String parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
