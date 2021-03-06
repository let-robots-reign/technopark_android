package com.edumage.bmstu_enrollee.Fragments;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toolbar;

import com.edumage.bmstu_enrollee.Adapters.EGEAdapter;
import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;
import com.edumage.bmstu_enrollee.EGESubject;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.EgeSubjectsViewModel;

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

public class EgeFragment extends Fragment implements View.OnClickListener {

    private EGEAdapter adapter;
    private EgeSubjectsViewModel model;


    // static final String TAG = "DialogEgeFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //LoadData();
        adapter = new EGEAdapter();
        model = new ViewModelProvider(this).get(EgeSubjectsViewModel.class);

        model.loadData();
        model.getExamPoints().observe(this, new Observer<List<ExamPoints>>() {
            @Override
            public void onChanged(List<ExamPoints> examPoints) {
                if (examPoints != null)
                    model.applyEgeScore(examPoints);
                model.getData().observe(EgeFragment.this, new Observer<ArrayList<EGESubject>>() {
                    @Override
                    public void onChanged(ArrayList<EGESubject> egeSubjects) {
                        adapter.setData(egeSubjects);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ege_fragment, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.ege_dialog_recyclerview);
        recyclerView.setAdapter(adapter);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
        }
        Button confirmBtn = v.findViewById(R.id.button);
        confirmBtn.setOnClickListener(this);

        Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setTitle(R.string.ege_results);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBack();
            }
        });

        return v;
    }

    @Override
    public void onClick(View v) {
        model.replaceAllPoints(adapter.getPassed());
        navigateBack();
    }

    private void navigateBack() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_egeFragment_to_home_tab);
    }
}
