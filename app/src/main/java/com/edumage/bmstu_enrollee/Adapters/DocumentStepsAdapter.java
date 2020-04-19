package com.edumage.bmstu_enrollee.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edumage.bmstu_enrollee.DocumentStep;
import com.edumage.bmstu_enrollee.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DocumentStepsAdapter extends RecyclerView.Adapter<DocumentStepsAdapter.StepsViewHolder> {
    private List<DocumentStep> stepsList;

    public DocumentStepsAdapter(List<DocumentStep> steps) {
        stepsList = steps;
    }

    static class StepsViewHolder extends RecyclerView.ViewHolder {
        private TextView stepTitle;
        private TextView stepText;
        private MaterialCardView stepCard;

        StepsViewHolder(@NonNull View itemView) {
            super(itemView);
            stepTitle = itemView.findViewById(R.id.step_title);
            stepText = itemView.findViewById(R.id.step_text);
            stepCard = itemView.findViewById(R.id.card);
        }
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_card, parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        DocumentStep curStep = stepsList.get(position);
        holder.stepTitle.setText(curStep.getStepTitle());
        holder.stepText.setText(curStep.getStepContent());
        holder.stepCard.setStrokeColor(curStep.getStepColor());
        holder.stepCard.setStrokeWidth(2);
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }
}
