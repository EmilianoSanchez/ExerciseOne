package com.emiliano.exerciseone.models;

import java.sql.Date;

public class Employee extends Person {

    private Date startingDate;

    public Employee(int dni, String firstName, String lastName, Date birthDate, Date startingDate) {
        super(dni, firstName, lastName, birthDate);
        this.startingDate = startingDate;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }
}
