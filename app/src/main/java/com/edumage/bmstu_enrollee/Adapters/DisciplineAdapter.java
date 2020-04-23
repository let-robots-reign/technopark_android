package com.edumage.bmstu_enrollee.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.edumage.bmstu_enrollee.Discipline;
import com.edumage.bmstu_enrollee.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class DisciplineAdapter extends RecyclerView.Adapter<DisciplineAdapter.ViewHolder> {
    private ArrayList<Discipline> data;
    private DisciplineCardClick onDisciplineClick;

    public DisciplineAdapter(ArrayList<Discipline> data, DisciplineCardClick clickListener) {
        this.data = data;
        onDisciplineClick = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.discipline_item, parent, false), parent.getContext(), onDisciplineClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setDiscipline(data.get(position));
    }

    public ArrayList<Discipline> getData() {
        return data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Discipline discipline;
        TextView name;
        TextView number;
        TextView form;
        CardView card;
        CheckBox checkBox;
        boolean enabled = false;
        Context context;

        private ViewHolder(@NonNull View itemView, final Context context,
                           final DisciplineCardClick onDisciplineClick) {
            super(itemView);
            name = itemView.findViewById(R.id.discipline_name);
            number = itemView.findViewById(R.id.discipline_number);
            form = itemView.findViewById(R.id.education_form);
            card = itemView.findViewById(R.id.discipline_card);
            checkBox = itemView.findViewById(R.id.d_checkBox);
            this.context = context;
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    enabled = checkBox.isChecked();
                    if (enabled && onDisciplineClick.getChosenDisciplines() == 3) {
                        Toast.makeText(context, context.getText(R.string.disciplines_alert), Toast.LENGTH_SHORT).show();
                        enabled = false;
                    } else {
                        if (enabled) {
                            onDisciplineClick.incrementChosen();
                        } else {
                            onDisciplineClick.decrementChosen();
                        }
                    }
                    UpdateState();
                }
            });
        }

        private void UpdateState() {
            if (enabled) {
                setEnabled();
            } else {
                setDisabled();
            }
        }

        private void setEnabled() {
            checkBox.setChecked(enabled);
            enabled = true;
            discipline.setStatus(enabled);
        }

        private void setDisabled() {
            checkBox.setChecked(enabled);
            enabled = false;
            discipline.setStatus(enabled);
        }

        void setDiscipline(Discipline d) {
            discipline = d;
            name.setText(d.getName());
            number.setText(String.valueOf(d.getNumber()));
            form.setText(d.getForm());
            enabled = d.getStatus();
            UpdateState();
        }
    }

    public interface DisciplineCardClick {
        int getChosenDisciplines();

        void incrementChosen();

        void decrementChosen();
    }
}
