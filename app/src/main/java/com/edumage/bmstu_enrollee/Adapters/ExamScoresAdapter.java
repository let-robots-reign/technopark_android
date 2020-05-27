package com.edumage.bmstu_enrollee.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;
import com.edumage.bmstu_enrollee.ExamScore;
import com.edumage.bmstu_enrollee.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExamScoresAdapter extends RecyclerView.Adapter<ExamScoresAdapter.ScoreViewHolder> {
    private List<ExamScore> scores;

    public ExamScoresAdapter(ArrayList<ExamScore> list) {
        scores = list;
    }

    public ExamScoresAdapter(List<ExamPoints> points){
        setDataExamPoints(points);
    }

    public ExamScoresAdapter(){
        scores= new ArrayList<>();
    }

    static class ScoreViewHolder extends RecyclerView.ViewHolder {
        private TextView examName;
        private TextView examScore;

        ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            examName = itemView.findViewById(R.id.exam_name);
            examScore = itemView.findViewById(R.id.exam_score);
        }
    }

    public void setData(List<ExamScore> scores){
        this.scores=scores;
        notifyDataSetChanged();
    }

    public void setDataExamPoints(List<ExamPoints> points){
        List<ExamScore> examResults = new ArrayList<>();
        for (ExamPoints p : points) {
            examResults.add(new ExamScore(p.getExamName(), p.getExamScore()));
        }
        scores=examResults;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_scores_item, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        holder.examName.setText(scores.get(position).getExamName());
        holder.examScore.setText(String.valueOf(scores.get(position).getExamScore()));
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }
}
