package com.edumage.bmstu_enrollee;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private NavController navController;
    private boolean backPressedOnce = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupFragments();
    }

    private void setupFragments() {
        navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
        bottomNavigation = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNavigation, navController);
    }
  
    public void hideBottomNav() {
        bottomNavigation.setVisibility(View.GONE);
    }

    public void showBottomNav() {
        bottomNavigation.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        NavDestination destination = navController.getCurrentDestination();
        if (destination != null &&
                destination.getId() == navController.getGraph().getStartDestination()) {
            if (backPressedOnce) {
                super.onBackPressed();
                return;
            }
            backPressedOnce = true;
            Toast.makeText(this, getResources().getString(R.string.press_back_again),
                    Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
        }
    }
}
