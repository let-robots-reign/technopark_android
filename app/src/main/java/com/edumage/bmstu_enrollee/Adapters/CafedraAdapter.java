package com.edumage.bmstu_enrollee.Adapters;

import android.content.Context;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edumage.bmstu_enrollee.CafedraItem;
import com.edumage.bmstu_enrollee.Data.CafedraNames;
import com.edumage.bmstu_enrollee.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CafedraAdapter extends RecyclerView.Adapter<CafedraAdapter.CafedraHolder> {
    private String nmFac;
    private OnCafedraListener cafedraListener;
    private ArrayMap<String, CafedraItem> fac;

    public CafedraAdapter(String nameFac, OnCafedraListener listener) {
        nmFac = nameFac;
        cafedraListener = listener;
        CafedraNames cafedraNames = new CafedraNames();
        fac = cafedraNames.getData().get(nmFac);
    }

    @NonNull
    @Override
    public CafedraHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cafedra_item, parent, false);
        return new CafedraHolder(view, cafedraListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CafedraHolder holder, int position) {
        String str = "";
        if (nmFac.equals("ФМОП")) {
            str = "-И";
        }

        if (fac != null) {
            holder.nameFac.setText(fac.keyAt(position) + str);
            holder.nameCaf.setText(fac.valueAt(position).getNameCaf());
            holder.shortDescribe.setText(fac.valueAt(position).getShortDesc());
        }
    }

    @Override
    public int getItemCount() {
        return fac.size();
    }

    class CafedraHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            String cafName = fac.keyAt(position);
            cafedraListener.onCafedraClick(cafName, position);
        }
    }

    public interface OnCafedraListener {
        void onCafedraClick(String cafName, int position);
    }
}

