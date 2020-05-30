package com.edumage.bmstu_enrollee.Fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.StatsFragmentViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.edumage.bmstu_enrollee.chartingData.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class StatsFragment extends Fragment {

    private Spinner spinner;
    private CheckBox budgetBox;
    private CheckBox targetBox;

    private static final String SPINNER_VALUE = "SPINNER_VALUE";
    private static final String BUDGET_BOX_VALUE = "BUDGET_BOX_VALUE";
    private static final String TARGET_BOX_VALUE = "TARGET_BOX_VALUE";

    private LineChart lineChart;
    private String discipline;
    private boolean budgetBoxValue;
    private boolean targetBoxValue;
    private ProgressBar progressBar;
    private ImageView noConnection;
    private boolean connected = true;

    private List<ChosenProgram> chosenProgramList;
    private StatsFragmentViewModel model;
    private String curProgram;

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

        model = new ViewModelProvider(this).get(StatsFragmentViewModel.class);

        //model.updateProgram();


        model.getHasConnection().observe(StatsFragment.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    connected = true;
                    lineChart.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    noConnection.setVisibility(View.GONE);
                } else {
                    connected = false;
                    lineChart.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.GONE);
                    noConnection.setVisibility(View.VISIBLE);
                }
            }
        });

        // updating the graphic when it's finished parsing
        model.getFinishedParsing().observe(StatsFragment.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    if (connected) {
                        lineChart.setVisibility(View.VISIBLE);
                    }
                    progressBar.setVisibility(View.GONE);
                    //UpdateChart(budgetBox.isChecked(), targetBox.isChecked());
                } else {
                    lineChart.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.stats_screen, container, false);

        lineChart = v.findViewById(R.id.linechart);
        spinner = v.findViewById(R.id.disciplines);
        budgetBox = v.findViewById(R.id.checkBoxBudget);
        targetBox = v.findViewById(R.id.checkBoxTarget);
        progressBar = v.findViewById(R.id.progress);
        noConnection = v.findViewById(R.id.no_connection);


        budgetBox.setChecked(budgetBoxValue);
        targetBox.setChecked(targetBoxValue);

       /* model.getAllChosenPrograms().observe(getViewLifecycleOwner(), new Observer<List<ChosenProgram>>() {
            @Override
            public void onChanged(List<ChosenProgram> chosenPrograms) {

            }
        });*/


        model.chosenProgram.observe(getViewLifecycleOwner(), new Observer<List<ChosenProgram>>() {
            @Override
            public void onChanged(List<ChosenProgram> chosenPrograms) {
                if (chosenPrograms != null) {
                    chosenProgramList = chosenPrograms;
                    // to get the program name from its full name, we need to slice an array of words)
                    if (!chosenProgramList.isEmpty()) {
                        curProgram = getProgramShortName(chosenProgramList.get(0).getProgramName());
                    }

                    if (savedInstanceState == null) {
                        model.init(curProgram);
                    }
                    initViews();
                }
            }
        });


        model.getMainData().observe(getViewLifecycleOwner(), new Observer<List<List<Entry>>>() {
            @Override
            public void onChanged(List<List<Entry>> lists) {

                LineData lineData = new LineData();

                List<Entry> budgetList = lists.get(StatsFragmentViewModel.BUDGET_INDEX);
                LineDataSet dataSet = new LineDataSet(Entry.toEntryList(budgetList),
                        getResources().getString(R.string.stats_screen_budget_label));
                dataSet.setLineWidth(3f);

                dataSet.setCircleColor(getResources().getColor(R.color.darkGreen));
                dataSet.setColor(getResources().getColor(R.color.darkGreen));
                dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                lineData.addDataSet(dataSet);


                List<Entry> entries = lists.get(StatsFragmentViewModel.INDUSTRY_INDEX);
                LineDataSet nextDataSet = new LineDataSet(Entry.toEntryList(entries),
                        getResources().getString(R.string.stats_screen_target_label));
                nextDataSet.setLineWidth(3f);
                nextDataSet.setCircleColor(getResources().getColor(R.color.targetYellow));
                nextDataSet.setColor(getResources().getColor(R.color.targetYellow));
                nextDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                lineData.addDataSet(nextDataSet);

                lineChart.setData(lineData);
                Description desc = new Description();
                desc.setText(getString(R.string.stats_screen_description));
                desc.setTextAlign(Paint.Align.RIGHT);
                lineChart.setDescription(desc);
                lineChart.animateY(1200, Easing.EaseOutCubic);
                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                lineChart.getLegend().setWordWrapEnabled(true);
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        model.updateProgram();
        super.onResume();
    }

    private void initViews() {
        List<String> programsNames = new ArrayList<>();
        for (ChosenProgram program : chosenProgramList) {
            programsNames.add(getProgramShortName(program.getProgramName()));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_spinner_dropdown_item, programsNames);

        spinner.setAdapter(adapter);
        if (discipline != null) {
            spinner.setSelection(adapter.getPosition(discipline));
            //UpdateChart(budgetBox.isChecked(), targetBox.isChecked());
        }

        lineChart.setNoDataText(getString(R.string.stats_screen_no_data));
        lineChart.setNoDataTextColor(requireActivity().getResources().getColor(R.color.gray));
        lineChart.setHorizontalScrollBarEnabled(true);


        budgetBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                noConnection.setVisibility(View.GONE);
                if (spinner.getSelectedItem() != null) {
                    curProgram = spinner.getSelectedItem().toString();
                } else {
                    return;
                }
                if (isChecked) {
                    model.loadBudgetFundedScores(curProgram);
                } else {
                    model.clearBudgetFundedScores();
                }
            }
        });


        targetBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                noConnection.setVisibility(View.GONE);
                if (spinner.getSelectedItem() != null) {
                    curProgram = spinner.getSelectedItem().toString();
                } else {
                    return;
                }
                if (isChecked) {
                    model.loadIndustryFundedScores(curProgram);
                } else {
                    model.clearIndustryFundedScores();
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                noConnection.setVisibility(View.GONE);
                if (spinner.getSelectedItem() != null) {
                    curProgram = spinner.getSelectedItem().toString();
                } else {
                    return;
                }
                if (budgetBox.isChecked()) {
                    model.loadBudgetFundedScores(curProgram);
                }

                if (targetBox.isChecked()) {
                    model.loadIndustryFundedScores(curProgram);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

   /* private void UpdateChart(boolean budget, boolean target) {
        LineData lineData = new LineData();

       if (budget) {
            List<Entry> entries = model.getBudgetFundedScores().getValue();
            LineDataSet dataSet = new LineDataSet(entries,
                    getResources().getString(R.string.stats_screen_budget_label));
            dataSet.setLineWidth(3f);

            dataSet.setCircleColor(getResources().getColor(R.color.darkGreen));
            dataSet.setColor(getResources().getColor(R.color.darkGreen));
            dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            lineData.addDataSet(dataSet);
        }
        if (target) {
            List<Entry> entries = model.getIndustryFundedScores().getValue();
            LineDataSet dataSet = new LineDataSet(entries,
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
    }*/

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(BUDGET_BOX_VALUE, budgetBox.isChecked());
        outState.putBoolean(TARGET_BOX_VALUE, targetBox.isChecked());
        outState.putString(SPINNER_VALUE, spinner.getSelectedItem().toString());
        super.onSaveInstanceState(outState);
    }

    private String getProgramShortName(String name) {
        String[] curProgramWords = name.split(" ");
        // we exclude first and last words
        String[] shortNameList = Arrays.copyOfRange(curProgramWords, 1,
                curProgramWords.length - 1);
        // and then joining the words again
        return TextUtils.join(" ", shortNameList);
    }


}
