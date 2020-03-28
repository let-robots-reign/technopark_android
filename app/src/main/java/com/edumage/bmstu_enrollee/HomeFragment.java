package com.edumage.bmstu_enrollee;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edumage.bmstu_enrollee.Adapters.DocumentStepsAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private DocumentStepsAdapter adapter;
    private List<DocumentStep> steps;

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
    private final String FIRST_PROGRAM = "09.03.04 Программная инженерия (Бакалавр)";
    private final String SECOND_PROGRAM = "09.03.03 Прикладная информатика (Бакалавр)";
    private final String THIRD_PROGRAM = "09.03.01 Информатика и вычислительная техника (Бакалавр)";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // sample data
        steps = new ArrayList<>();
        steps.add(new DocumentStep("Это предыдущий шаг №1", -1));
        steps.add(new DocumentStep("Это предыдущий шаг №2", -1));
        steps.add(new DocumentStep("Это текущий шаг", 0));
        steps.add(new DocumentStep("Это следующий шаг №1", 1));
        steps.add(new DocumentStep("Это следующий шаг №2", 1));

        adapter = new DocumentStepsAdapter(steps);

        // parsing AsyncTask
        CurrentScoresParsing currentScores = new CurrentScoresParsing();
        currentScores.execute();
        // parsing files
        CurrentFilesParsing currentFiles = new CurrentFilesParsing();
        currentFiles.execute();
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

        scores1 = rootView.findViewById(R.id.score1);
        scores2 = rootView.findViewById(R.id.score2);
        scores3 = rootView.findViewById(R.id.score3);

        progress1 = rootView.findViewById(R.id.progress1);
        progress2 = rootView.findViewById(R.id.progress2);
        progress3 = rootView.findViewById(R.id.progress3);

        ic1 = rootView.findViewById(R.id.ic1);
        ic2 = rootView.findViewById(R.id.ic2);
        ic3 = rootView.findViewById(R.id.ic3);

        return rootView;
    }

    private int getCurrentStepPosition() {
        // searching for the first step with status code 0
        int position = 0;
        while (steps.get(position).getStepStatus() != 0) {
            position++;
        }
        return position;
    }


    private class CurrentScoresParsing extends AsyncTask<Void, Void, Void> {
        private String score1_title;
        private String score2_title;
        private String score3_title;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = "http://priem.bmstu.ru/ru/points";
                Document doc = Jsoup.connect(url).get();

                Elements specialities = doc.select("div.speciality-container");
                Elements specialityInfo;
                String specialityScore;
                String specialityTitle;

                for (Element speciality : specialities) {
                    // all the info about speciality (title and score)
                    specialityInfo = speciality.select("div.speciality-header > table.pretty-table > tbody > tr > td");
                    specialityTitle = specialityInfo.select("h3").text();
                    specialityScore = specialityInfo.last().select("b").text();

                    // if title equals any of the programs chosen by user
                    switch (specialityTitle) {
                        case FIRST_PROGRAM:
                            score1_title = specialityScore;
                            break;
                        case SECOND_PROGRAM:
                            score2_title = specialityScore;
                            break;
                        case THIRD_PROGRAM:
                            score3_title = specialityScore;
                            break;
                    }
                }
            } catch (IOException e) {
                Log.e("PARSE", "Error in parsing scores: ", e);
                score1_title = score2_title = score3_title = "Ошибка";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            scores1.setText(score1_title);
            scores2.setText(score2_title);
            scores3.setText(score3_title);
            progress1.setVisibility(View.GONE);
            progress2.setVisibility(View.GONE);
            progress3.setVisibility(View.GONE);
        }
    }

    private class CurrentFilesParsing extends AsyncTask<Void, Void, Void> {
        private IconClickListener ic1Listener;
        private IconClickListener ic2Listener;
        private IconClickListener ic3Listener;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = "http://priem.bmstu.ru/lists.html";
                Document doc = Jsoup.connect(url).get();
                String specialityTitle;
                String fileUrl;

                Elements specialities = doc.select("div.speciality-header");
                for (Element speciality : specialities) {
                    specialityTitle = speciality.select(
                            "table.pretty-table > tbody > tr > td > h3").text();
                    fileUrl = speciality.select("table.pretty-table > tbody > tr > td")
                            .get(1).select("a").attr("abs:href");

                    switch (specialityTitle) {
                        case FIRST_PROGRAM:
                            ic1Listener = new IconClickListener(fileUrl);
                            break;
                        case SECOND_PROGRAM:
                            ic2Listener = new IconClickListener(fileUrl);
                            break;
                        case THIRD_PROGRAM:
                            ic3Listener = new IconClickListener(fileUrl);
                            break;
                    }

                }
            } catch (IOException e) {
                Log.e("PARSE", "Error in parsing files: ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ic1.setOnClickListener(ic1Listener);
            ic2.setOnClickListener(ic2Listener);
            ic3.setOnClickListener(ic3Listener);
            super.onPostExecute(aVoid);
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
}
