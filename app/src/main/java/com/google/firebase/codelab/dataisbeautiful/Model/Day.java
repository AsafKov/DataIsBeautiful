package com.google.firebase.codelab.dataisbeautiful.Model;

import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.Date;

public class Day {

    private long timeStamp;
    private ArrayList<Rating> ratings;

    public Day(long timeStamp) {
        this.timeStamp = timeStamp;
        this.ratings = new ArrayList<>();
    }

    public String toString(){
        Date date = new Date(timeStamp);

        return DateFormat.format("dd/MM", date).toString();
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(ArrayList<Rating> ratings) {
        this.ratings = ratings;
    }
}
