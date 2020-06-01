package com.edumage.bmstu_enrollee;

import android.app.Application;

import com.edumage.bmstu_enrollee.DbRepo.DbRepository;
import com.edumage.bmstu_enrollee.ParsingRepo.NewsParsing;
import com.edumage.bmstu_enrollee.ParsingRepo.StatsScoresParsing;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class BmstuApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        installTrustManager();
        scheduleWork();
        DbRepository.init(this);
        XmlDataStorage.init();
        StatsScoresParsing.init();
        NewsParsing.init();
    }

    private void scheduleWork() {
        String TAG = "PARSING_WORK";
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(ParsingWorker.class,
                1, TimeUnit.HOURS)
                .setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).build())
                .addTag(TAG).build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("PARSING_WORK",
                ExistingPeriodicWorkPolicy.KEEP, workRequest);
    }

    private void installTrustManager() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }
        }};
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
