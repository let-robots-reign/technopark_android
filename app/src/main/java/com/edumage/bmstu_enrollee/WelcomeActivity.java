package com.edumage.bmstu_enrollee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
    ArrayList<CompletableFragment> fragments;
    private int state;

    private static final String STATE_KEY="STATE";
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

        nextButton = findViewById(R.id.button_next);
        prevButton = findViewById(R.id.button_prev);
        nextButton.setOnClickListener(buttonListener);
        prevButton.setOnClickListener(buttonListener);

        if (savedInstanceState == null) {
            firstFragment = new LAFragmentFirst();
            secondFragment = new LAFragmentSecond();
            thirdFragment =  new LAFragmentThird();
            state = 1;
            setState(state);
        } else {
            state=savedInstanceState.getInt(STATE_KEY);
            firstFragment = (LAFragmentFirst) getSupportFragmentManager().findFragmentByTag(LAFragmentFirst.TAG);
            secondFragment = (LAFragmentSecond) getSupportFragmentManager().findFragmentByTag(LAFragmentSecond.TAG);
            thirdFragment =(LAFragmentThird)getSupportFragmentManager().findFragmentByTag(LAFragmentThird.TAG);
            if (firstFragment==null)firstFragment= new LAFragmentFirst();
            if (secondFragment == null) secondFragment = new LAFragmentSecond();
            if (thirdFragment==null) thirdFragment=new LAFragmentThird();
        }

        fragments = new ArrayList<>();
        fragments.add(firstFragment);
        fragments.add(secondFragment);
        fragments.add(thirdFragment);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_KEY,state);
    }


    private void setState(int state) {
        if (state == 1) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.la_frame, firstFragment, LAFragmentFirst.TAG);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            prevButton.setVisibility(View.GONE);
        }
        if (state == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.la_frame, secondFragment, LAFragmentSecond.TAG).
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            prevButton.setVisibility(View.VISIBLE);
        }
        if (state==3){
            getSupportFragmentManager().beginTransaction().replace(R.id.la_frame, thirdFragment, LAFragmentThird.TAG).
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
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
