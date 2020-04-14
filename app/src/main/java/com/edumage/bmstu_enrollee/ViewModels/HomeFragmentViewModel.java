package com.edumage.bmstu_enrollee.ViewModels;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.edumage.bmstu_enrollee.ParsingRepo.CurrentFilesParsing;
import com.edumage.bmstu_enrollee.ParsingRepo.CurrentScoresParsing;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeFragmentViewModel extends AndroidViewModel {
    private final MutableLiveData<List<String>> scoresLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<String>> filesLiveData = new MutableLiveData<>();
    private List<String> programsNames;
    private final Handler handler = new Handler(Looper.getMainLooper());

    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(List<String> names) {
        programsNames = names;
        loadScores();
        loadFiles();
    }

    public LiveData<List<String>> getParsingScores() {
        return scoresLiveData;
    }

    public LiveData<List<String>> getParsingFiles() {
        return filesLiveData;
    }

    private void loadScores() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final List<String> scores = new ArrayList<>();
                String score = "Ошибка";
                for (String name : programsNames) {
                    if (!isNetworkConnected()) {
                        score = "Нет сети";
                    } else if (!hasInternetAccess()) {
                        score = "Нет интернета";
                    } else {
                        try {
                            score = CurrentScoresParsing.getInstance().parseScore(name);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    scores.add(score);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            scoresLiveData.setValue(scores);
                        }
                    });

                }
            }
        });
        thread.start();
    }

    private void loadFiles() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final List<String> fileUrls = new ArrayList<>();
                String fileUrl = null;
                for (String name : programsNames) {
                    try {
                        fileUrl = CurrentFilesParsing.getInstance().parseFile(name);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fileUrls.add(fileUrl);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            filesLiveData.setValue(fileUrls);
                        }
                    });
                }
            }
        });
        thread.start();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                NetworkInfo ni = cm.getActiveNetworkInfo();
                if (ni != null) {
                    return (ni.isConnected() && (ni.getType() == ConnectivityManager.TYPE_WIFI
                            || ni.getType() == ConnectivityManager.TYPE_MOBILE));
                }
            } else {
                Network net = cm.getActiveNetwork();
                if (net != null) {
                    NetworkCapabilities nc = cm.getNetworkCapabilities(net);
                    return (nc != null && (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)));
                }
            }
        }
        return false;
    }

    private boolean hasInternetAccess() {
        try {
            HttpURLConnection urlc = (HttpURLConnection)
                    (new URL("http://clients3.google.com/generate_204")
                            .openConnection());
            urlc.setRequestProperty("User-Agent", "Android");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(1500);
            urlc.connect();
            return (urlc.getResponseCode() == 204 &&
                    urlc.getContentLength() == 0);
        } catch (IOException e) {
            Log.e("Connection Check", "Error checking internet connection", e);
        }
        return false;
    }
}