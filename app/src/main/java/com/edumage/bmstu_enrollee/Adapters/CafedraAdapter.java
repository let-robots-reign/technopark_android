package com.edumage.bmstu_enrollee.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.edumage.bmstu_enrollee.CafedraItem;
import com.edumage.bmstu_enrollee.Data.CafedraNames;
import com.edumage.bmstu_enrollee.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CafedraAdapter extends RecyclerView.Adapter<CafedraAdapter.CafedraHolder> {

    private Context context;
    private String nmFac;
    CafedraNames cafedraNames = new CafedraNames();
    private OnCafedraListener cafedraListener;

    public CafedraAdapter(Context c, String nameFac, OnCafedraListener listener){
        context = c;
        nmFac = nameFac;
        cafedraListener = listener;
    }

    @NonNull
    @Override
    public CafedraHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cafedra_item, parent, false);
        return new CafedraHolder(view, cafedraListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CafedraHolder holder, int position) {
        String str = "";
        if (nmFac == "ФМОП") {
            str = "-И";
        }

        holder.nameFac.setText(cafedraNames.getData().get(nmFac).keyAt(position) + str);
        holder.nameCaf.setText(Objects.requireNonNull(cafedraNames.getData().get(nmFac)).valueAt(position).getNameCaf());
        holder.shortDescribe.setText(cafedraNames.getData().get(nmFac).valueAt(position).getShortDesc());
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(cafedraNames.getData().get(nmFac)).size();
    }

    class CafedraHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        OnCafedraListener cafedraListener;
        TextView nameFac, nameCaf, shortDescribe;

        CafedraHolder(@NonNull View itemView, OnCafedraListener listener) {
            super(itemView);
            nameFac = itemView.findViewById(R.id.nmFac);
            nameCaf = itemView.findViewById(R.id.caf);
            shortDescribe = itemView.findViewById(R.id.short_describe);
            cafedraListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String cafName = cafedraNames.getData().get(nmFac).keyAt(position);
            cafedraListener.onCafedraClick(cafName);
        }

    }

    public interface OnCafedraListener {
        void onCafedraClick(String cafName);
    }

}

