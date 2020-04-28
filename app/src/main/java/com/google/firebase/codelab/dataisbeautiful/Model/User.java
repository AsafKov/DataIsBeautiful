package com.google.firebase.codelab.dataisbeautiful.Model;

import java.util.Map;

public class User {

    private String gender, country;
    private int age;
    private Map<Long, Integer> entries;

    public User(String gender, String country, int age) {
        this.gender = gender;
        this.country = country;
        this.age = age;
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

    public Map<Long, Integer> getEntries() {
        return entries;
    }

    public void setEntries(Map<Long, Integer> entries) {
        this.entries = entries;
    }
}
