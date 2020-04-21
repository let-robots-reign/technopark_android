package com.edumage.bmstu_enrollee.Fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.edumage.bmstu_enrollee.DataTransformator;
import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.Discipline;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.StatsFragmentViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

public class StatsFragment extends Fragment implements View.OnClickListener {

    private Spinner spinner;
    private CheckBox budgetBox;
    private CheckBox targetBox;

    private static final String SPINNER_VALUE = "SPINNER_VALUE";
    private static final String BUDGET_BOX_VALUE = "BUDGET_BOX_VALUE";
    private static final String TARGET_BOX_VALUE = "TARGET_BOX_VALUE";

    private LineChart lineChart;
    private String discipline;
    private boolean budgetBoxValue;
    private boolean targetBoxValue ;

    private List<ChosenProgram> chosenProgramList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            budgetBoxValue = savedInstanceState.getBoolean(BUDGET_BOX_VALUE);
            targetBoxValue = savedInstanceState.getBoolean(TARGET_BOX_VALUE);
            discipline = savedInstanceState.getString(SPINNER_VALUE);
        } else {
            budgetBoxValue = true;
            targetBoxValue = false;

        }

        StatsFragmentViewModel model = ViewModelProviders.of(this).get(StatsFragmentViewModel.class);
        chosenProgramList = model.getAllChosenPrograms();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.stats_screen, container, false);

        lineChart = v.findViewById(R.id.linechart);
        spinner = v.findViewById(R.id.disciplines);
        budgetBox = v.findViewById(R.id.checkBoxBudget);
        targetBox = v.findViewById(R.id.checkBoxTarget);
        Button updateButton = v.findViewById(R.id.update_button);
        updateButton.setOnClickListener(this);

        budgetBox.setChecked(budgetBoxValue);
        targetBox.setChecked(targetBoxValue);

        List<String> programsNames = new ArrayList<>();
        for (ChosenProgram program : chosenProgramList) {
            programsNames.add(program.getProgramName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, programsNames);

        spinner.setAdapter(adapter);
        if (discipline != null) {
            spinner.setSelection(adapter.getPosition(discipline));
            UpdateChart(discipline, budgetBox.isChecked(), targetBox.isChecked());
        }

        lineChart.setNoDataText(getString(R.string.stats_screen_no_data));
        lineChart.setNoDataTextColor(getActivity().getResources().getColor(R.color.gray));
        lineChart.setHorizontalScrollBarEnabled(true);

        return v;
    }

    private void UpdateChart(String discipline, boolean budget, boolean target) {

        LineData lineData = new LineData();

        if (budget) {
            DataTransformator.PassScoreComponent scoreComponent = DataTransformator.LoadSetPassScore(discipline, 0);
            LineDataSet dataSet = new LineDataSet(scoreComponent.getEntries(),
                    getResources().getString(R.string.stats_screen_budget_label));
            dataSet.setLineWidth(3f);

            dataSet.setCircleColor(getResources().getColor(R.color.darkGreen));
            dataSet.setColor(getResources().getColor(R.color.darkGreen));
            dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            lineData.addDataSet(dataSet);
        }
        if (target) {
            DataTransformator.PassScoreComponent scoreComponent = DataTransformator.LoadSetPassScore(discipline, 1);
            LineDataSet dataSet = new LineDataSet(scoreComponent.getEntries(),
                    getResources().getString(R.string.stats_screen_target_label));

            dataSet.setLineWidth(3f);

            dataSet.setCircleColor(getResources().getColor(R.color.targetYellow));
            dataSet.setColor(getResources().getColor(R.color.targetYellow));
            dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            lineData.addDataSet(dataSet);
        }

        lineChart.setData(lineData);
        Description desc = new Description();
        desc.setText(getString(R.string.stats_screen_description));
        desc.setTextAlign(Paint.Align.RIGHT);
        lineChart.setDescription(desc);
        lineChart.animateY(1200, Easing.EaseOutCubic);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        lineChart.getLegend().setWordWrapEnabled(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(BUDGET_BOX_VALUE, budgetBox.isChecked());
        outState.putBoolean(TARGET_BOX_VALUE, targetBox.isChecked());
        outState.putString(SPINNER_VALUE, spinner.getSelectedItem().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.update_button) {
            UpdateChart(spinner.getSelectedView().toString(), budgetBox.isChecked(), targetBox.isChecked());
        }
    }
}
