package com.edumage.bmstu_enrollee.Fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edumage.bmstu_enrollee.Adapters.CatalogCardsAdapter;
import com.edumage.bmstu_enrollee.CatalogCard;
import com.edumage.bmstu_enrollee.FeedType;
import com.edumage.bmstu_enrollee.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class CatalogFragment extends Fragment implements CatalogCardsAdapter.OnCardListener {

    private CatalogCardsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<CatalogCard> cards = new ArrayList<>();
        cards.add(new CatalogCard(getString(R.string.about_university), R.drawable.bmstu, CatalogCard.ABOUT_CARD_ID));
        cards.add(new CatalogCard(getString(R.string.news), R.drawable.newspaper, CatalogCard.NEWS_CARD_ID));
        cards.add(new CatalogCard(getString(R.string.faculties_and_departments), R.drawable.faculties, CatalogCard.FACULTIES_CARD_ID));
        cards.add(new CatalogCard(getString(R.string.buildings_and_hostels), R.drawable.ulk, CatalogCard.CAMPUS_CARD_ID));
        cards.add(new CatalogCard(getString(R.string.extracurricular_activities), R.drawable.studsovet, CatalogCard.EVENTS_CARD_ID));
        cards.add(new CatalogCard(getString(R.string.admission_process), R.drawable.application, CatalogCard.APPLY_CARD_ID));
        cards.add(new CatalogCard(getString(R.string.about_application), R.drawable.info, CatalogCard.INFO_CARD_ID));

        adapter = new CatalogCardsAdapter(cards, this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.catalog_screen, container, false);
        RecyclerView list = rootView.findViewById(R.id.catalog_list);
        int orientation = getResources().getConfiguration().orientation;
        int spanCount;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCount = 2;
        } else {
            spanCount = 3;
        }
        list.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));
        list.setAdapter(adapter);
        list.setHasFixedSize(true);
        return rootView;
    }

    @Override
    public void onCardClick(int catalogId) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        if (catalogId == CatalogCard.ABOUT_CARD_ID) {
            navController.navigate(R.id.action_catalog_tab_to_universityFragment);
        } else if (catalogId == CatalogCard.NEWS_CARD_ID || catalogId == CatalogCard.EVENTS_CARD_ID) {
            Bundle args = new Bundle();
            if (catalogId == CatalogCard.NEWS_CARD_ID)
                args.putSerializable("type", FeedType.NEWS);
            else
                args.putSerializable("type", FeedType.EVENTS);
            navController.navigate(R.id.action_catalogFragment_to_newsFragment, args);
        } else if (catalogId == CatalogCard.FACULTIES_CARD_ID) {
            navController.navigate(R.id.action_catalogFragment_to_facultetFragment);
        } else if (catalogId == CatalogCard.CAMPUS_CARD_ID) {
            navController.navigate(R.id.action_catalog_tab_to_buildingActivity);
        } else if (catalogId == CatalogCard.APPLY_CARD_ID) {
            navController.navigate(R.id.action_catalog_Fragment_to_applyFragment);
        } else if (catalogId == CatalogCard.INFO_CARD_ID) {
            navController.navigate(R.id.action_catalog_tab_to_informationFragment);
        }
    }
}
