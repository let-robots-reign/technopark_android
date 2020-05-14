package com.edumage.bmstu_enrollee.Fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.edumage.bmstu_enrollee.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class ApplyFragment extends Fragment {
    private static final String APP_PREFERENCES = "APP_PREFERENCES";
    private static final String CURRENT_STEP = "CURRENT_DOCUMENTS_STEP";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.apply_screen, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setTitle(R.string.application_tab);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        View nestedCard = rootView.findViewById(R.id.step_card);

        SharedPreferences preferences = requireActivity().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        int currentStep = preferences.getInt(CURRENT_STEP, 0);
        String[] budgetSteps = getResources().getStringArray(R.array.application_steps);
        String step;
        if (currentStep == budgetSteps.length) {
            step = getResources().getString(R.string.all_steps_completed);
            nestedCard.setVisibility(View.GONE);
        } else {
            step = budgetSteps[currentStep];
        }

        ProgressBar progressBar = rootView.findViewById(R.id.circularProgressBar);
        TextView progressTitle = rootView.findViewById(R.id.progress_title);
        int percent = (int) Math.round((double)currentStep / budgetSteps.length * 100);
        progressTitle.setText(percent + "%");
        ProgressBarAnimation animation = new ProgressBarAnimation(progressBar, 0, percent);
        animation.setDuration(percent * 10);
        progressBar.startAnimation(animation);

        TextView progressDescription = rootView.findViewById(R.id.progress_description);
        String[] descriptions = getResources().getStringArray(R.array.application_descriptions);
        if (percent == 100) {
            progressDescription.setText(step);
        } else {
            progressDescription.setText(descriptions[percent / (100 / descriptions.length)]);
        }

        ((TextView)nestedCard.findViewById(R.id.step_title)).setText(R.string.current_step_title);
        ((TextView)nestedCard.findViewById(R.id.step_text)).setText(step);

        // animate text
        progressTitle.setAlpha(0f);
        progressTitle.animate().alpha(1f).setDuration(1000);
        progressDescription.setAlpha(0f);
        progressDescription.animate().alpha(1f).setDuration(1000);

        return rootView;
    }

    private static class ProgressBarAnimation extends Animation {
        private ProgressBar progressBar;
        private float fromValue;
        private float toValue;

        ProgressBarAnimation(ProgressBar progress, float fromV, float toV) {
            progressBar = progress;
            fromValue = fromV;
            toValue = toV;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float progressValue = fromValue + (toValue - fromValue) * interpolatedTime;
            progressBar.setProgress((int)progressValue);
        }
    }
}
