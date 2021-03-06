package com.edumage.bmstu_enrollee.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;
import com.edumage.bmstu_enrollee.EGESubject;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.EgeSubjectsViewModel;
import com.edumage.bmstu_enrollee.WelcomeActivity;

import java.util.ArrayList;
import java.util.List;

import com.edumage.bmstu_enrollee.Adapters.EGEAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LAFragmentSecond extends Fragment implements WelcomeActivity.CompletableFragment {

    // private ArrayList<EGESubject> data;
    private EGEAdapter adapter;

    public static final String TAG = "LAFragmentSecond";
    //private static final String DATA = "SUBJECTS";

    private EgeSubjectsViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* if (savedInstanceState != null) {
            ObjectInputStream objectInputStream;
            try {
                objectInputStream = new ObjectInputStream(
                        new ByteArrayInputStream(savedInstanceState.getByteArray(DATA)));
                data = (ArrayList<EGESubject>) objectInputStream.readObject();
                objectInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                data = EGESubject.LoadEgeSubjects(requireActivity());
                Toast.makeText(getContext(), "Unable to deserialize", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (data == null && getActivity() != null) {
                data = EGESubject.LoadEgeSubjects(getActivity());
            }
        }*/

        adapter = new EGEAdapter();

        model = new ViewModelProvider(this).get(EgeSubjectsViewModel.class);

        if (savedInstanceState == null) {
            model.loadData();
        }

        model.getExamPoints().observe(this, new Observer<List<ExamPoints>>() {
            @Override
            public void onChanged(List<ExamPoints> examPoints) {
                if (examPoints != null)
                    model.applyEgeScore(examPoints);
                model.getData().observe(LAFragmentSecond.this, new Observer<ArrayList<EGESubject>>() {
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
        View rootView = inflater.inflate(R.layout.la_fragment2, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.ege_list);
        recyclerView.setAdapter(adapter);

        Context appContext = getContext();
        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(appContext, 2, RecyclerView.VERTICAL, false));
        }

        return rootView;
    }

    @Override
    public void onResume() {


        super.onResume();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onStop() {
        model.replaceAllPoints(adapter.getPassed());
        super.onStop();
    }


    @Override
    public boolean isComplete() {
        model.replaceAllPoints(adapter.getPassed());
        return true;
    }
}
