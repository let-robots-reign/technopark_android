package com.edumage.bmstu_enrollee.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edumage.bmstu_enrollee.Adapters.NewsAdapter;
import com.edumage.bmstu_enrollee.NewsItem;
import com.edumage.bmstu_enrollee.R;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class NewsFragment extends Fragment {

    private NewsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<NewsItem> cardsNews = new ArrayList<>();
        cardsNews.add(new NewsItem(R.drawable.exampleimgnews_1, "12 апреля в 12:00 пройдет прямая трансляция на сайте abiturient.bmstu.ru"));
        cardsNews.add(new NewsItem(R.drawable.exampleimgnews_2,"В связи с угрозой коронавирусной инфекции МГТУ им. Н.Э. Баумана перешел на дистанционное обучение."));
        cardsNews.add(new NewsItem(R.drawable.exampleimgnews_1,"Hello 3"));

        adapter = new NewsAdapter(cardsNews);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.news, container, false);

        RecyclerView RVnews = rootView.findViewById(R.id.RVnews);
        RVnews.setLayoutManager(new GridLayoutManager(getContext(), 2, VERTICAL, false));

        RVnews.setAdapter(adapter);
        RVnews.setHasFixedSize(true);

        return rootView;
    }
}
