package com.edumage.bmstu_enrollee.Fragments;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.edumage.bmstu_enrollee.Adapters.DisciplineAdapter;
import com.edumage.bmstu_enrollee.Discipline;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.DisciplinesViewModel;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DialogDisciplineFragment extends DialogFragment implements View.OnClickListener, DisciplineAdapter.DisciplineCardClick {

    private DisciplineAdapter adapter;
    static final String TAG = "DialogDisciplineFragment";
    private DisciplinesViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new DisciplineAdapter(this);

        model = new ViewModelProvider(this).get(DisciplinesViewModel.class);
        if (savedInstanceState == null) {
            model.loadData();
            model.applyChosenProgram();
        }

        model.data.observe(this, new Observer<ArrayList<Discipline>>() {
            @Override
            public void onChanged(ArrayList<Discipline> disciplines) {
                adapter.setData(disciplines);
                adapter.notifyDataSetChanged();
            }
        });
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

        TextView textViewOk = v.findViewById(R.id.discipline_dialog_ok);
        TextView textViewCancel = v.findViewById(R.id.discipline_dialog_cancel);
        textViewOk.setOnClickListener(this);
        textViewCancel.setOnClickListener(this);
        return v;
    }

   /* // TODO: переписать этот метод
    private void LoadData() {
        data = Discipline.LoadDisciplines(requireContext());
    }*/

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.discipline_dialog_ok) {
            if (getChosenDisciplines() > 3) {
                Toast.makeText(getContext(), R.string.disciplines_alert, Toast.LENGTH_SHORT).show();
                return;
            } else {
                model.replaceAllPrograms(adapter.getData());
            }
        }

        dismiss();
    }

    @Override
    public int getChosenDisciplines() {
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
