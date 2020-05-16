package com.edumage.bmstu_enrollee.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.lifecycle.ViewModelProviders;
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

        /*if (savedInstanceState == null) {
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
                data = Discipline.LoadDisciplines(requireContext());
            }
        }*/

        adapter = new DisciplineAdapter( this);
        // get all programm
        model = ViewModelProviders.of(this).get(DisciplinesViewModel.class);

        if (savedInstanceState == null){
            model.loadData();
            model.applyChosenSubjects();
        }
        model.data.observe(this, new Observer<ArrayList<Discipline>>() {
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

  /*  @Override
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
    }*/

    @Override
    public boolean isComplete() {

      /*  List<ChosenProgram> chosenPrograms = new ArrayList<>();
        for (Discipline d : data) {
            if (d.getStatus()) {
                count++;
                chosenPrograms.add(new ChosenProgram(d.getFullName(), 0));
            }
        }*/

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
