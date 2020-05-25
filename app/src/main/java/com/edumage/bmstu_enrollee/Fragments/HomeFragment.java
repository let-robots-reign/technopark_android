package com.edumage.bmstu_enrollee.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements View.OnClickListener, DocumentStepsAdapter.DoneClickListener {
    private ExamScoresAdapter examScoresAdapter;
    private DocumentStepsAdapter stepsAdapter;
    private RecyclerView examResults;
    private RecyclerView steps;

    private List<TextView> scoresTexts;
    private List<ProgressBar> progressBars;
    private List<ImageView> downloadIcons;
    private List<TextView> userscores;

    private List<ChosenProgram> programs;
    private HomeFragmentViewModel model;

    private static final int EGE_EDIT_DIALOG = 0;
    private static final int DISCIPLINES_EDIT_DIALOG = 1;
    private static final String APP_PREFERENCES = "APP_PREFERENCES";
    private static final String CURRENT_STEP = "CURRENT_DOCUMENTS_STEP";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        try {
            programs = model.getChosenPrograms();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (savedInstanceState == null) {
            // don't reload data after rotation
            try {
                model.init(programs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            createScoresList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        createDocumentStepsList();
    }

    @Override
    public void onResume() {
        super.onResume();
        startParsing();
    }

    private void createScoresList() throws InterruptedException {
        // get exam scores from DB
        List<ExamPoints> points = model.getExamPoints();
        List<ExamScore> examResults = new ArrayList<>();
        for (ExamPoints p : points) {
            examResults.add(new ExamScore(p.getExamName(), p.getExamScore()));
        }
        examScoresAdapter = new ExamScoresAdapter(examResults);
    }

    private void createDocumentStepsList() {
        List<DocumentStep> steps = new ArrayList<>();
        String[] budgetSteps = getResources().getStringArray(R.array.application_steps);
        for (int i = 0; i < budgetSteps.length; ++i) {
            steps.add(new DocumentStep(budgetSteps[i], getDocumentCardStatus(i)));
        }
        if (getCurrentStepPosition() == budgetSteps.length) {
            steps.add(new DocumentStep(getResources().getString(R.string.all_steps_completed),
                    DocumentStepStatus.COMPLETED_STEP));
        }

        stepsAdapter = new DocumentStepsAdapter(steps, this);
    }

    @Override
    public void onButtonClick(final int current) {
        List<DocumentStep> newList = stepsAdapter.getStepsList();
        SharedPreferences preferences = requireActivity().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        preferences.edit().putInt(CURRENT_STEP, current + 1).apply();
        newList.get(current).setStepStatus(DocumentStepStatus.COMPLETED_STEP);
        if (current == stepsAdapter.getItemCount() - 1) {
            newList.add(new DocumentStep(getResources().getString(R.string.all_steps_completed),
                    DocumentStepStatus.COMPLETED_STEP));
        } else {
            newList.get(current + 1).setStepStatus(DocumentStepStatus.CURRENT_STEP);
        }

        stepsAdapter.setStepsList(newList);
        stepsAdapter.notifyItemChanged(current);
        stepsAdapter.notifyItemChanged(current + 1);
        LinearLayoutManager manager = (LinearLayoutManager) steps.getLayoutManager();
        if (manager != null) {
            manager.scrollToPositionWithOffset(current + 1, 0);
        }
    }

    private void startParsing() {
        model.getParsingScores().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> scores) {

                if (scores.size() == 0) {
                    scoresTexts.get(0).setText(getResources().getString(R.string.last_reload)); // last reload TextView
                    for (int i = 1; i < programs.size() + 1; ++i) {
                        scoresTexts.get(i).setVisibility(View.INVISIBLE);
                        progressBars.get(i - 1).setVisibility(View.VISIBLE);
                        //userscores.get(i).setVisibility(View.INVISIBLE);
                    }
                } else {
                    scoresTexts.get(0).setText(getResources().getString(R.string.last_reload) + " " + scores.get(0));
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
            case R.id.edit_ege:
                showDialogFragment(EGE_EDIT_DIALOG);
                break;
            case R.id.edit_disciplines:
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
        try {
            name.setText(model.getUserInfo().getUserName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ImageView changeName = rootView.findViewById(R.id.edit_name);
        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_home_tab_to_userFragment);
            }
        });

        examResults = rootView.findViewById(R.id.exam_scores_list);
        examResults.setLayoutManager(new LinearLayoutManager(getActivity(),
                RecyclerView.VERTICAL, false));
        examResults.setAdapter(examScoresAdapter);

        steps = rootView.findViewById(R.id.documents_steps);
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

        List<TextView> yourscores = Arrays.asList(
                (TextView) rootView.findViewById(R.id.yourscore1),
                (TextView) rootView.findViewById(R.id.yourscore2),
                (TextView) rootView.findViewById(R.id.yourscore3));

        ImageView edit_ege = rootView.findViewById(R.id.edit_ege);
        ImageView edit_disciplines = rootView.findViewById(R.id.edit_disciplines);
        edit_ege.setOnClickListener(this);
        edit_disciplines.setOnClickListener(this);

        // displaying the programs user has chosen
        List<TextView> programsTexts = Arrays.asList(
                (TextView) rootView.findViewById(R.id.program1),
                (TextView) rootView.findViewById(R.id.program2),
                (TextView) rootView.findViewById(R.id.program3));

        userscores = Arrays.asList(
                (TextView) rootView.findViewById(R.id.userscore1),
                (TextView) rootView.findViewById(R.id.userscore2),
                (TextView) rootView.findViewById(R.id.userscore3));

        model.getUserscoresLiveData().observe(getViewLifecycleOwner(), new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> integers) {
                for (int i = 0; i < integers.size(); i++) {
                    userscores.get(i).setText(String.valueOf(integers.get(i)));
                }
            }
        });

        for (int i = 0; i < programs.size(); ++i) {
            programsTexts.get(i).setText(programs.get(i).getProgramName());
        }
        // hide views if user had chosen less than three programs
        for (int i = programs.size(); i < 3; ++i) {
            programsTexts.get(i).setVisibility(View.GONE);
            scoresTexts.get(i + 1).setVisibility(View.GONE);
            progressBars.get(i).setVisibility(View.GONE);
            downloadIcons.get(i).setVisibility(View.GONE);
            userscores.get(i).setVisibility(View.GONE);
            yourscores.get(i).setVisibility(View.GONE);
        }

        ImageView icRefresh = rootView.findViewById(R.id.refresh);
        icRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    model.init(programs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        TextView questionTextView = rootView.findViewById(R.id.textView_question_about_score);
        if (programs.size() == 0) {
            questionTextView.setText(getResources().getString(R.string.no_programs_chosen));
        } else {
            questionTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogAboutPassScore();
                }
            });
        }

        return rootView;
    }

    private void showDialogAboutPassScore() {
        if (getContext() == null) return;
        AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
        adb.setTitle(R.string.passing_score);
        adb.setMessage(R.string.explanation_passing_score);
        AlertDialog ad = adb.create();
        ad.show();
    }

    private DocumentStepStatus getDocumentCardStatus(int cardNumber) {
        int currentStep = getCurrentStepPosition();
        if (cardNumber < currentStep) {
            return DocumentStepStatus.COMPLETED_STEP;
        } else if (cardNumber > currentStep) {
            return DocumentStepStatus.FUTURE_STEP;
        }
        return DocumentStepStatus.CURRENT_STEP;
    }

    private int getCurrentStepPosition() {
        SharedPreferences preferences = requireActivity().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        return preferences.getInt(CURRENT_STEP, 0);
    }

    private void showDialogFragment(int dialog_id) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        switch (dialog_id) {
            case DISCIPLINES_EDIT_DIALOG:
                navController.navigate(R.id.action_home_tab_to_disciplineFragment);
                break;
            case EGE_EDIT_DIALOG:
                navController.navigate(R.id.action_home_tab_to_egeFragment);
                break;
        }
    }
}
