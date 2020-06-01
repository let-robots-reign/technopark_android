package com.edumage.bmstu_enrollee.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edumage.bmstu_enrollee.DocumentStep;
import com.edumage.bmstu_enrollee.DocumentStepStatus;
import com.edumage.bmstu_enrollee.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DocumentStepsAdapter extends RecyclerView.Adapter<DocumentStepsAdapter.StepsViewHolder> {
    private List<DocumentStep> stepsList;
    private DoneClickListener buttonListener;
    private Context context;

    public DocumentStepsAdapter(List<DocumentStep> steps, DoneClickListener listener, Context context) {
        stepsList = steps;
        buttonListener = listener;
        this.context=context;
    }

    static class StepsViewHolder extends RecyclerView.ViewHolder {
        private TextView stepTitle;
        private TextView stepText;
        private Button doneButton;
        private MaterialCardView stepCard;

        StepsViewHolder(@NonNull View itemView, final DoneClickListener listener) {
            super(itemView);
            stepTitle = itemView.findViewById(R.id.step_title);
            stepText = itemView.findViewById(R.id.step_text);
            stepCard = itemView.findViewById(R.id.card);
            doneButton = itemView.findViewById(R.id.button_done);
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onButtonClick(getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_card, parent, false);
        return new StepsViewHolder(view, buttonListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        DocumentStep curStep = stepsList.get(position);
        holder.stepTitle.setText(curStep.getStepTitle(context));
        holder.stepText.setText(curStep.getStepContent());
        holder.stepCard.setStrokeColor(curStep.getStepColor());
        holder.stepCard.setStrokeWidth(2);
        if (curStep.getStepStatus() == DocumentStepStatus.CURRENT_STEP) {
            holder.doneButton.setVisibility(View.VISIBLE);
        } else {
            holder.doneButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    public List<DocumentStep> getStepsList() {
        return stepsList;
    }

    public void setStepsList(List<DocumentStep> stepsList) {
        this.stepsList = stepsList;
    }

    public interface DoneClickListener {
        void onButtonClick(int position);
    }
}
