package com.emiliano.exerciseone.models;

import java.sql.Date;

public class Person {

    private int dni;
    private String firstName;
    private String lastName;
    private Date birthDate;

    public Person() {
    }

    public Person(int dni, String firstName, String lastName, Date birthDate) {
        this.dni = dni;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
