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

    public DisciplineAdapter(DisciplineCardClick clickListener) {
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
        if (data!=null) {
            return data.size();
        } else {
            return 0;
        }
    }

    public void setData(ArrayList<Discipline> data) {
        this.data = data;
    }

    public ArrayList<Discipline> getEnabled(){
        ArrayList<Discipline> res= new ArrayList<Discipline>();
        for (Discipline d:data){
            if (d.getStatus())res.add(d);
        }
        return res;
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
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(false);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    enabled = isChecked;
                    if (enabled && (onDisciplineClick.getChosenDisciplines() >= 3)) {
                        Toast.makeText(context, context.getText(R.string.disciplines_alert), Toast.LENGTH_SHORT).show();
                        enabled = false;
                        //checkBox.setChecked(enabled);
                    } /*else {
                        if (enabled) {
                            onDisciplineClick.incrementChosen();
                        } else {
                            onDisciplineClick.decrementChosen();
                        }
                    }*/
                    UpdateState();
                }
            });
        }

        private void UpdateState() {
            checkBox.setChecked(enabled);
            discipline.setStatus(enabled);
        }


        void setDiscipline(Discipline d) {
            discipline = d;
            name.setText(d.getName());
            // слишком длинные названия программ не влезают на карточку
            if (d.getName().length() > 50) {
                name.setTextSize(14);
            } else {
                name.setTextSize(16);
            }
            number.setText(String.valueOf(d.getNumber()));
            form.setText(d.getForm());
            enabled = d.getStatus();
            UpdateState();
        }
    }

    public interface DisciplineCardClick {
        int getChosenDisciplines();

        /*void incrementChosen();

        void decrementChosen();*/
    }
}
