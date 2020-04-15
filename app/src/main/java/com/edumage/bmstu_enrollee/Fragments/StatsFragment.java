package com.edumage.bmstu_enrollee.Fragments;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

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
import dalvik.system.DexClassLoader;

public class StatsFragment extends Fragment implements View.OnClickListener {

    Spinner spinner;
    BarChart barChart;
    CheckBox budgetBox;
    CheckBox targetBox;
    Button updateButton;

    private static final String SPINNER_VALUE="SPINNER_VALUE";
    private static final String BUDGET_BOX_VALUE="BUDGET_BOX_VALUE";
    private static final String TARGET_BOX_VALUE="TARGET_BOX_VALUE";



    LineChart lineChart;
    String discipline;
    boolean budgetBoxValue=true;
    boolean targetBoxValue=false;
    DataTransformator dataTransformator;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState!=null){
            budgetBoxValue=savedInstanceState.getBoolean(BUDGET_BOX_VALUE);
            targetBoxValue=savedInstanceState.getBoolean(TARGET_BOX_VALUE);
            discipline=savedInstanceState.getString(SPINNER_VALUE);
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.stats_screen, container, false);

        lineChart =v.findViewById(R.id.linechart);
        spinner=v.findViewById(R.id.disciplines);
        budgetBox=v.findViewById(R.id.checkBoxBudget);
        targetBox=v.findViewById(R.id.checkBoxTarget);
        updateButton =v.findViewById(R.id.update_button);
        updateButton.setOnClickListener(this);

        budgetBox.setChecked(budgetBoxValue);
        targetBox.setChecked(targetBoxValue);

        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (getContext(),android.R.layout.simple_spinner_item,Discipline.LoadStringArray(getContext()));

        spinner.setAdapter(adapter);
        if (discipline!=null) {
            spinner.setSelection(adapter.getPosition(discipline));
            UpdateChart(spinner.getSelectedView().toString(),budgetBox.isChecked(),targetBox.isChecked());
        }

        lineChart.setNoDataText(getString(R.string.stats_screen_no_data));
        lineChart.setNoDataTextColor(getContext().getResources().getColor(R.color.gray));
        lineChart.setHorizontalScrollBarEnabled(true);






        return v;
    }

    private void UpdateChart(String discipline, boolean budget, boolean target ){

        LineData lineData = new LineData();

        if (budget){
            DataTransformator.PassScoreComponent scoreComponent = DataTransformator.LoadSetPassScore(discipline,0);
            LineDataSet dataSet = new LineDataSet(scoreComponent .getEntries(),
                    getResources().getString(R.string.stats_screen_budget_label));
            dataSet.setLineWidth(3f);

            dataSet.setCircleColor(getContext().getResources().getColor(R.color.darkGreen));
            dataSet.setColor(getContext().getResources().getColor(R.color.darkGreen));
            dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

            lineData.addDataSet(dataSet);

        }
        if(target){
            DataTransformator.PassScoreComponent scoreComponent  = DataTransformator.LoadSetPassScore(discipline,1);
            LineDataSet dataSet = new LineDataSet(scoreComponent .getEntries(),
                    getResources().getString(R.string.stats_screen_target_label));

            dataSet.setLineWidth(3f);

            dataSet.setCircleColor(getContext().getResources().getColor(R.color.targetYellow));
            dataSet.setColor(getContext().getResources().getColor(R.color.targetYellow));
            dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

            lineData.addDataSet(dataSet);
        }




        lineChart.setData(lineData);
        Description desc =  new Description();
        desc.setText(getString(R.string.stats_screen_description));
        desc.setTextAlign(Paint.Align.RIGHT);
        lineChart.setDescription(desc);




        lineChart.animateY(1200,Easing.EaseOutCubic);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putBoolean(BUDGET_BOX_VALUE, budgetBox.isChecked());
        outState.putBoolean(TARGET_BOX_VALUE,targetBox.isChecked());
        outState.putString(SPINNER_VALUE,spinner.getSelectedItem().toString());
        super.onSaveInstanceState(outState);

    }


    @Override
    public void onClick(View v) {
        int id =v.getId();
        if (id==R.id.update_button){
            UpdateChart(spinner.getSelectedView().toString(),budgetBox.isChecked(),targetBox.isChecked());
        }
    }
}
