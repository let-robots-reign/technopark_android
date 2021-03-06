package com.edumage.bmstu_enrollee.Fragments;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edumage.bmstu_enrollee.Adapters.NewsAdapter;
import com.edumage.bmstu_enrollee.FeedType;
import com.edumage.bmstu_enrollee.R;

import java.util.Arrays;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;
import com.edumage.bmstu_enrollee.ViewModels.NewsViewModel;

public class NewsFragment extends Fragment implements NewsAdapter.OnNewsListener {
    private FeedType type = FeedType.NEWS;
    private NewsAdapter adapter;
    private RecyclerView RVnews;
    private ProgressBar progressBar;
    private ImageView noConnection;
    private boolean connected;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            type = (FeedType) getArguments().getSerializable("type");
        }

        final NewsViewModel model = new ViewModelProvider(this).get(NewsViewModel.class);
        model.parseNewsList(type);

        final List<Integer> colors = Arrays.asList(
                R.color.news1,
                R.color.news2,
                R.color.news3,
                R.color.news4,
                R.color.news5,
                R.color.news6);

        adapter = new NewsAdapter(model.getNewsList().getValue(), this, colors, getActivity());

        model.getHasConnection().observe(this, aBoolean -> {
            if (aBoolean) {
                connected = true;
                progressBar.setVisibility(View.VISIBLE);
                noConnection.setVisibility(View.GONE);
            } else {
                connected = false;
                progressBar.setVisibility(View.GONE);
                noConnection.setVisibility(View.VISIBLE);
            }
        });

        model.getNewsList().observe(this, newsItems -> {
            if (newsItems.size() == 0 && connected) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
            adapter = new NewsAdapter(model.getNewsList().getValue(), NewsFragment.this, colors, getActivity());
            RVnews.setAdapter(adapter);
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.news, container, false);

        RVnews = rootView.findViewById(R.id.RVnews);
        progressBar = rootView.findViewById(R.id.progress);
        noConnection = rootView.findViewById(R.id.no_connection);
        int orientation = getResources().getConfiguration().orientation;
        int columns;  // number of columns depends on the orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            columns = 2;
        } else {
            columns = 3;
        }
        RVnews.setLayoutManager(new GridLayoutManager(getContext(), columns, VERTICAL, false));

        RVnews.setAdapter(adapter);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        if (type == FeedType.EVENTS) {
            toolbar.setTitle(R.string.events_tab);
        } else {
            toolbar.setTitle(R.string.news_tab);
        }
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
        return rootView;
    }

    @Override
    public void onNewsClick(String title, String imageURL, String linkURL, Integer colorId) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("imageURL", imageURL);
        args.putString("linkURL", linkURL);
        args.putInt("colorId", colorId);
        navController.navigate(R.id.action_newsFragment_to_newsItemFragment, args);
    }
}
