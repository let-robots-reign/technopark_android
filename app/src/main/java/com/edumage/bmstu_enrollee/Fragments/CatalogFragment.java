package com.edumage.bmstu_enrollee.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.edumage.bmstu_enrollee.Adapters.CatalogCardsAdapter;
import com.edumage.bmstu_enrollee.CatalogCard;
import com.edumage.bmstu_enrollee.MainActivity;
import com.edumage.bmstu_enrollee.R;

import java.util.ArrayList;
import java.util.List;

public class CatalogFragment extends Fragment implements CatalogCardsAdapter.OnCardListener {
    private CatalogCardsAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<CatalogCard> cards = new ArrayList<>();
        cards.add(new CatalogCard("Об университете", R.drawable.bmstu));
        cards.add(new CatalogCard("Новости", R.drawable.newspaper));
        cards.add(new CatalogCard("Факультеты и кафедры", R.drawable.faculties));
        cards.add(new CatalogCard("Корпуса и общежития", R.drawable.ulk));
        cards.add(new CatalogCard("Внеучебная деятельность", R.drawable.studsovet));
        cards.add(new CatalogCard("Подача документов", R.drawable.application));
        cards.add(new CatalogCard("О приложении", R.drawable.info));
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
    public void onCardClick(int position) {
        if (position == 1) {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.action_catalogFragment_to_newsFragment);
        }
    }
}
