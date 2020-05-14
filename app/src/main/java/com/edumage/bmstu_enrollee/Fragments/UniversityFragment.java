package com.edumage.bmstu_enrollee.Fragments;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.UniversityViewModel;


public class UniversityFragment extends Fragment implements View.OnClickListener {

    private UniversityViewModel mViewModel;

    public static UniversityFragment newInstance() {
        return new UniversityFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.university_fragment, container, false);
        Button universityBtn= v.findViewById(R.id.university_site_btn);
        ImageView vkBtn=v.findViewById(R.id.vk_social);
        ImageView instBtn=v.findViewById(R.id.inst_social);
        universityBtn.setOnClickListener(this);
        vkBtn.setOnClickListener(this);
        instBtn.setOnClickListener(this);

        TextView descrTextView = v.findViewById(R.id.university_description_textView);
        descrTextView.setMovementMethod(new ScrollingMovementMethod());

        Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setTitle(R.string.about_university);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        return v;

    }


    @Override
    public void onClick(View v) {
        String url="";


        int id =v.getId();
        switch(id){
            case R.id.university_site_btn:
                url="bmstu.ru";
                break;
            case R.id.vk_social:
                url="vk.com/ab_bmstu1830";
                break;
            case R.id.inst_social:
                url="www.instagram.com/bmstu1830/";
                break;
        }

        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);



        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getContext(),R.string.unable_to_show_site,Toast.LENGTH_LONG).show();
        }


    }
}
