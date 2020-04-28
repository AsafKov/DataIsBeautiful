package com.google.firebase.codelab.dataisbeautiful.Model;

public class Rating {

    private double value;
    private String gender, country;
    private int age;

    public Rating(){}

    public Rating(double value, String gender, String country, int age) {
        this.value = value;
        this.gender = gender;
        this.country = country;
        this.age = age;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}


