package com.edumage.bmstu_enrollee.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edumage.bmstu_enrollee.Adapters.CatalogCardsAdapter;
import com.edumage.bmstu_enrollee.BuildingActivity;
import com.edumage.bmstu_enrollee.CatalogCard;
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
        cards.add(new CatalogCard("Об университете", R.drawable.bmstu, CatalogCard.ABOUT_CARD_ID));
        cards.add(new CatalogCard("Новости", R.drawable.newspaper, CatalogCard.NEWS_CARD_ID));
        cards.add(new CatalogCard("Факультеты и кафедры", R.drawable.faculties, CatalogCard.FACULTIES_CARD_ID));
        cards.add(new CatalogCard("Корпуса и общежития", R.drawable.ulk, CatalogCard.CAMPUS_CARD_ID));
        cards.add(new CatalogCard("Внеучебная деятельность", R.drawable.studsovet, CatalogCard.EVENTS_CARD_ID));
        cards.add(new CatalogCard("Процесс поступления", R.drawable.application, CatalogCard.APPLY_CARD_ID));
        cards.add(new CatalogCard("О приложении", R.drawable.info, CatalogCard.INFO_CARD_ID));

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
        if (catalogId == CatalogCard.NEWS_CARD_ID) {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.action_catalogFragment_to_newsFragment);
        } else if (catalogId == CatalogCard.APPLY_CARD_ID) {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.action_catalog_Fragment_to_applyFragment);
        }

        //TODO change way of start new activity, using MVP
        if (catalogId == CatalogCard.CAMPUS_CARD_ID) {
            Intent intent = new Intent(getContext(), BuildingActivity.class);
            startActivity(intent);
        }
    }
}
