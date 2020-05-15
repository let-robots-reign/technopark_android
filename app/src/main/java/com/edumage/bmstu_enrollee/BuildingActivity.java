package com.edumage.bmstu_enrollee;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.edumage.bmstu_enrollee.Adapters.BuildingPagerAdapter;

public class BuildingActivity extends AppCompatActivity {

    ViewPager viewPager;
    BuildingPagerAdapter sectionsPagerAdapter;
    TabLayout tabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        sectionsPagerAdapter = new BuildingPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}