package com.edumage.bmstu_enrollee.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edumage.bmstu_enrollee.EGESubject;
import com.edumage.bmstu_enrollee.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class EGEAdapter extends RecyclerView.Adapter<EGEAdapter.ViewHolder> {
    private ArrayList<EGESubject> data;

    public ArrayList<EGESubject> getPassed() {
        ArrayList<EGESubject> res = new ArrayList<>();
        for (EGESubject subject : data) {
            if (subject.isPassed()) res.add(subject);
        }
        return res;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ege_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setSubject(data.get(position));
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    public void setData(ArrayList<EGESubject> data) {
        this.data = data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        EditText editText;
        CardView card;
        EGESubject subject;
        CheckBox checkBox;
        boolean enabled;

        private void setSubject(EGESubject subject) {
            this.subject = subject;
            textView.setText(subject.getName());
            imageView.setImageResource(subject.getImg());
            if (subject.isPassed()) {
                setEnabled();
                editText.setText(String.valueOf(subject.getScore()));
            } else {
                setDisabled();
            }
        }

        private void UpdateSubject() {
            subject.setPassed(enabled);
            if (subject.isPassed()) {
                if (editText.getText().toString().length() != 0) {
                    subject.setScore(Integer.parseInt(editText.getText().toString()));
                }
            } else {
                subject.setScore(0);
            }
        }

        private ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ege_icon);
            card = itemView.findViewById(R.id.ege_card);
            textView = itemView.findViewById(R.id.ege_name);
            editText = itemView.findViewById(R.id.ege_score);
            checkBox = itemView.findViewById(R.id.ege_checkBox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    enabled = isChecked;
                    if (enabled) {
                        setEnabled();
                    } else {
                        setDisabled();
                    }
                }
            });

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    int value = 0;
                    if (s.toString().length() != 0)
                        value = Integer.parseInt(s.toString());
                    Context context = itemView.getContext();
                    if (value > 100 || value < 0) {
                        Toast.makeText(context, R.string.alert_ege, Toast.LENGTH_SHORT).show();
                        s.clear();
                    }
                    UpdateSubject();
                }
            });
        }

        private void setEnabled() {
            editText.setVisibility(View.VISIBLE);
            enabled = true;
            UpdateSubject();
            checkBox.setChecked(enabled);
        }

        private void setDisabled() {
            editText.setVisibility(View.INVISIBLE);
            enabled = false;
            UpdateSubject();
            checkBox.setChecked(enabled);
        }
    }
}
