package com.edumage.bmstu_enrollee.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.edumage.bmstu_enrollee.Adapters.DisciplineAdapter;
import com.edumage.bmstu_enrollee.DataTransformator;
import com.edumage.bmstu_enrollee.Discipline;
import com.edumage.bmstu_enrollee.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StatsFragment extends Fragment  {

    Spinner spinner;
    BarChart barChart;

    LineChart lineChart;
    Discipline discipline;
    DataTransformator dataTransformator;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.stats_screen, container, false);
        spinner=v.findViewById(R.id.disciplines);
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (getContext(),android.R.layout.simple_list_item_1,Discipline.LoadStringArray(getContext()));

        spinner.setAdapter(adapter);



        //spinner.setOnItemClickListener(this);

        lineChart =v.findViewById(R.id.linechart);
        DataTransformator.PassScoreComponent passScoreComponent = DataTransformator.LoadSetPassScore();

        LineDataSet dataSet = new LineDataSet(passScoreComponent.getEntries(),"Проходной балл на бюджет");
        LineData lineData = new LineData(dataSet);



        dataSet.setLineWidth(3f);
        dataSet.setCircleColor(getContext().getResources().getColor(R.color.darkGreen));
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setColor(getContext().getResources().getColor(R.color.darkGreen));


        lineChart.setData(lineData);
        lineChart.setScaleEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);








        lineChart.animateY(1000,Easing.EaseInBounce);


        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }



}
