package com.edumage.bmstu_enrollee;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private String score1_title;
    private TextView scores2;
    private String score2_title;
    private TextView scores3;
    private String score3_title;

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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = "http://priem.bmstu.ru/ru/points";
                Document doc = Jsoup.connect(url).get();
                int i = 0; // для начала просто посчитаем количество направлений
                Elements specialities = doc.select("div.speciality-content");
                for (Element speciality: specialities) {
                    // для каждой специальности есть несколько направлений
                    Elements programs = speciality.select("table.pretty-table > tbody > tr");
                    for (Element program: programs) {
                        // проходим по каждому направлению в специальности
                        Log.v("PARSE", program.select("td").last().text());
                        i++;
                    }
                }
                score1_title = String.valueOf(i);
            } catch (IOException e) {
                Log.e("PARSE", "I got an error ", e);
                score1_title = "Ошибка";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            scores1.setText(score1_title);
        }
    }
}
