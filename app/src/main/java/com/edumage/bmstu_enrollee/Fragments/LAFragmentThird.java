package com.edumage.bmstu_enrollee.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.edumage.bmstu_enrollee.Adapters.DisciplineAdapter;
import com.edumage.bmstu_enrollee.Discipline;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.DisciplinesViewModel;
import com.edumage.bmstu_enrollee.WelcomeActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LAFragmentThird extends Fragment implements WelcomeActivity.CompletableFragment, DisciplineAdapter.DisciplineCardClick {

    private DisciplineAdapter adapter;
    public static final String TAG = "LAFragmentThird";

    private DisciplinesViewModel model;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new DisciplineAdapter( this);
        // get all programm
        model = new ViewModelProvider(this).get(DisciplinesViewModel.class);

        model.loadData();
        model.applyChosenSubjects();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.la_fragment3, container, false);



        RecyclerView recyclerView = v.findViewById(R.id.discipline_list);
        recyclerView.setAdapter(adapter);

        Context appContext = getContext();
        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(appContext, 2, RecyclerView.VERTICAL, false));
        }
        final TextView textView = v.findViewById(R.id.no_discipline_textView);
        model.data.observe(getViewLifecycleOwner(), new Observer<ArrayList<Discipline>>() {
            @Override
            public void onChanged(ArrayList<Discipline> disciplines) {
                adapter.setData(disciplines);
                adapter.notifyDataSetChanged();
                if (disciplines.size()==0){
                    textView.setVisibility(View.VISIBLE);
                } else{
                    textView.setVisibility(View.INVISIBLE);
                }
            }
        });


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        model.loadData();
        model.applyChosenSubjects();
    }

    @Override
    public void onPause() {
        //model.replaceAllPrograms();
        super.onPause();
    }


    @Override
    public boolean isComplete() {

        int count = getChosenDisciplines();
        if (count > 3) {
            Toast.makeText(getActivity(), R.string.alert_discipline_more, Toast.LENGTH_SHORT).show();
            return false;
        } else if (count == 0) {
            Toast.makeText(getActivity(), R.string.alert_discipline_zero, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            model.replaceAllPrograms(adapter.getEnabled());
            return true;
        }
    }

    @Override
    public int getChosenDisciplines() {
        //предыдещее решение не рабоает - некорректно считается chosenDisciplines
        return adapter.getEnabled().size();
    }

   /* @Override
    public void incrementChosen() {
        ++chosenDisciplines;
    }

    @Override
    public void decrementChosen() {
        --chosenDisciplines;
    }*/
}
