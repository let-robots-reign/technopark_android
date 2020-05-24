package com.edumage.bmstu_enrollee.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edumage.bmstu_enrollee.CatalogCard;
import com.edumage.bmstu_enrollee.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CatalogCardsAdapter extends RecyclerView.Adapter<CatalogCardsAdapter.CatalogViewHolder> {
    private List<CatalogCard> cards;
    private OnCardListener onCardListener;

    public CatalogCardsAdapter(List<CatalogCard> list, OnCardListener cardListener) {
        cards = list;
        onCardListener = cardListener;
    }

    static class CatalogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private ImageView image;
        private OnCardListener onCardListener;
        private CatalogCard card;

        CatalogViewHolder(@NonNull View itemView, OnCardListener cardListener) {
            super(itemView);
            title = itemView.findViewById(R.id.catalog_text);
            image = itemView.findViewById(R.id.catalog_image);
            onCardListener = cardListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCardListener.onCardClick(card.getId());
        }
    }

    @NonNull
    @Override
    public CatalogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_card, parent, false);
        return new CatalogViewHolder(view, onCardListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogViewHolder holder, int position) {
        holder.title.setText(cards.get(position).getTitle());
        holder.image.setImageResource(cards.get(position).getImage());
        holder.card = cards.get(position);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public interface OnCardListener {
        void onCardClick(int position);
    }
}