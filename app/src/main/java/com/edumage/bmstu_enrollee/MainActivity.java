package com.edumage.bmstu_enrollee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.edumage.bmstu_enrollee.Fragments.CatalogFragment;
import com.edumage.bmstu_enrollee.Fragments.HomeFragment;
import com.edumage.bmstu_enrollee.Fragments.StatsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // test feature here (testing git)
        int a = 10;
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_nav);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);
        // keep selected fragment after rotation
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new HomeFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home_tab:
                            if (!(selectedFragment instanceof HomeFragment)) {
                                selectedFragment = new HomeFragment();
                            }
                            break;
                        case R.id.catalog_tab:
                            if (!(selectedFragment instanceof CatalogFragment)) {
                                selectedFragment = new CatalogFragment();
                            }
                            break;
                        case R.id.stats_tab:
                            if (!(selectedFragment instanceof StatsFragment)) {
                                selectedFragment = new StatsFragment();
                            }
                            break;
                    }
                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, selectedFragment).commit();
                    }
                    return true;
        }
    };
}
