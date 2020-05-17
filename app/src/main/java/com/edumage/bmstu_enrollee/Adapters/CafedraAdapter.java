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
import androidx.recyclerview.widget.RecyclerView;

import com.edumage.bmstu_enrollee.CafedraItem;
import com.edumage.bmstu_enrollee.Data.CafedraNames;
import com.edumage.bmstu_enrollee.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CafedraAdapter extends RecyclerView.Adapter<CafedraAdapter.CafedraHolder> {

    private Context context;
    private String nmFac;
    private String nmCaf;
    CafedraNames cafedraNames = new CafedraNames();
    //private ArrayMap<String, ArrayList<CafedraItem>> namesCaf = cafedraNames.getData();

    public CafedraAdapter(Context c, String nameFac, String num){
        context = c;
        nmFac = nameFac;
        nmCaf = num;
    }

    @NonNull
    @Override
    public CafedraHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cafedra_item, parent, false);
        return new CafedraHolder(view);
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
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(cafedraNames.getData().get(nmFac)).size();
    }

    static class CafedraHolder extends RecyclerView.ViewHolder {

        TextView nameFac, nameCaf;

        CafedraHolder(@NonNull View itemView) {
            super(itemView);
            nameFac = itemView.findViewById(R.id.nmFac);
            nameCaf = itemView.findViewById(R.id.caf);
        }
    }

}

