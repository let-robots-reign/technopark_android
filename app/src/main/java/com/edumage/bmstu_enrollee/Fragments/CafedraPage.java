package com.edumage.bmstu_enrollee.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.edumage.bmstu_enrollee.CafedraPageItem;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.XmlCafedraParser;
import com.github.barteksc.pdfviewer.PDFView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CafedraPage extends Fragment {
    private CafedraPageItem item;
    private String nameCaf;
    private static Map<String, String> disciplinesMap = createMap();
    private PDFView pdfView;
    private OnBackPressedCallback callback;
    private OnBackPressedDispatcher backPressedDispatcher;
    private String[] plans;
    private String currentPlan;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();
        if (args != null) {
            String nameFacultet = args.getString("nameFacultet");
            nameCaf = args.getString("nameCafedra");
            String fileName = disciplinesMap.get(nameFacultet) + nameCaf.replaceAll("\\D+","");;
            try {
                item = XmlCafedraParser.getInstance().parseCafedraInfo(requireActivity(), fileName);
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
        }

        plans = item.getPlan().split("\n");

        callback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                pdfView.setVisibility(View.GONE);
                callback.setEnabled(false);
            }
        };

        backPressedDispatcher = requireActivity().getOnBackPressedDispatcher();
        backPressedDispatcher.addCallback(CafedraPage.this, callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cafedra_page, container, false);

        final Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setTitle(nameCaf);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        LinearLayout linearLayout = rootView.findViewById(R.id.plans_texts);
        for (final String plan : plans) {
            TextView planText = getPlanTextView();
            planText.setText(plan.substring(plan.indexOf("_") + 1));
            planText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentPlan = plan;
                    openPdf(currentPlan);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            backPressedDispatcher.onBackPressed();
                        }
                    });
                    callback.setEnabled(true);
                }
            });
            linearLayout.addView(planText);
        }

        pdfView = rootView.findViewById(R.id.pdfView);
        if (savedInstanceState == null) {
            pdfView.setVisibility(View.GONE);
        } else {
            openPdf(currentPlan);
        }

        TextView fullNameCafedra = rootView.findViewById(R.id.full_name_cafedra);
        TextView kodSpec = rootView.findViewById(R.id.kod_spec);
        TextView fullDescribe = rootView.findViewById(R.id.full_describe);
        TextView site = rootView.findViewById(R.id.link);
        TextView hostel = rootView.findViewById(R.id.hostel);

        fullNameCafedra.setText(item.getName());
        kodSpec.setText(item.getCode());
        fullDescribe.setText(item.getDescription());
        site.setText(item.getSiteLink());
        hostel.setText(item.getHostel());

        return rootView;
    }


    private void openPdf(String fileName) {
        pdfView.setVisibility(View.VISIBLE);
        pdfView.fromAsset(fileName)
                .password(null)
                .defaultPage(0)
                .load();
    }

    private TextView getPlanTextView() {
        TextView planText = new TextView(getActivity());
        planText.setTextColor(Color.BLACK);
        planText.setTextSize(24);
        return planText;
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
