package com.edumage.bmstu_enrollee;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class EGEAdapter extends RecyclerView.Adapter<EGEAdapter.ViewHolder> {


   private ArrayList<EGESubject> data;

    public EGEAdapter(ArrayList<EGESubject> data){
        this.data=data;
    }

    public ArrayList<EGESubject> getData(){
        return data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ege_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setSubject(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView textView;
        EditText editText;
        CardView card;
        EGESubject subject;
        boolean enabled;

        private void setSubject(EGESubject subject) {
            this.subject = subject;
            textView.setText(subject.name);
            imageView.setImageResource(subject.img);
            if (subject.isPassed){
                setEnabled();
                editText.setText(String.valueOf(subject.score));
            } else {
                setDisabled();
            }
        }

        private void UpdateSubject(){
            subject.isPassed=enabled;
            if (subject.isPassed){
                if (editText.getText().toString().length()!=0){
                subject.score=Integer.parseInt(editText.getText().toString());}
            } else {
                subject.score=0;
            }
        }

        private ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ege_icon);
            card=itemView.findViewById(R.id.ege_card);
            textView=itemView.findViewById(R.id.ege_name);
            editText=itemView.findViewById(R.id.ege_score);
            itemView.setOnClickListener(this);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                        int value=0;

                        if (s.toString().length()!=0)
                        value = Integer.parseInt(s.toString());

                        Context context= itemView.getContext();
                       if(value>100 || value<0){
                           Toast.makeText(context,R.string.alert_ege,Toast.LENGTH_SHORT).show();
                           s.clear();
                       }

                       if(s.toString().length()==0){
                           editText.setText("0");
                       }
                       UpdateSubject();
                }
            });


        }

        private void setEnabled(){
            editText.setVisibility(View.VISIBLE);
            enabled=true;
            card.setCardBackgroundColor(imageView.getResources().getColor(R.color.darkGreen));
            UpdateSubject();
        }

        private void setDisabled(){
            card.setCardBackgroundColor(imageView.getResources().getColor(R.color.colorPrimary));
            editText.setVisibility(View.INVISIBLE);
            enabled=false;
            UpdateSubject();
        }

        @Override
        public void onClick(View v) {
            if (enabled){
                setDisabled();
            } else {
                setEnabled();
            }
        }
    }
}
