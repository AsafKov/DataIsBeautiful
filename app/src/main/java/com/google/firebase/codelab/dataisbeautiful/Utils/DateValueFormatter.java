package com.google.firebase.codelab.dataisbeautiful.Utils;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;

public class DateValueFormatter extends IndexAxisValueFormatter {

    private final String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                                                    "Aug", "Sep", "Oct", "Nov", "Dec"};

    private ArrayList<String> currentIndices;

    /**
     * TODO: Check why is called twice for every index
     * @param value - Current x value
     * @return The dayOfMonth representing the index, or the name of month
     */
    @Override
    public String getFormattedValue(float value) {
        return currentIndices.get((int)value);
    }

    public void setCurrentIndices(ArrayList<String> indices) {
        this.currentIndices = indices;
    }
}
