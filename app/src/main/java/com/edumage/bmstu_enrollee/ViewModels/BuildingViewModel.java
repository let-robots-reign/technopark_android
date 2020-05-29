package com.edumage.bmstu_enrollee.ViewModels;

import android.app.Application;

import com.edumage.bmstu_enrollee.BuildingItem;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.XmlDataStorage;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class BuildingViewModel extends AndroidViewModel {

    public final MediatorLiveData<List<BuildingItem>> list = new MediatorLiveData<>();
    public final MutableLiveData<ParsingState> state = new MutableLiveData<>();

    public BuildingViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadData(BuildingType type) {
        int xml = 0;
        state.setValue(ParsingState.IN_PROGRESS);
        switch (type) {
            case CAMPUS:
                xml = R.xml.bmstu_campus;
                break;
            case HOSTEL:
                xml = R.xml.bmstu_hostel;
                break;
            default:
                break;
        }

        final int xml_file = xml;

        MutableLiveData<List<BuildingItem>> res= XmlDataStorage.getInstance().parseBuilding(getApplication(), xml_file);
        list.addSource(res, new Observer<List<BuildingItem>>() {
            @Override
            public void onChanged(List<BuildingItem> buildingItems) {
                list.postValue(buildingItems);
                state.postValue(ParsingState.SUCCESS);
            }
        });

     /*   new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<BuildingItem> items =
                            XmlDataStorage.getInstance().parseBuilding(getApplication(), xml_file);
                    list.postValue(items);
                    state.postValue(ParsingState.SUCCESS);
                } catch (Exception e) {
                    state.postValue(ParsingState.FAILURE);
                }
            }
        }).start();*/
    }

    public enum ParsingState {
        SUCCESS, IN_PROGRESS, FAILURE
    }

    public enum BuildingType {
        CAMPUS, HOSTEL
    }
}
