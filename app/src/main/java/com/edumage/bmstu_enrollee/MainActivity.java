package com.edumage.bmstu_enrollee;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupFragments();
    }

    private void setupFragments() {
        NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
        bottomNavigation = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNavigation, navController);
    }

    public void hideBottomNav() {
        bottomNavigation.setVisibility(View.GONE);
    }

    public void showBottomNav() {
        bottomNavigation.setVisibility(View.VISIBLE);
    }
}
