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
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> cardsNews;
    private OnNewsListener newsListener;
    public NewsAdapter(List<NewsItem> list, OnNewsListener listener){
        cardsNews = list;
        newsListener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view, newsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.textOfNews.setText(cardsNews.get(position).getTitle());
        String imgURL = cardsNews.get(position).getImgURL();
        if (imgURL != null) {
            Picasso.get().load(imgURL).into(holder.imageOfNews);
        } else {
            holder.imageOfNews.setImageResource(R.drawable.no_image);
        }
    }

    @Override
    public int getItemCount() {
        return cardsNews.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageOfNews;
        TextView textOfNews;
        OnNewsListener newsListener;

        NewsViewHolder(@NonNull View itemView, OnNewsListener listener) {
            super(itemView);
            imageOfNews = itemView.findViewById(R.id.image_of_news);
            textOfNews = itemView.findViewById(R.id.text_of_news);
            newsListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            NewsItem item = cardsNews.get(position);
            newsListener.onNewsClick(item.getTitle(), item.getImgURL(), item.getLinkURL());
        }
    }

    public interface OnNewsListener {
        void onNewsClick(String title, String imageURL, String linkURL);
    }
}
