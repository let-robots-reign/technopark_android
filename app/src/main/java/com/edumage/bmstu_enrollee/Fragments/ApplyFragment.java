package com.edumage.bmstu_enrollee.Fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
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
    private int currentStep;
    private String[] budgetSteps;
    private int percent;
    private ProgressBar progressBar;
    private TextView progressTitle;
    private TextView progressDescription;
    private View nestedCard;

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

        nestedCard = rootView.findViewById(R.id.step_card);

        final SharedPreferences preferences = requireActivity().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        currentStep = preferences.getInt(CURRENT_STEP, 0);
        budgetSteps = getResources().getStringArray(R.array.application_steps);
        String step = getCurrentStepText();

        progressBar = rootView.findViewById(R.id.circularProgressBar);
        progressBar.setProgress(0);
        progressTitle = rootView.findViewById(R.id.progress_title);
        progressDescription = rootView.findViewById(R.id.progress_description);
        percent = 0;
        updateProgressCard(step, percent);

        TextView title = nestedCard.findViewById(R.id.step_title);
        title.setText(R.string.current_step_title);
        final TextView stepContent = nestedCard.findViewById(R.id.step_text);
        stepContent.setText(step);

        // animate text
        progressTitle.setAlpha(0f);
        progressTitle.animate().alpha(1f).setDuration(1000);
        progressDescription.setAlpha(0f);
        progressDescription.animate().alpha(1f).setDuration(1000);

        Button doneButton = nestedCard.findViewById(R.id.button_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++currentStep;
                preferences.edit().putInt(CURRENT_STEP, currentStep).apply();
                String newStep = getCurrentStepText();
                stepContent.animate().alpha(0f).setDuration(500);
                stepContent.setText(newStep);
                stepContent.animate().alpha(1f).setDuration(500);
                updateProgressCard(newStep, percent);
            }
        });

        return rootView;
    }

    private String getCurrentStepText() {
        String step;
        if (currentStep == budgetSteps.length) {
            step = getResources().getString(R.string.all_steps_completed);
            nestedCard.setVisibility(View.GONE);
        } else {
            step = budgetSteps[currentStep];
        }
        return step;
    }

    private void updateProgressCard(String step, int oldPercent) {
        percent = (int) Math.round((double)currentStep / budgetSteps.length * 100);
        String per = percent + "%";
        progressTitle.setText(per);
        ProgressBarAnimation animation = new ProgressBarAnimation(progressBar, oldPercent, percent);
        animation.setDuration((percent - oldPercent) * 10);
        progressBar.startAnimation(animation);

        String[] descriptions = getResources().getStringArray(R.array.application_descriptions);
        if (percent == 100) {
            progressDescription.setText(step);
        } else {
            progressDescription.setText(descriptions[percent / (100 / descriptions.length)]);
        }
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
