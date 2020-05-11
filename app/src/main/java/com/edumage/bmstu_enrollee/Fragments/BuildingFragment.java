package com.edumage.bmstu_enrollee.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.edumage.bmstu_enrollee.Adapters.BuildingAdapter;
import com.edumage.bmstu_enrollee.BuildingItem;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.BuildingViewModel;
import com.edumage.bmstu_enrollee.XmlDataStorage;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class BuildingFragment extends Fragment implements BuildingAdapter.BuildingMap {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int CAMPUS_NUMBER=1;
    private static final int HOSTEL_NUMBER=2;

    private RecyclerView recyclerView;
    private BuildingAdapter adapter;


    public static BuildingFragment newInstance(int index) {
        BuildingFragment fragment = new BuildingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int section_number=0;
        Bundle bundle = getArguments();
        if (bundle!=null) {
            section_number = bundle.getInt(ARG_SECTION_NUMBER);
        }

        final  BuildingViewModel model = ViewModelProviders.of(this).get(BuildingViewModel.class);

        model.state.observe(this, new Observer<BuildingViewModel.ParsingState>() {
            @Override
            public void onChanged(BuildingViewModel.ParsingState parsingState) {
                switch (parsingState){
                    case FAILURE:
                        Toast.makeText(getContext(),R.string.unable_to_load_data,Toast.LENGTH_LONG).show();
                        recyclerView.setVisibility(View.INVISIBLE);
                        break;
                    case SUCCESS:
                        recyclerView.setVisibility(View.VISIBLE);
                        break;

                    case IN_PROGRESS:
                        recyclerView.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });

        model.list.observe(this, new Observer<ArrayList<BuildingItem>>() {
            @Override
            public void onChanged(ArrayList<BuildingItem> buildingItems) {
                if (buildingItems!=null){
                    adapter.setData(buildingItems);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        BuildingViewModel.BuildingType type= BuildingViewModel.BuildingType.HOSTEL;
        switch (section_number){
            case CAMPUS_NUMBER:
                type= BuildingViewModel.BuildingType.CAMPUS;
                break;
            case HOSTEL_NUMBER:
                type= BuildingViewModel.BuildingType.HOSTEL;
                break;
        }
        model.loadData(type);
        adapter = new BuildingAdapter(this);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.building_fragment,container, false);
        recyclerView = v.findViewById(R.id.placeholder_recyclerview);
        RecyclerView.LayoutManager layoutManager=null;

        if(getContext().getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
            layoutManager= new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        } else {
            layoutManager =  new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return v;
    }

   /* private void InitData(int state) {
        switch(state){
            case CAMPUS_NUMBER:
                try {
                    data = XmlDataStorage.getInstance().parseBuilding(getContext(), R.xml.bmstu_campus);
                } catch (Exception e){
                    Toast.makeText(getContext(),R.string.unable_to_load_data,Toast.LENGTH_SHORT).show();
                }
            break;
            case HOSTEL_NUMBER:
                int size= (int)Math.round(Math.random()*7)+1;
                data= new ArrayList<>();
                for (int i=0; i<size; i++){
                    data.add(BuildingItem.generate());
                }
//TODO set data for campuses and hostel
                break;

        }

    }*/

    @Override
    public void showMap(BuildingItem building) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(building.getGeoLocation());
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getContext(),R.string.unable_to_show_map,Toast.LENGTH_SHORT).show();
        }

    }
}