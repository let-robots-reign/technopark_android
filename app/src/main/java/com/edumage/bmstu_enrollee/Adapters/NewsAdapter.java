package com.edumage.bmstu_enrollee.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.edumage.bmstu_enrollee.NewsItem;
import com.edumage.bmstu_enrollee.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> cardsNews;

    public NewsAdapter(List<NewsItem> list){
        cardsNews = list;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.textOfNews.setText(cardsNews.get(position).getTextNews());
        holder.imageOfNews.setImageResource(cardsNews.get(position).getImgNews());
    }

    @Override
    public int getItemCount() {
        return cardsNews.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder{
        ImageView imageOfNews;
        TextView textOfNews;
        CardView cardNews;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageOfNews = itemView.findViewById(R.id.image_of_news);
            textOfNews = itemView.findViewById(R.id.text_of_news);
            cardNews = itemView.findViewById(R.id.card_news);
        }
    }
}
