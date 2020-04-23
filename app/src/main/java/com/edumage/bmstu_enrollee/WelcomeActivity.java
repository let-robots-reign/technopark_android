package com.edumage.bmstu_enrollee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.edumage.bmstu_enrollee.Fragments.LAFragmentFirst;
import com.edumage.bmstu_enrollee.Fragments.LAFragmentSecond;
import com.edumage.bmstu_enrollee.Fragments.LAFragmentThird;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {

    LAFragmentFirst firstFragment;
    LAFragmentSecond secondFragment;
    LAFragmentThird thirdFragment;
    Button nextButton;
    Button prevButton;
    ImageView imageBackground;
    ArrayList<CompletableFragment> fragments;
    private int state = 1;

    private static final String STATE_KEY = "STATE";
    private static final int MAX_STATE = 3;

    // 1 - LAFragment_First
    // 2 - LAFragment_Second
    // 3 - LAFragment_Third

    public interface CompletableFragment {
        boolean isComplete();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);

        imageBackground = findViewById(R.id.image_background);
        Glide.with(this).asGif().load(R.drawable.background_test).into(imageBackground);

        nextButton = findViewById(R.id.button_next);
        prevButton = findViewById(R.id.button_prev);
        nextButton.setOnClickListener(buttonListener);
        prevButton.setOnClickListener(buttonListener);

        if (savedInstanceState == null) {
            firstFragment = new LAFragmentFirst();
            secondFragment = new LAFragmentSecond();
            thirdFragment = new LAFragmentThird();
            state = 1;
            setState(state);
        } else {
            firstFragment = (LAFragmentFirst) getSupportFragmentManager().findFragmentByTag(LAFragmentFirst.TAG);
            secondFragment = (LAFragmentSecond) getSupportFragmentManager().findFragmentByTag(LAFragmentSecond.TAG);
            thirdFragment = (LAFragmentThird) getSupportFragmentManager().findFragmentByTag(LAFragmentThird.TAG);
            if (firstFragment == null) firstFragment = new LAFragmentFirst();
            if (secondFragment == null) secondFragment = new LAFragmentSecond();
            if (thirdFragment == null) thirdFragment = new LAFragmentThird();
            state = savedInstanceState.getInt(STATE_KEY);
            setState(state);

        }

        fragments = new ArrayList<>();
        fragments.add(firstFragment);
        fragments.add(secondFragment);
        fragments.add(thirdFragment);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_KEY, state);
    }

    @Override
    public void onBackPressed() {
        if (state > 1) {
            state--;
        }
        super.onBackPressed();
    }

    private void clearBackStack(int state) {
        FragmentManager fm = getSupportFragmentManager();
        switch (state) {
            case 1:
                fm.popBackStack(LAFragmentSecond.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case 2:
                fm.popBackStack(LAFragmentThird.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }
    }

    private void setState(int state) {
        clearBackStack(state);
        if (state == 1) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.la_frame, firstFragment, LAFragmentFirst.TAG);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            prevButton.setVisibility(View.GONE);
        }
        if (state == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.la_frame, secondFragment, LAFragmentSecond.TAG).
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                    addToBackStack(LAFragmentSecond.TAG).commit();
            prevButton.setVisibility(View.VISIBLE);
        }
        if (state == 3) {
            getSupportFragmentManager().beginTransaction().replace(R.id.la_frame, thirdFragment, LAFragmentThird.TAG).
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                    addToBackStack(LAFragmentThird.TAG).commit();
            prevButton.setVisibility(View.VISIBLE);
        }

        if (state > MAX_STATE) {
            goToMainActivity();
        }
    }

    private void goToMainActivity() {
        // передача данных
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.button_next:
                    if (fragments.get(state - 1).isComplete()) {
                        state++;
                        setState(state);
                    }
                    break;
                case R.id.button_prev:
                    state--;
                    setState(state);
                    break;
            }
        }
    };
}
