package com.edumage.bmstu_enrollee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class EGEAdapter extends RecyclerView.Adapter<EGEAdapter.ViewHolder> {


    ArrayList<EGESubject> data;

    public EGEAdapter(ArrayList<EGESubject> data){
        this.data=data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ege_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EGESubject subject = data.get(position);
        holder.textView.setText(subject.name);
        holder.imageView.setImageResource(subject.img);
        holder.setDisabled();
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
        boolean enabled;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ege_icon);
           // imageView.setScaleType(ImageView.SC);
            card=itemView.findViewById(R.id.ege_card);
            textView=itemView.findViewById(R.id.ege_name);
            editText=itemView.findViewById(R.id.ege_score);
            itemView.setOnClickListener(this);


        }

        public void setEnabled(){
            editText.setVisibility(View.VISIBLE);
            enabled=true;
            card.setCardBackgroundColor(imageView.getResources().getColor(R.color.darkGreen));

        }

        public void setDisabled(){
            card.setCardBackgroundColor(imageView.getResources().getColor(R.color.colorPrimary));
            editText.setVisibility(View.INVISIBLE);
            enabled=false;
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
