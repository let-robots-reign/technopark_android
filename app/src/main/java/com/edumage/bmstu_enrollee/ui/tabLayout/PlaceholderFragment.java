package com.edumage.bmstu_enrollee.ui.tabLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.edumage.bmstu_enrollee.Adapters.BuildingAdapter;
import com.edumage.bmstu_enrollee.BuildingItem;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.XmlDataStorage;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements BuildingAdapter.BuildingMap {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int CAMPUS_NUMBER=1;
    private static final int HOSTEL_NUMBER=2;

    RecyclerView recyclerView;
    ArrayList<BuildingItem> data;
    BuildingAdapter adapter;



    int section_number=0;


    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle!=null) {
            section_number = bundle.getInt(ARG_SECTION_NUMBER);
        }else {
            section_number=CAMPUS_NUMBER;
        }
        InitData(section_number);
        adapter = new BuildingAdapter(data,this);

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
            //layoutManager= new GridLayoutManager(getContext(),2,RecyclerView.VERTICAL,false);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return v;
    }

    private void InitData(int state) {
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

    }

    @Override
    public int showMap(BuildingItem building) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(building.getGeoLocation());
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
            return 1;
        } else {
            return -1;
        }

    }
}