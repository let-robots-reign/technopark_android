package com.edumage.bmstu_enrollee.Fragments;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.edumage.bmstu_enrollee.Adapters.EGEAdapter;
import com.edumage.bmstu_enrollee.EGESubject;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.EgeSubjectsViewModel;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DialogEgeFragment extends DialogFragment implements View.OnClickListener {

    private EGEAdapter adapter;
    private ArrayList<EGESubject> data;
    private EgeSubjectsViewModel model;
    private HomeFragment homeFragment;

    static final String TAG = "DialogEgeFragment";

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        Objects.requireNonNull(getDialog().getWindow()).setAttributes(params);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getParentFragment() instanceof HomeFragment) {
            homeFragment = (HomeFragment)getParentFragment();
        }

        //LoadData();
        adapter = new EGEAdapter();
        model = ViewModelProviders.of(this).get(EgeSubjectsViewModel.class);
        if (savedInstanceState==null){
            model.loadData();
            model.applyEgeScore();
        }
        model.data.observe(this, new Observer<ArrayList<EGESubject>>() {
            @Override
            public void onChanged(ArrayList<EGESubject> egeSubjects) {
                adapter.setData(egeSubjects);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        homeFragment.notifyEGEChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ege_dialog_fragment, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.ege_dialog_recyclerview);
        recyclerView.setAdapter(adapter);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
        }
        TextView textViewOk = v.findViewById(R.id.ege_dialog_ok);
        TextView textViewCancel = v.findViewById(R.id.ege_dialog_cancel);

        textViewOk.setOnClickListener(this);
        textViewCancel.setOnClickListener(this);
        return v;
    }

   /* // TODO: переписать этот метод
    private void LoadData() {
        data = EGESubject.LoadEgeSubjects(requireContext());
    }*/

    @Override
    public void onClick(View v) {
       /* List<ExamPoints> points = new ArrayList<>();
        for (EGESubject subject : data) {
            if (subject.isPassed()) {
                points.add(new ExamPoints(subject.getName(), subject.getScore()));
            }
        }
*/
       int id=v.getId();

       if (id==R.id.ege_dialog_ok) {
               model.replaceAllPoints(adapter.getPassed());

       }

        dismiss();
    }
}
