package com.edumage.bmstu_enrollee.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.edumage.bmstu_enrollee.BuildingItem;
import com.edumage.bmstu_enrollee.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

public class BuildingAdapter extends Adapter<BuildingAdapter.ViewHolder> {

    private List<BuildingItem> data;
    private BuildingMap buildingMap;

    public BuildingAdapter(BuildingMap buildingMap) {
        this.buildingMap = buildingMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.building_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setBuildingItem(data.get(position), buildingMap);
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    public void setData(List<BuildingItem> data) {
        this.data = data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView descr;
        TextView address;
        ImageView img;
        BuildingItem buildingItem;
        BuildingMap buildingMap;
        Button btn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.building_name);
            descr = itemView.findViewById(R.id.building_descr);
            address = itemView.findViewById(R.id.building_address);
            img = itemView.findViewById(R.id.building_image);
            btn = itemView.findViewById(R.id.show_map_button);
            btn.setOnClickListener(this);
        }

        void setBuildingItem(BuildingItem buildingItem, BuildingMap buildingMap) {
            this.buildingItem = buildingItem;
            name.setText(buildingItem.getName());
            descr.setText(buildingItem.getDescription());
            img.setImageResource(buildingItem.getImg_id());
            address.setText(buildingItem.getAddress());
            this.buildingMap = buildingMap;
        }

        @Override
        public void onClick(View v) {
            buildingMap.showMap(buildingItem);

        }
    }

    public interface BuildingMap {
        void showMap(BuildingItem building);
    }
}
