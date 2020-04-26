package com.edumage.bmstu_enrollee.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.edumage.bmstu_enrollee.R;
import com.squareup.picasso.Picasso;

public class NewsItemFragment extends Fragment {
    private String title = null, imageURL = null, linkURL = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            imageURL = getArguments().getString("imageURL");
            linkURL = getArguments().getString("linkURL");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.news_page, container, false);
        TextView titleView = rootView.findViewById(R.id.news_title);
        titleView.setText(title);
        ImageView image = rootView.findViewById(R.id.news_page_img);
        if (imageURL != null) {
            Picasso.get().load(imageURL).into(image);
        } else {
            image.setImageResource(R.drawable.no_image);
        }
        TextView contentView = rootView.findViewById(R.id.news_text);



        return rootView;
    }
}
