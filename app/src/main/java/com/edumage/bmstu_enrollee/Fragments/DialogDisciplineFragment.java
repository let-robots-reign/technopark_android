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
import com.edumage.bmstu_enrollee.Adapters.EGEAdapter;
import com.edumage.bmstu_enrollee.Discipline;
import com.edumage.bmstu_enrollee.EGESubject;
import com.edumage.bmstu_enrollee.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DialogDisciplineFragment extends DialogFragment implements View.OnClickListener, DisciplineAdapter.DisciplineCardClick {

    private RecyclerView recyclerView;
    private Button button;
    private DisciplineAdapter adapter;
    private ArrayList<Discipline> data;
    private int chosenDisciplines = 0;

    static final String TAG = "DialogDisciplineFragment";

    // TODO: need another solution
    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadData();
        adapter = new DisciplineAdapter(data, this);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.discipline_dialog_fragment, container, false);
        recyclerView = v.findViewById(R.id.discipline_dialog_recyclerview);
        recyclerView.setAdapter(adapter);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
        }

        button = v.findViewById(R.id.discipline_dialog_button);
        button.setOnClickListener(this);
        return v;
    }

    // TODO: переписать этот метод
    private void LoadData() {
        data = Discipline.LoadDisciplines(getContext());
    }

    @Override
    public void onClick(View v) {
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
