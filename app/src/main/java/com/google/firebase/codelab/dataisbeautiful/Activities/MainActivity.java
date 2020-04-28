package com.google.firebase.codelab.dataisbeautiful.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.codelab.dataisbeautiful.Model.User;
import com.google.firebase.codelab.dataisbeautiful.R;
import com.google.firebase.codelab.dataisbeautiful.Utils.DateValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = "MainActivity";
    public static final String PREFERENCE_USER_KEY = "DataIsBeautifulUser";
    public static final String PREFERENCE_USER_ENTRIES_KEY = "DataIsBeautifulUserEntries";

    private FirebaseUser mFirebaseUser;
    private User mUser;
    private LineChart mPersonalLineChart;

    private Spinner mFocusSpinner;
    private RatingBar mRatingBar;
    private Button mRateBtn;
    private TextView mPreviousDayTv, mPreviousDayConclusionTv;

    private ArrayList<Entry> mEntries, mWeekEntries, mMonthEntries;
    private ArrayList<String> mIndices, mWeekIndices, mMonthIndices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mFirebaseUser == null){
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        }

        mEntries = new ArrayList<>();
        mWeekEntries = new ArrayList<>();
        mMonthEntries = new ArrayList<>();
        mIndices = new ArrayList<>();
        mWeekIndices = new ArrayList<>();
        mMonthIndices = new ArrayList<>();

        readPersonalData();
        initializeViews();
    }

    public void initializeViews(){
        configureChart();

        mFocusSpinner = findViewById(R.id.activity_main_quick_focus_filter);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_selected_item,
                getResources().getStringArray(R.array.quick_focus_spinner));
        adapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        mFocusSpinner.setAdapter(adapter);
        mFocusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                XAxis axis = mPersonalLineChart.getXAxis();
                DateValueFormatter valueFormatter = (DateValueFormatter) axis.getValueFormatter();
                switch (position){
                    case 0: {
                        mPersonalLineChart.clear();
                        valueFormatter.setCurrentIndices(mIndices);
                        addDataSet(mEntries);
                        axis.setAxisMaximum(mEntries.size()-1);
                        mPersonalLineChart.setVisibleXRangeMaximum(axis.getLabelCount());
                        mPersonalLineChart.setVisibleXRangeMinimum(axis.getLabelCount());
                    } break;
                    case 1: {
                        mPersonalLineChart.clear();
                        addDataSet(mWeekEntries);
                        axis.setAxisMaximum(mWeekEntries.size()-1);
                        valueFormatter.setCurrentIndices(mWeekIndices);
                        mPersonalLineChart.setVisibleXRangeMaximum(axis.getLabelCount());
                        mPersonalLineChart.setVisibleXRangeMinimum(axis.getLabelCount());
                    }
                    break;
                    case 2: {
                        addDataSet(mMonthEntries);
                        axis.setAxisMaximum(mMonthEntries.size()-1);
                        valueFormatter.setCurrentIndices(mMonthIndices);
                        mPersonalLineChart.setVisibleXRangeMaximum(axis.getLabelCount());
                        mPersonalLineChart.setVisibleXRangeMinimum(axis.getLabelCount());
                    } break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mRatingBar = findViewById(R.id.activity_main_rate_bar);

        mRateBtn = findViewById(R.id.activity_main_rate_btn);
        mRateBtn.setOnClickListener(view -> {
            SharedPreferences preferences = getSharedPreferences(PREFERENCE_USER_ENTRIES_KEY, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
            calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
            editor.putFloat(String.valueOf(calendar.getTimeInMillis()), mRatingBar.getRating());
            editor.commit();
        });
    }

    private void configureChart(){
        mPersonalLineChart = findViewById(R.id.activity_main_personal_chart);
        //mUser.getEntries().forEach((day, rating) -> mEntries.add(new Entry(day, rating)));

        XAxis axis = mPersonalLineChart.getXAxis();
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new DateValueFormatter());
        axis.setDrawGridLines(false);
        axis.setTextColor(Color.WHITE);
        axis.setLabelCount(15, true);

        mPersonalLineChart.getAxisRight().setDrawLabels(false);
        mPersonalLineChart.getAxisRight().setDrawGridLines(false);
        mPersonalLineChart.getAxisLeft().setDrawGridLines(false);

        mPersonalLineChart.setTouchEnabled(true);
        mPersonalLineChart.getAxisLeft().setAxisMinimum(0);
        mPersonalLineChart.getAxisLeft().setAxisMaximum(10);
        mPersonalLineChart.getAxisLeft().setTextColor(Color.WHITE);
        mPersonalLineChart.getLegend().setTextColor(Color.WHITE );
    }

    private void addDataSet(ArrayList<Entry> entries){
        LineDataSet dataSet = new LineDataSet(entries, "Rating");
        XAxis axis = mPersonalLineChart.getXAxis();

        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawValues(false);
        dataSet.setDrawFilled(true);

        LineData data = new LineData(dataSet);
        mPersonalLineChart.setData(data);
        mPersonalLineChart.setVisibleXRangeMaximum(axis.getLabelCount());
        mPersonalLineChart.setVisibleXRangeMinimum(axis.getLabelCount());
        mPersonalLineChart.moveViewToX(mEntries.size() - axis.getLabelCount());
        mPersonalLineChart.invalidate();
    }

    private void generateFocusedEntries(ArrayList<Long> indices, ArrayList<Integer> values){
        Calendar calendar = Calendar.getInstance();

        final String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov", "Dec"};

        mWeekEntries = new ArrayList<>();
        mMonthEntries = new ArrayList<>();

        int sumOfWeek, sumOfMonth;
        int countInWeek, countInMonth;
        int countWeeks, countMonths;
        sumOfWeek = sumOfMonth = countInWeek = countInMonth = countWeeks = countMonths = 0;

        for(int i = 0; i < indices.size(); i++){
            calendar.setTimeInMillis(indices.get(i));

            if(calendar.get(Calendar.DAY_OF_MONTH) == 1){
                if(countInMonth != 0)
                    mMonthEntries.add(new Entry(countMonths, sumOfMonth/countInMonth));
                if(countInWeek != 0)
                    mWeekEntries.add(new Entry(countWeeks, sumOfWeek/countInWeek));
                mMonthIndices.add(months[calendar.get(Calendar.MONTH)]);
                mWeekIndices.add(months[calendar.get(Calendar.MONTH)]);
                mIndices.add(months[calendar.get(Calendar.MONTH)]);
                countWeeks++;
                countMonths++;
                sumOfWeek = sumOfMonth = countInWeek = countInMonth = 0;
            } else{
                if(calendar.get(Calendar.DAY_OF_WEEK) == 1 && countInWeek != 0){
                    mWeekEntries.add(new Entry(countWeeks, sumOfWeek/countInWeek));
                    mWeekIndices.add(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)-1));
                    countWeeks++;
                    sumOfWeek = countInWeek = 0;
                }
            }

            mIndices.add(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));

            if(values.get(i) == -1) {
                mEntries.add(new Entry(i, -100000f));
                continue;
            }

            mEntries.add(new Entry(i, values.get(i)));

            sumOfWeek += values.get(i);
            sumOfMonth += values.get(i);
            countInWeek++;
            countInMonth++;
        }
    }

    public void readPersonalData(){
        SharedPreferences preferences = getSharedPreferences(PREFERENCE_USER_KEY, Context.MODE_PRIVATE);
        mUser = new User(preferences.getString("gender", ""),
                preferences.getString("country", ""),
                preferences.getInt("age", 0));

        preferences = getSharedPreferences(PREFERENCE_USER_ENTRIES_KEY, Context.MODE_PRIVATE);
        Map<String, Integer> entries = (Map<String, Integer>) preferences.getAll();
        ArrayList<Integer> values = new ArrayList<>(entries.size());
        ArrayList<Long> indices = new ArrayList<>(entries.size());
//        entries.forEach((key, value) ->{
//            values.add(value);
//            indices.add(Long.valueOf(key));
//        });

        // Generating random data
        long dayInMs = 24*60*60*1000;
        double[] probability = new double[]{0.01, 0.02, 0.04, 0.20, 0.35, 0.50, 0.75, 0.9, 0.95, 1};
        double cube;
        for(int i=0; i<900; i++){
            if((int)(Math.random()*27) == 6){
                values.add(-1); //padding
            } else{
                cube = Math.random();
                for (int j=0; j<probability.length; j++){
                    if (cube < probability[j]){
                        values.add(j+1);
                        break;
                    }
                }
            }
            indices.add(System.currentTimeMillis() - dayInMs*(900-i));
        }
        generateFocusedEntries(indices, values);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }
}
