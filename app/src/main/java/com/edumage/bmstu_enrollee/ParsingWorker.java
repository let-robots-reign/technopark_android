package com.edumage.bmstu_enrollee;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;

import com.edumage.bmstu_enrollee.ParsingRepo.NewsParsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavDeepLinkBuilder;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ParsingWorker extends Worker {
    private static final String APP_PREFERENCES = "APP_PREFERENCES";
    private static final String NEWS_COUNT = "NEWS_COUNT";
    private static final String NEWS_CHANNEL_ID = "NEWS_CHANNEL";
    private static final int NEWS_NOTIFY_ID = 1;
    private Context context;

    public ParsingWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        List<NewsItem> news = new ArrayList<>();
        NewsParsing newsParsing = NewsParsing.getInstance();
        try {
            news.addAll(newsParsing.parseNewsList(FeedType.NEWS));
        } catch (IOException e) {
            e.printStackTrace();
        }

        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (preferences.contains(NEWS_COUNT)) {
            if (news.size() > preferences.getInt(NEWS_COUNT, 0)) {
                showNotification(news.get(0));
            }
        }
        preferences.edit().putInt(NEWS_COUNT, news.size()).apply();

        return Result.success();
    }

    private void showNotification(NewsItem newsItem) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NEWS_CHANNEL_ID,
                    getApplicationContext().getResources().getString(R.string.news_channel),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.enableVibration(false);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), NEWS_CHANNEL_ID)
                .setContentTitle(getApplicationContext().getResources().getString(R.string.fresh_news))
                .setContentText(newsItem.getTitle())
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(createPendingIntent());
        notificationManager.notify(NEWS_NOTIFY_ID, notification.build());
    }

    private PendingIntent createPendingIntent() {
        return new NavDeepLinkBuilder(getApplicationContext())
                .setComponentName(MainActivity.class)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.newsFragment)
                .createPendingIntent();
    }
}
