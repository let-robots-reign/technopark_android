package com.edumage.bmstu_enrollee.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.edumage.bmstu_enrollee.Adapters.DisciplineAdapter;
import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.Discipline;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.LAThirdViewModel;
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

public class LAFragmentThird extends Fragment implements WelcomeActivity.CompletableFragment, DisciplineAdapter.DisciplineCardClick {

    private DisciplineAdapter adapter;
    private ArrayList<Discipline> data;
    private int chosenDisciplines;

    private static final String DATA = "DISCIPLINES";
    public static final String TAG = "LAFragmentThird";

    private LAThirdViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            if (getContext() != null && data == null)
                data = Discipline.LoadDisciplines(getContext());
        } else {
            try {
                ObjectInputStream stream =
                        new ObjectInputStream(new ByteArrayInputStream(savedInstanceState.getByteArray(DATA)));
                data = (ArrayList<Discipline>) stream.readObject();
                stream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                data = Discipline.LoadDisciplines(getContext());
            }
        }
        chosenDisciplines = 0; // in the beginning, user chose nothing
        adapter = new DisciplineAdapter(data, this);
        adapter.notifyDataSetChanged();

        model = ViewModelProviders.of(this).get(LAThirdViewModel.class);
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
        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream ObjOut = new ObjectOutputStream(baos);
            ObjOut.writeObject(adapter.getData());
            ObjOut.flush();
            outState.putByteArray(DATA, baos.toByteArray());
        } catch (IOException e) {
            Toast.makeText(getContext(), "Unable to serialize", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean isComplete() {
        int count = 0;
        List<ChosenProgram> chosenPrograms = new ArrayList<>();
        for (Discipline d : data) {
            if (d.getStatus()) {
                count++;
                chosenPrograms.add(new ChosenProgram(d.getFullName(), 0));
            }
        }

        if (count > 3) {
            Toast.makeText(getActivity(), R.string.alert_discipline_more, Toast.LENGTH_SHORT).show();
            return false;
        } else if (count == 0) {
            Toast.makeText(getActivity(), R.string.alert_discipline_zero, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            model.replaceAllPrograms(chosenPrograms);
            return true;
        }
    }

    @Override
    public int getChosenDisciplines() {
        return chosenDisciplines;
    }

    @Override
    public void incrementChosen() {
        ++chosenDisciplines;
    }

    @Override
    public void decrementChosen() {
        --chosenDisciplines;
    }
}
