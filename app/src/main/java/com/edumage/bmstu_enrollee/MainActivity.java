package com.edumage.bmstu_enrollee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.edumage.bmstu_enrollee.Fragments.CatalogFragment;
import com.edumage.bmstu_enrollee.Fragments.HomeFragment;
import com.edumage.bmstu_enrollee.Fragments.StatsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements CatalogFragment.FragmentCreation {
    private final String HOME_FRAGMENT = "Home";
    private final String CATALOG_FRAGMENT = "Catalog";
    private final String STATS_FRAGMENT = "Stats";
    private FragmentManager fragmentManager;
    private Fragment selectedFragment = new HomeFragment();
    private String selectedFragmentTag = HOME_FRAGMENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            selectedFragmentTag = savedInstanceState.getString("selectedFragmentTag");
            selectedFragment = fragmentManager.findFragmentByTag(selectedFragmentTag);
        }

        initViews();
    }

    private void initViews() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        // manually displaying the first fragment - one time only
        changeFragment(selectedFragment, selectedFragmentTag);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home_tab:
                            selectedFragment = new HomeFragment();
                            selectedFragmentTag = HOME_FRAGMENT;
                            break;
                        case R.id.catalog_tab:
                            Fragment fragment = fragmentManager.findFragmentByTag(CATALOG_FRAGMENT);
                            if (fragment != null && ((CatalogFragment)fragment).getSelectedCatalogFragment() != null) {
                                selectedFragment = ((CatalogFragment)fragment).getSelectedCatalogFragment();
                                selectedFragmentTag = ((CatalogFragment)fragment).getSelectedCatalogFragmentTag();
                            } else {
                                selectedFragment = new CatalogFragment();
                                selectedFragmentTag = CATALOG_FRAGMENT;
                            }
                            break;
                        case R.id.stats_tab:
                            selectedFragment = new StatsFragment();
                            selectedFragmentTag = STATS_FRAGMENT;
                            break;
                    }
                    changeFragment(selectedFragment, selectedFragmentTag);
                    return true;
                }
            };

    private void changeFragment(Fragment newFragment, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragmentTemp = fragmentManager.findFragmentByTag(tag);
        if (fragmentTemp == null) {
            fragmentTemp = newFragment;
            fragmentTransaction.add(R.id.container, fragmentTemp, tag);
        } else {
            fragmentTransaction.show(fragmentTemp);
        }

        if (!(newFragment instanceof HomeFragment) && !(newFragment instanceof CatalogFragment)
                && !(newFragment instanceof StatsFragment)) {
            fragmentTransaction.addToBackStack(null);
            selectedFragment = fragmentTemp;
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("selectedFragmentTag", selectedFragmentTag);
    }

    @Override
    public void createCatalogFragment(Fragment fragment, String tag) {
        changeFragment(fragment, tag);
    }
}
