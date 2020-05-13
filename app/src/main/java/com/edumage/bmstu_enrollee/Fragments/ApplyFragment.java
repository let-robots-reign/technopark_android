package com.edumage.bmstu_enrollee.Fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        SharedPreferences preferences = requireActivity().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        int currentStep = preferences.getInt(CURRENT_STEP, 0);
        String[] budgetSteps = getResources().getStringArray(R.array.application_steps);
        String step = budgetSteps[currentStep];

        ProgressBar progressBar = rootView.findViewById(R.id.circularProgressBar);
        TextView progressTitle = rootView.findViewById(R.id.progress_title);
        int percent = (int) Math.round((double)currentStep / (budgetSteps.length - 1) * 100);
        progressBar.setProgress(percent);
        progressTitle.setText(percent + "%");

        TextView progressDescription = rootView.findViewById(R.id.progress_description);
        String[] descriptions = getResources().getStringArray(R.array.application_descriptions);
        progressDescription.setText(descriptions[percent / (100 / descriptions.length)]);

        View nestedCard = rootView.findViewById(R.id.step_card);
        ((TextView)nestedCard.findViewById(R.id.step_title)).setText(R.string.current_step_title);
        ((TextView)nestedCard.findViewById(R.id.step_text)).setText(step);

        return rootView;
    }
}
