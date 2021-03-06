package com.edumage.bmstu_enrollee.Fragments;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.edumage.bmstu_enrollee.Adapters.DisciplineAdapter;
import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;
import com.edumage.bmstu_enrollee.Discipline;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.DisciplinesViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DisciplineFragment extends Fragment implements View.OnClickListener, DisciplineAdapter.DisciplineCardClick {

    private DisciplineAdapter adapter;
    //static final String TAG = "DisciplineFragment";
    private DisciplinesViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new DisciplineAdapter(this);

        model = new ViewModelProvider(this).get(DisciplinesViewModel.class);

        model.loadData();
        model.updateChosenSubjects();
        model.getExamPoints().observe(this, new Observer<List<ExamPoints>>() {
            @Override
            public void onChanged(List<ExamPoints> examPoints) {
                if (examPoints != null)
                    model.applyChosenSubjects(examPoints);
            }
        });


        model.getChosenPrograms().observe(this, new Observer<List<ChosenProgram>>() {
            @Override
            public void onChanged(List<ChosenProgram> chosenPrograms) {
                if (chosenPrograms != null)
                    model.applyChosenProgram(chosenPrograms);
            }
        });

        model.getData().observe(this, new Observer<ArrayList<Discipline>>() {
            @Override
            public void onChanged(ArrayList<Discipline> disciplines) {
                adapter.setData(disciplines);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.discipline_fragment, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.discipline_dialog_recyclerview);
        recyclerView.setAdapter(adapter);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
        }

        Button confirmButton = v.findViewById(R.id.button);
        confirmButton.setOnClickListener(this);
        Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setTitle(R.string.disciplines);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBack();
            }
        });


        final TextView noData_textView = v.findViewById(R.id.no_discipline_textView);
        model.getData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Discipline>>() {
            @Override
            public void onChanged(ArrayList<Discipline> disciplines) {
                if (disciplines.size() == 0) {
                    noData_textView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                } else {
                    noData_textView.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
        return v;
    }

    @Override
    public void onClick(View v) {
        if (getChosenDisciplines() > 3) {
            Toast.makeText(getContext(), R.string.disciplines_alert, Toast.LENGTH_SHORT).show();
        } else {
            model.replaceAllPrograms(adapter.getData());
            navigateBack();
        }
    }

    @Override
    public int getChosenDisciplines() {
        return adapter.getEnabled().size();
    }

    private void navigateBack() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_disciplineFragment_to_home_tab);
    }
}
