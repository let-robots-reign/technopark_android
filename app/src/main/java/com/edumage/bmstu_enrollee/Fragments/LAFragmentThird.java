package com.edumage.bmstu_enrollee.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.edumage.bmstu_enrollee.Adapters.DisciplineAdapter;
import com.edumage.bmstu_enrollee.Discipline;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.WelcomeActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LAFragmentThird extends Fragment implements WelcomeActivity.CompletableFragment {

    private RecyclerView recyclerView;
    private DisciplineAdapter adapter;
    private ArrayList<Discipline> data;

    private static final String DATA="DISCIPLINES";
    public static final String TAG ="LAFragmentThird";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState==null){
           if (getContext()!=null && data==null) data =Discipline.LoadDisciplines(getContext());
        } else {
            try {
                ObjectInputStream stream =
                        new ObjectInputStream(new ByteArrayInputStream(savedInstanceState.getByteArray(DATA)));
                data =(ArrayList<Discipline>)stream.readObject();
                stream.close();
            } catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
                data= Discipline.LoadDisciplines(getContext());
            }
        }
        adapter = new DisciplineAdapter(data);
        adapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.la_fragment3,container,false);
        recyclerView = v.findViewById(R.id.discipline_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(adapter);

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
        int count=0;
        for (Discipline d : data){
            if(d.getStatus())count++;
        }
        if(count>=3){
            return true;
        } else {
            Toast.makeText(getContext(),R.string.alert_discipline,Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
