package com.edumage.bmstu_enrollee.Adapters;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.edumage.bmstu_enrollee.FacultetItem;
import com.edumage.bmstu_enrollee.Fragments.FacultetFragment;
import com.edumage.bmstu_enrollee.NewsItem;
import com.edumage.bmstu_enrollee.R;

import org.w3c.dom.Text;

import java.util.List;

public class FacultetAdapter extends RecyclerView.Adapter<FacultetAdapter.FacultetViewHolder> {

    private List<FacultetItem> facultets;
    private Context context;
    private OnFacultetListener facultetListener;

    public FacultetAdapter(List<FacultetItem> list, Context c, OnFacultetListener listener) {
        facultets = list;
        context = c;
        facultetListener = listener;
    }

    @NonNull
    @Override
    public FacultetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.facultet_item, parent, false);
        return new FacultetViewHolder(view, facultetListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FacultetViewHolder holder, int position) {
        holder.imgFacultet.setImageResource(facultets.get(position).getImage());
        String str = facultets.get(position).getNameShort();
        holder.facultetNm.setText(str);
        if (str.equals("РКТ") || str.equals("АК") || str.equals("ПС") || str.equals("РТ") || str.equals("ОЭП")) {
            holder.statatusFacultet.setVisibility(View.VISIBLE);
        } else {
            holder.statatusFacultet.setVisibility(View.GONE);
        }
        holder.facultetName.setText(facultets.get(position).getNameLong());
    }

    @Override
    public int getItemCount() {
        return facultets.size();
    }

    public class FacultetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView facultetNm, facultetName, statatusFacultet;
        ImageView imgFacultet;
        OnFacultetListener facultetListener;

        FacultetViewHolder(@NonNull View itemView, OnFacultetListener listener) {
            super(itemView);
            facultetNm = itemView.findViewById(R.id.facultet_nm);
            facultetName = itemView.findViewById(R.id.facultet_name);
            imgFacultet = itemView.findViewById(R.id.img_facultet);
            statatusFacultet = itemView.findViewById(R.id.status_facultet);
            facultetListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            FacultetItem item = facultets.get(position);
            facultetListener.onFacultetClick(item.getNameShort());
        }
    }

    public interface OnFacultetListener {
        void onFacultetClick(String nameFacultet);
    }
}
