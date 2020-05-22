package com.edumage.bmstu_enrollee.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.edumage.bmstu_enrollee.CafedraPageItem;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.XmlCafedraParser;

import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class CafedraPage extends Fragment {
    private CafedraPageItem item;
    private String nameCaf;
    private static Map<String, String> disciplinesMap = createMap();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            String nameFacultet = args.getString("nameFacultet");
            int cafedraNumber = args.getInt("cafedraNumber");
            nameCaf = args.getString("nameCafedra");
            String fileName = disciplinesMap.get(nameFacultet) + cafedraNumber;
            try {
                item = XmlCafedraParser.getInstance().parseCafedraInfo(requireActivity(), fileName);
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cafedra_page, container, false);

        String name = item.getName();
        String kod = item.getCode();
        String describe = item.getDescription();
        String plan = item.getPlan();
        String link = item.getSiteLink();
        String host = item.getHostel();

        TextView fullNameCafedra = rootView.findViewById(R.id.full_name_cafedra);
        TextView kodSpec = rootView.findViewById(R.id.kod_spec);
        TextView fullDescribe = rootView.findViewById(R.id.full_describe);
        TextView studyPlan = rootView.findViewById(R.id.plan_of_study);
        TextView site = rootView.findViewById(R.id.link);
        TextView hostel = rootView.findViewById(R.id.hostel);

        fullNameCafedra.setText(name);
        kodSpec.setText(kod);
        fullDescribe.setText(describe);
        studyPlan.setText(plan);
        site.setText(link);
        hostel.setText(host);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setTitle(nameCaf);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        return rootView;
    }


    private static Map<String, String> createMap() {
        Map<String, String> map = new HashMap<>();
        map.put("МТ", "mt");
        map.put("ИУ", "ics");
        map.put("РЛ", "rl");
        map.put("ФН", "fn");
        map.put("СМ", "sm");
        map.put("Э", "e");
        map.put("РК", "rk");
        map.put("БМТ", "bmt");
        map.put("ИБМ", "ibm");
        map.put("Л", "l");
        map.put("ОЭП", "oep");
        map.put("РКТ", "rkt");
        map.put("АК", "ak");
        map.put("ПС", "ps");
        map.put("РТ", "rt");
        map.put("СГН", "sgn");
        map.put("ЮР", "ur");
        map.put("ФВО", "fvo");
        map.put("ГУИМЦ", "guimc");
        map.put("ФМОП", "fmop");
        map.put("ФОФ", "fof");
        return map;
    }
}
