package com.edumage.bmstu_enrollee.Fragments;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.edumage.bmstu_enrollee.Adapters.BuildingAdapter;
import com.edumage.bmstu_enrollee.BuildingItem;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.BuildingViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class BuildingFragment extends Fragment implements BuildingAdapter.BuildingMap {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int CAMPUS_NUMBER = 1;
    private static final int HOSTEL_NUMBER = 2;

    private static final String PREFERENCES = "PREFERENCES";
    private static final String ALERT_KEY = "ALERT";

   // private static

    private BuildingViewModel.BuildingType type;

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
         int section_number = 0;
        Bundle bundle = getArguments();
        if (bundle != null) {
            section_number = bundle.getInt(ARG_SECTION_NUMBER);
        }

        final BuildingViewModel model = new ViewModelProvider(this).get(BuildingViewModel.class);

        model.state.observe(this, new Observer<BuildingViewModel.ParsingState>() {
            @Override
            public void onChanged(BuildingViewModel.ParsingState parsingState) {
                switch (parsingState) {
                    case FAILURE:
                        Toast.makeText(getContext(), R.string.unable_to_load_data, Toast.LENGTH_LONG).show();
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

        model.list.observe(this, new Observer<List<BuildingItem>>() {
            @Override
            public void onChanged(List<BuildingItem> buildingItems) {
                if (buildingItems != null) {
                    adapter.setData(buildingItems);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        type = BuildingViewModel.BuildingType.HOSTEL;
        switch (section_number) {
            case CAMPUS_NUMBER:
                type = BuildingViewModel.BuildingType.CAMPUS;
                break;
            case HOSTEL_NUMBER:
                type = BuildingViewModel.BuildingType.HOSTEL;
                break;
        }
        model.loadData(type);
        adapter = new BuildingAdapter(this);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.building_fragment, container, false);
        recyclerView = v.findViewById(R.id.placeholder_recyclerview);
        RecyclerView.LayoutManager layoutManager;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        } else {
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        switch (type) {
            case CAMPUS:
                break;
            case HOSTEL:
                initHostelAlert(v);
                break;
        }

        return v;
    }

    private void initHostelAlert(View container){
        if (getContext()==null)return;

        final SharedPreferences preferences = getContext().getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        boolean show =preferences.getBoolean(ALERT_KEY,true);
        if(!show) return;

        final CardView card  = container.findViewById(R.id.info_hostel_card);
        final CheckBox checkBox =container.findViewById(R.id.do_not_show);
        final Button buttonOk =container.findViewById(R.id.button_ok);

        card.setVisibility(View.VISIBLE);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    preferences.edit().putBoolean(ALERT_KEY,false).apply();
                }

                Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        checkBox.setClickable(false);
                        buttonOk.setClickable(false);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        card.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                };

                card.animate()
                        .translationYBy(card.getWidth()*(-1))
                        .setDuration(500)
                        .setInterpolator(new DecelerateInterpolator())
                        .setListener(animatorListener)
                        .start();


            }
        });
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
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), R.string.unable_to_show_map, Toast.LENGTH_SHORT).show();
        }
    }
}