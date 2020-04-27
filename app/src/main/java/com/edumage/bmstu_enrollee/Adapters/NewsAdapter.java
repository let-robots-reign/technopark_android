package com.edumage.bmstu_enrollee.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edumage.bmstu_enrollee.NewsItem;
import com.edumage.bmstu_enrollee.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> cardsNews;
    private OnNewsListener newsListener;
    private List<Integer> colorsNews;
    public NewsAdapter(List<NewsItem> list, OnNewsListener listener, List<Integer> colors){
        cardsNews = list;
        newsListener = listener;
        colorsNews = colors;
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
        holder.halfOfNews.setBackgroundResource(colorsNews.get(position % colorsNews.size()));
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
        View halfOfNews, half1, half2, filterNews;

        NewsViewHolder(@NonNull View itemView, OnNewsListener listener) {
            super(itemView);
            imageOfNews = itemView.findViewById(R.id.image_of_news);
            textOfNews = itemView.findViewById(R.id.text_of_news);
            newsListener = listener;
            halfOfNews = itemView.findViewById(R.id.half_of_item_news);
            half1 = itemView.findViewById(R.id.half1_news);
            half2 = itemView.findViewById(R.id.half2_news);
            filterNews = itemView.findViewById(R.id.filter_news);
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
