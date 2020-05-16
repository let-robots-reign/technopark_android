package com.edumage.bmstu_enrollee.Fragments;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.edumage.bmstu_enrollee.Adapters.EGEAdapter;
import com.edumage.bmstu_enrollee.EGESubject;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.EgeSubjectsViewModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EgeFragment extends Fragment implements View.OnClickListener {

    private EGEAdapter adapter;
    private EgeSubjectsViewModel model;
    private HomeFragment homeFragment;

    static final String TAG = "DialogEgeFragment";



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getParentFragment() instanceof HomeFragment) {
            homeFragment = (HomeFragment) getParentFragment();
        }

        //LoadData();
        adapter = new EGEAdapter();
        model = ViewModelProviders.of(this).get(EgeSubjectsViewModel.class);
        if (savedInstanceState == null) {
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
        Button confirmBtn= v.findViewById(R.id.button);
        confirmBtn.setOnClickListener(this);

        Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setTitle(R.string.ege_results);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        return v;
    }


    @Override
    public void onClick(View v) {
            model.replaceAllPoints(adapter.getPassed());
        }

}
