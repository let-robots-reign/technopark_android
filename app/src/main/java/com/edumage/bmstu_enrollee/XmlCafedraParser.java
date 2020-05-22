package com.edumage.bmstu_enrollee;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class XmlCafedraParser {
    private static XmlCafedraParser instance;

    private static final String TAG_DISCIPLINE = "Discipline";
    private static final String NAME = "FullName";
    private static final String CODE = "Kod";
    private static final String DESCRIPTION = "Describe";
    private static final String PLAN = "Plan";
    private static final String LINK = "Link";
    private static final String HOSTEL = "Hostel";

    public static XmlCafedraParser getInstance() {
        if (instance == null) {
            instance = new XmlCafedraParser();
        }
        return instance;
    }

    public CafedraPageItem parseCafedraInfo(Context context, String xmlPath) throws XmlPullParserException, IOException {
        int xmlId = context.getResources().getIdentifier(xmlPath, "xml", context.getPackageName());

        XmlPullParser parser = context.getResources().getXml(xmlId);
        CafedraPageItem item = new CafedraPageItem();
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                if (!parser.getName().equals(TAG_DISCIPLINE)) {
                    parser.next();
                    continue;
                }

                for (int i = 0; i < parser.getAttributeCount(); ++i) {
                    String attribute = parser.getAttributeName(i);
                    String value = parser.getAttributeValue(i);
                    switch (attribute) {
                        case NAME:
                            item.setName(value);
                            break;
                        case CODE:
                            item.setCode(value);
                            break;
                        case DESCRIPTION:
                            item.setDescription(value);
                            break;
                        case PLAN:
                            item.setPlan(value);
                            break;
                        case LINK:
                            item.setSiteLink(value);
                            break;
                        case HOSTEL:
                            item.setHostel(value);
                            break;
                        default:
                            break;
                    }
                }
            }
            parser.next();
        }
        return item;
    }
}
