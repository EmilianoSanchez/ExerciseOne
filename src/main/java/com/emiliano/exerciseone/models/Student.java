package com.emiliano.exerciseone.models;

import java.sql.Date;

public class Student extends Person {

    private int currentYear;

    public Student() {
    }

    public Student(int dni, String firstName, String lastName, Date birthDate, int currentYear) {
        super(dni, firstName, lastName, birthDate);
        this.currentYear = currentYear;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }
}
