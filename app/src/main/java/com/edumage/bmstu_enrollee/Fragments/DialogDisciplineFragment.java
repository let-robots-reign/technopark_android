package com.edumage.bmstu_enrollee.Fragments;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.edumage.bmstu_enrollee.Adapters.DisciplineAdapter;
import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.Discipline;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.LAThirdViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DialogDisciplineFragment extends DialogFragment implements View.OnClickListener, DisciplineAdapter.DisciplineCardClick {

    private DisciplineAdapter adapter;
    private ArrayList<Discipline> data;
    private int chosenDisciplines = 0;

    static final String TAG = "DialogDisciplineFragment";

    private LAThirdViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadData();
        adapter = new DisciplineAdapter(data, this);
        model = ViewModelProviders.of(this).get(LAThirdViewModel.class);
    }

    // TODO: need another solution
    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        Objects.requireNonNull(getDialog().getWindow()).setAttributes(params);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_home_tab_self);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.discipline_dialog_fragment, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.discipline_dialog_recyclerview);
        recyclerView.setAdapter(adapter);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
        }

        Button button = v.findViewById(R.id.discipline_dialog_button);
        button.setOnClickListener(this);
        return v;
    }

    // TODO: переписать этот метод
    private void LoadData() {
        data = Discipline.LoadDisciplines(requireContext());
    }

    @Override
    public void onClick(View v) {
        List<ChosenProgram> chosenPrograms = new ArrayList<>();
        for (Discipline d : data) {
            if (d.getStatus()) {
                chosenPrograms.add(new ChosenProgram(d.getFullName(), 0));
            }
        }
        model.replaceAllPrograms(chosenPrograms);
        dismiss();
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
