package com.edumage.bmstu_enrollee.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.edumage.bmstu_enrollee.Adapters.EGEAdapter;
import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;
import com.edumage.bmstu_enrollee.EGESubject;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.LASecondViewModel;
import com.edumage.bmstu_enrollee.WelcomeActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LAFragmentSecond extends Fragment implements WelcomeActivity.CompletableFragment {

    private ArrayList<EGESubject> data;
    private EGEAdapter adapter;

    public static final String TAG = "LAFragmentSecond";
    private static final String DATA = "SUBJECTS";

    private LASecondViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            ObjectInputStream objectInputStream;
            try {
                objectInputStream = new ObjectInputStream(
                        new ByteArrayInputStream(savedInstanceState.getByteArray(DATA)));
                data = (ArrayList<EGESubject>) objectInputStream.readObject();
                objectInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                data = EGESubject.LoadEgeSubjects(getActivity());
                Toast.makeText(getContext(), "Unable to deserialize", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (data == null && getActivity() != null) {
                data = EGESubject.LoadEgeSubjects(getActivity());
            }
        }

        model = ViewModelProviders.of(this).get(LASecondViewModel.class);
        model.deleteAllPoints();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.la_fragment2, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.ege_list);
        adapter = new EGEAdapter(data);
        recyclerView.setAdapter(adapter);

        Context appContext = getContext();
        Configuration configuration = getResources().getConfiguration();
        if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            recyclerView.setLayoutManager(new GridLayoutManager(appContext,2,RecyclerView.VERTICAL,false));
        }




        return rootView;
    }

    @Override
    public void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (adapter!=null){try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream ObjOut = new ObjectOutputStream(baos);
            ObjOut.writeObject(adapter.getData());
            ObjOut.flush();
            outState.putByteArray(DATA, baos.toByteArray());
        } catch (IOException e) {
            Toast.makeText(getContext(), "Unable to serialize", Toast.LENGTH_SHORT).show();
        }}
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean isComplete() {
        // условие завершения
        // выгрузка информации

        List<ExamPoints> points = new ArrayList<>();
        for (EGESubject subject : data) {
            if (subject.isPassed()) {
                points.add(new ExamPoints(subject.getName(), subject.getScore()));
            }
        }

        if (!points.isEmpty()) {
            model.insertAll(points);
        }

        return true;
    }
}
