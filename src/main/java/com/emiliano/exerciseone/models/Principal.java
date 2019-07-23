package com.emiliano.exerciseone.models;

import java.sql.Date;

public class Principal extends Employee {

    private String schoolInCharge;

    public Principal(int dni, String firstName, String lastName, Date birthDate, Date startingDate, String schoolInCharge) {
        super(dni, firstName, lastName, birthDate, startingDate);
        this.schoolInCharge = schoolInCharge;
    }

    public String getSchoolInCharge() {
        return schoolInCharge;
    }

    public void setSchoolInCharge(String schoolInCharge) {
        this.schoolInCharge = schoolInCharge;
    }
}
