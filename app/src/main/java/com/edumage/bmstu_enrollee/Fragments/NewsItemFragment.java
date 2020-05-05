package com.edumage.bmstu_enrollee.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.NewsViewModel;
import com.squareup.picasso.Picasso;

public class NewsItemFragment extends Fragment {
    private String title = null, imageURL = null, linkURL = null;
    private TextView contentView;
    private ProgressBar progressBar;
    private ImageView noConnection;

    private boolean connected;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            imageURL = getArguments().getString("imageURL");
            linkURL = getArguments().getString("linkURL");
        }

        NewsViewModel model = ViewModelProviders.of(this).get(NewsViewModel.class);
        model.getHasConnection().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    connected = true;
                    progressBar.setVisibility(View.VISIBLE);
                    noConnection.setVisibility(View.GONE);
                } else {
                    connected = false;
                    progressBar.setVisibility(View.GONE);
                    noConnection.setVisibility(View.VISIBLE);
                }
            }
        });
        model.parseNewsContent(linkURL);
        model.getNewsContent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if ((s == null || s.length() == 0) && connected) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
                if (connected) contentView.setText(s);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.news_page, container, false);

        progressBar = rootView.findViewById(R.id.progress);
        noConnection = rootView.findViewById(R.id.no_connection);
        TextView titleView = rootView.findViewById(R.id.news_title);
        titleView.setText(title);
        ImageView image = rootView.findViewById(R.id.news_page_img);
        if (imageURL != null) {
            Picasso.with(getActivity()).load(imageURL).into(image);
        } else {
            image.setImageResource(R.drawable.no_image);
        }
        contentView = rootView.findViewById(R.id.news_text);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        return rootView;
    }
}
