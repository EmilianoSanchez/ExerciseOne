package com.emiliano.exerciseone.models;

import java.sql.Date;

public class Janitor extends Employee {

    private String workingArea;

    public Janitor(int dni, String firstName, String lastName, Date birthDate, Date startingDate, String workingArea) {
        super(dni, firstName, lastName, birthDate, startingDate);
        this.workingArea = workingArea;
    }

    public String getWorkingArea() {
        return workingArea;
    }

    public void setWorkingArea(String workingArea) {
        this.workingArea = workingArea;
    }
}
