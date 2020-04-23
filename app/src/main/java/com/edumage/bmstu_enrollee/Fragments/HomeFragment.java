package com.edumage.bmstu_enrollee.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edumage.bmstu_enrollee.Adapters.DocumentStepsAdapter;
import com.edumage.bmstu_enrollee.Adapters.ExamScoresAdapter;
import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;
import com.edumage.bmstu_enrollee.DocumentStep;
import com.edumage.bmstu_enrollee.DocumentStepStatus;
import com.edumage.bmstu_enrollee.ExamScore;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.HomeFragmentViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private ExamScoresAdapter examScoresAdapter;
    private DocumentStepsAdapter stepsAdapter;
    private List<DocumentStep> steps;

    private List<TextView> scoresTexts;
    private List<ProgressBar> progressBars;
    private List<ImageView> downloadIcons;

    private List<ChosenProgram> programs;
    private HomeFragmentViewModel model;

    private static final int EGE_EDIT_DIALOG = 0;
    private static final int DISCIPLINES_EDIT_DIALOG = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = ViewModelProviders.of(this).get(HomeFragmentViewModel.class);
        programs = model.getChosenPrograms();

        if (savedInstanceState == null) {
            // don't reload data after rotation
            model.init(programs);
        }

        createScoresList();
        createDocumentStepsList();
        startParsing();
    }

    private void createScoresList() {
        // get exam scores from DB
        List<ExamPoints> points = model.getExamPoints();
        List<ExamScore> examResults = new ArrayList<>();
        for (ExamPoints p : points) {
            examResults.add(new ExamScore(p.getExamName(), p.getExamScore()));
        }
        examScoresAdapter = new ExamScoresAdapter(examResults);
    }

    private void createDocumentStepsList() {
        // sample data
        steps = new ArrayList<>();
        steps.add(new DocumentStep("Это предыдущий шаг №1", DocumentStepStatus.COMPLETED_STEP));
        steps.add(new DocumentStep("Это предыдущий шаг №2", DocumentStepStatus.COMPLETED_STEP));
        steps.add(new DocumentStep("Это текущий шаг", DocumentStepStatus.CURRENT_STEP));
        steps.add(new DocumentStep("Это следующий шаг №1", DocumentStepStatus.FUTURE_STEP));
        steps.add(new DocumentStep("Это следующий шаг №2", DocumentStepStatus.FUTURE_STEP));

        stepsAdapter = new DocumentStepsAdapter(steps);
    }

    private void startParsing() {
        model.getParsingScores().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> scores) {

                if (scores.size() == 0) {
                    scoresTexts.get(0).setText(getResources().getString(R.string.last_reload, "")); // last reload TextView
                    for (int i = 1; i < programs.size() + 1; ++i) {
                        scoresTexts.get(i).setVisibility(View.INVISIBLE);
                        progressBars.get(i - 1).setVisibility(View.VISIBLE);
                    }
                } else {
                    scoresTexts.get(0).setText(getResources().getString(R.string.last_reload, scores.get(0)));
                    for (int i = 1; i < scores.size(); ++i) {
                        scoresTexts.get(i).setVisibility(View.VISIBLE);
                        scoresTexts.get(i).setText(scores.get(i));
                        progressBars.get(i - 1).setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        model.getParsingFiles().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> filesUrls) {
                for (int i = 0; i < filesUrls.size(); ++i) {
                    if (filesUrls.get(i) != null) {
                        downloadIcons.get(i).setOnClickListener(new IconClickListener(filesUrls.get(i)));
                    } else {
                        downloadIcons.get(i).setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.textView_edit_ege:
                showDialogFragment(EGE_EDIT_DIALOG);
                break;
            case R.id.textView_edit_disciplines:
                showDialogFragment(DISCIPLINES_EDIT_DIALOG);
                break;
        }
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

        TextView name = rootView.findViewById(R.id.user_name);
        name.setText(model.getUserInfo().getUserName());

        RecyclerView examResults = rootView.findViewById(R.id.exam_scores_list);
        examResults.setLayoutManager(new LinearLayoutManager(getActivity(),
                RecyclerView.VERTICAL, false));
        examResults.setAdapter(examScoresAdapter);

        RecyclerView steps = rootView.findViewById(R.id.documents_steps);
        // we need to scroll horizontally so horizontal LinearLayout is needed
        steps.setLayoutManager(new LinearLayoutManager(getActivity(),
                RecyclerView.HORIZONTAL, false));
        steps.setAdapter(stepsAdapter);
        // on start current step should be seen
        steps.scrollToPosition(getCurrentStepPosition());

        scoresTexts = Arrays.asList(
                (TextView) rootView.findViewById(R.id.last_reload),
                (TextView) rootView.findViewById(R.id.score1),
                (TextView) rootView.findViewById(R.id.score2),
                (TextView) rootView.findViewById(R.id.score3));

        progressBars = Arrays.asList(
                (ProgressBar) rootView.findViewById(R.id.progress1),
                (ProgressBar) rootView.findViewById(R.id.progress2),
                (ProgressBar) rootView.findViewById(R.id.progress3));

        downloadIcons = Arrays.asList(
                (ImageView) rootView.findViewById(R.id.ic1),
                (ImageView) rootView.findViewById(R.id.ic2),
                (ImageView) rootView.findViewById(R.id.ic3));

        TextView edit_ege = rootView.findViewById(R.id.textView_edit_ege);
        TextView edit_disciplines = rootView.findViewById(R.id.textView_edit_disciplines);
        edit_ege.setOnClickListener(this);
        edit_disciplines.setOnClickListener(this);

        // displaying the programs user has chosen
        List<TextView> programsTexts = Arrays.asList(
                (TextView) rootView.findViewById(R.id.program1),
                (TextView) rootView.findViewById(R.id.program2),
                (TextView) rootView.findViewById(R.id.program3));

        for (int i = 0; i < programs.size(); ++i) {
            programsTexts.get(i).setText(programs.get(i).getProgramName());
        }
        // hide views if user had chosen less than three programs
        for (int i = programs.size(); i < 3; ++i) {
            programsTexts.get(i).setVisibility(View.GONE);
            scoresTexts.get(i + 1).setVisibility(View.GONE);
            progressBars.get(i).setVisibility(View.GONE);
            downloadIcons.get(i).setVisibility(View.GONE);
        }


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
        // searching for the first step with status code CURRENT_STEP
        int position = 0;
        while (steps.get(position).getStepStatus() != DocumentStepStatus.CURRENT_STEP) {
            position++;
        }
        return position;
    }

    private void showDialogFragment(int dialog_id) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        DialogFragment dialogFragment = null;
        String tag = "";
        switch (dialog_id) {
            case DISCIPLINES_EDIT_DIALOG:
                dialogFragment = new DialogDisciplineFragment();
                tag = DialogDisciplineFragment.TAG;
                break;
            case EGE_EDIT_DIALOG:
                dialogFragment = new DialogEgeFragment();
                tag = DialogEgeFragment.TAG;
                break;
        }

        Fragment prev = getChildFragmentManager().findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(tag);
        if (dialogFragment != null) dialogFragment.show(ft, tag);
    }
}
