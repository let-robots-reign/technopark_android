package com.edumage.bmstu_enrollee;

import android.content.Context;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class XmlDataStorage {
    private static XmlDataStorage instance;

    private static final String TAG_BUILDING = "building";
    private static final String BUILDING_NAME = "name";
    private static final String BUILDING_DESCR = "description";
    private static final String BUILDING_GEO = "geo";
    private static final String BUILDING_ADDRESS = "address";
    private static final String BUILDING_ID = "id";

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


    static void init(){
        instance = new XmlDataStorage();
    }

    public static XmlDataStorage getInstance() {
        if (instance == null) {
            instance = new XmlDataStorage();
        }
        return instance;
    }

    public void pushTask(Runnable runnable){
        executorService.execute(runnable);
    }

    public MutableLiveData<List<BuildingItem>> parseBuilding(@NotNull final Context context,final int xml)  {

       final  MutableLiveData<List<BuildingItem>> res=  new MutableLiveData<>();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    XmlPullParser parser = context.getResources().getXml(xml);
                    ArrayList<BuildingItem> list = new ArrayList<>();
                    while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                        if (parser.getEventType() == XmlPullParser.START_TAG) {
                            if (!parser.getName().equals(TAG_BUILDING)) {
                                parser.next();
                                continue;
                            }
                            BuildingItem item = new BuildingItem();

                            for (int i = 0; i < parser.getAttributeCount(); i++) {
                                String attribute = parser.getAttributeName(i);
                                switch (attribute) {
                                    case BUILDING_NAME:
                                        item.setName(parser.getAttributeValue(i));
                                        break;
                                    case BUILDING_DESCR:
                                        item.setDescription(parser.getAttributeValue(i));
                                        break;
                                    case BUILDING_GEO:
                                        item.setGeoLocation(parser.getAttributeValue(i));
                                        break;
                                    case BUILDING_ADDRESS:
                                        item.setAddress(parser.getAttributeValue(i));
                                        break;
                                    case BUILDING_ID:
                                        item.setId(Integer.parseInt(parser.getAttributeValue(i)));
                                        break;
                                    default:
                                        break;
                                }
                            }
                            list.add(item);
                        }
                        parser.next();
                    }
                    res.postValue(list);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        return res;
    }
}
