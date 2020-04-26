package com.edumage.bmstu_enrollee.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edumage.bmstu_enrollee.Adapters.NewsAdapter;
import com.edumage.bmstu_enrollee.NewsItem;
import com.edumage.bmstu_enrollee.R;

import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

import androidx.lifecycle.ViewModelProviders;

import com.edumage.bmstu_enrollee.ViewModels.NewsViewModel;

public class NewsFragment extends Fragment implements NewsAdapter.OnNewsListener {

    private NewsAdapter adapter;
    private NewsViewModel model;
    private RecyclerView RVnews;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = ViewModelProviders.of(this).get(NewsViewModel.class);
        model.parseNewsList();
        adapter = new NewsAdapter(model.getNewsList().getValue(), this);
        model.getNewsList().observe(this, new Observer<List<NewsItem>>() {
            @Override
            public void onChanged(List<NewsItem> newsItems) {
                adapter = new NewsAdapter(model.getNewsList().getValue(), NewsFragment.this);
                RVnews.setAdapter(adapter);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.news, container, false);

        RVnews = rootView.findViewById(R.id.RVnews);
        RVnews.setLayoutManager(new GridLayoutManager(getContext(), 2, VERTICAL, false));

        RVnews.setAdapter(adapter);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setTitle("Новости");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        return rootView;
    }

    @Override
    public void onNewsClick(String title, String imageURL, String linkURL) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("imageURL", imageURL);
        args.putString("linkURL", linkURL);
        navController.navigate(R.id.action_newsFragment_to_newsItemFragment, args);
    }
}
