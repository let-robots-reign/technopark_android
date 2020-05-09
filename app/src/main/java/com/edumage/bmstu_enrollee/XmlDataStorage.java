package com.edumage.bmstu_enrollee;

import android.annotation.SuppressLint;
import android.content.Context;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class XmlDataStorage {

    private XmlPullParser parser;

    private static final String TAG_BUILDING="building";
    private static final String BUILDING_NAME="name";
    private static final String BUILDING_DESCR="description";
    private static final String BUILDING_GEO="geo";
    private static final String BUILDING_ADDRESS="address";
    private static final String BUILDING_ID="id";




    @SuppressLint("StaticFieldLeak")
    private static XmlDataStorage storage;

    public static XmlDataStorage getInstance(){
        if (storage==null){
           storage=new XmlDataStorage();
        }
        return storage;
    }


    public ArrayList<BuildingItem> parseBuilding(@NotNull Context context, int xml) throws XmlPullParserException, IOException {
        parser=context.getResources().getXml(xml);
        ArrayList<BuildingItem> res= new ArrayList<BuildingItem>();
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType()==XmlPullParser.START_TAG) {
                    if (!parser.getName().equals(TAG_BUILDING)){
                        parser.next();
                        continue;
                    }
                    BuildingItem item= new BuildingItem();

                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                        String attribute=parser.getAttributeName(i);
                        switch(attribute){
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
                            default:break;
                        }
                    }
                    res.add(item);
            }
            parser.next();
        }

        //parser=null;
        return res;
    }






}
