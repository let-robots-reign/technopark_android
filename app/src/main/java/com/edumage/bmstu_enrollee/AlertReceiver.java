package com.edumage.bmstu_enrollee;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import com.edumage.bmstu_enrollee.ParsingRepo.NewsParsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.core.app.NotificationCompat;
import androidx.navigation.NavDeepLinkBuilder;

public class AlertReceiver extends BroadcastReceiver {
    private static final String APP_PREFERENCES = "APP_PREFERENCES";
    private static final String NEWS_COUNT = "NEWS_COUNT";
    private static final String NEWS_CHANNEL_ID = "NEWS_CHANNEL";
    private static final int NEWS_NOTIFY_ID = 1;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        parseInBackground();
    }

    private void parseInBackground() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<NewsItem> news = new ArrayList<>();
                NewsParsing newsParsing = NewsParsing.getInstance();
                try {
                    news.addAll(newsParsing.parseNewsList(FeedType.NEWS));
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
//                if (preferences.contains(NEWS_COUNT)) {
//                    if (news.size() > preferences.getInt(NEWS_COUNT, 0)) {
//                        showNotification(news.get(0));
//                    }
//                } else {
//                    preferences.edit().putInt(NEWS_COUNT, news.size()).apply();
//                }
                showNotification(news.get(0));
            }
        }).start();
    }

    private void showNotification(NewsItem newsItem) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NEWS_CHANNEL_ID,
                    context.getResources().getString(R.string.news_channel),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.enableVibration(false);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, NEWS_CHANNEL_ID)
                .setContentTitle(context.getResources().getString(R.string.fresh_news))
                .setContentText(newsItem.getTitle())
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(createPendingIntent());
        notificationManager.notify(NEWS_NOTIFY_ID, notification.build());
    }

    private PendingIntent createPendingIntent() {
        return new NavDeepLinkBuilder(context)
                .setComponentName(MainActivity.class)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.newsFragment)
                .createPendingIntent();
    }
}
