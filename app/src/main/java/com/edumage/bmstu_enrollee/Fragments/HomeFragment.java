package com.edumage.bmstu_enrollee.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edumage.bmstu_enrollee.Adapters.DocumentStepsAdapter;
import com.edumage.bmstu_enrollee.DocumentStep;
import com.edumage.bmstu_enrollee.DocumentStepStatus;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.HomeFragmentViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {
    private DocumentStepsAdapter adapter;
    private List<DocumentStep> steps;

    private TextView lastReload;
    private TextView scores1;
    private TextView scores2;
    private TextView scores3;
    private ProgressBar progress1;
    private ProgressBar progress2;
    private ProgressBar progress3;
    private ImageView ic1;
    private ImageView ic2;
    private ImageView ic3;

    // пока не запоминаем выбранные направления
    // для тестирования они заданы константами
    private final List<String> programs = Arrays.asList("09.03.04 Программная инженерия (Бакалавр)",
            "09.03.03 Прикладная информатика (Бакалавр)", "09.03.01 Информатика и вычислительная техника (Бакалавр)");

    private HomeFragmentViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // sample data
        steps = new ArrayList<>();
        steps.add(new DocumentStep("Это предыдущий шаг №1", DocumentStepStatus.COMPLETED_STEP));
        steps.add(new DocumentStep("Это предыдущий шаг №2", DocumentStepStatus.COMPLETED_STEP));
        steps.add(new DocumentStep("Это текущий шаг", DocumentStepStatus.CURRENT_STEP));
        steps.add(new DocumentStep("Это следующий шаг №1", DocumentStepStatus.FUTURE_STEP));
        steps.add(new DocumentStep("Это следующий шаг №2", DocumentStepStatus.FUTURE_STEP));

        adapter = new DocumentStepsAdapter(steps);

        model = ViewModelProviders.of(this).get(HomeFragmentViewModel.class);
        model.init(programs);

        model.getParsingScores().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> scores) {
                List<TextView> scoresTexts = Arrays.asList(lastReload, scores1, scores2, scores3);
                List<ProgressBar> progressBars = Arrays.asList(progress1, progress2, progress3);

                if (scores.size() == 0) {
                    lastReload.setText(getResources().getString(R.string.last_reload, ""));
                    for (int i = 1; i < scoresTexts.size(); ++i) {
                        scoresTexts.get(i).setText("");
                        progressBars.get(i - 1).setVisibility(View.VISIBLE);
                    }
                } else {
                    lastReload.setText(getResources().getString(R.string.last_reload, scores.get(0)));
                    for (int i = 1; i < scores.size(); ++i) {
                        scoresTexts.get(i).setText(scores.get(i));
                        progressBars.get(i - 1).setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        model.getParsingFiles().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> filesUrls) {
                List<ImageView> icons = Arrays.asList(ic1, ic2, ic3);
                for (int i = 0; i < filesUrls.size(); ++i) {
                    if (filesUrls.get(i) != null) {
                        icons.get(i).setOnClickListener(new IconClickListener(filesUrls.get(i)));
                    } else {
                        icons.get(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getActivity(), "Ошибка: не удалось найти файл",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        });
    }

    private class IconClickListener implements View.OnClickListener {
        private String fileUrl;

        IconClickListener(String url) {
            fileUrl = url;
        }

        @Override
        public void onClick(View view) {
            Uri uri = Uri.parse(fileUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_screen, container, false);

        RecyclerView steps = rootView.findViewById(R.id.documents_steps);
        // we need to scroll horizontally so horizontal LinearLayout is needed
        steps.setLayoutManager(new LinearLayoutManager(getActivity(),
                RecyclerView.HORIZONTAL, false));
        steps.setAdapter(adapter);
        // on start current step should be seen
        steps.scrollToPosition(getCurrentStepPosition());

        lastReload = rootView.findViewById(R.id.last_reload);
        scores1 = rootView.findViewById(R.id.score1);
        scores2 = rootView.findViewById(R.id.score2);
        scores3 = rootView.findViewById(R.id.score3);

        progress1 = rootView.findViewById(R.id.progress1);
        progress2 = rootView.findViewById(R.id.progress2);
        progress3 = rootView.findViewById(R.id.progress3);

        ic1 = rootView.findViewById(R.id.ic1);
        ic2 = rootView.findViewById(R.id.ic2);
        ic3 = rootView.findViewById(R.id.ic3);

        ImageView icRefresh = rootView.findViewById(R.id.refresh);
        icRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.init(programs);
            }
        });

        return rootView;
    }

    private int getCurrentStepPosition() {
        // searching for the first step with status code 0
        int position = 0;
        while (steps.get(position).getStepStatus() != DocumentStepStatus.CURRENT_STEP) {
            position++;
        }
        return position;
    }
}
