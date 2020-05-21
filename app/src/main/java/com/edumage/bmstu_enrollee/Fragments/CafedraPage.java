package com.edumage.bmstu_enrollee.Fragments;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.edumage.bmstu_enrollee.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class CafedraPage extends Fragment {
    String content;
    String nameCaf;
    String[] files;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nameCaf = getArguments().getString("nameFacultet");

        content = "";
        String current;
        String fileName = nameCaf + ".txt";
        BufferedReader in = null;
        AssetManager assetManager = getContext().getAssets();
        try {
            InputStreamReader istream = new InputStreamReader(assetManager.open(fileName));
            in = new BufferedReader(istream);

            while ((current = in.readLine()) != null){
                content = content + current + '\n';
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cafedra_page, container, false);

        //Обработка данных из txtFullName
        String name = content.substring(content.indexOf("<FullName>") + 10, content.indexOf("</FullName>"));
        String kod = content.substring(content.indexOf("<Kod>") + 5, content.indexOf("</Kod>"));
        String describe = content.substring(content.indexOf("<Describe>") + 10, content.indexOf("</Describe>"));
        String plan = content.substring(content.indexOf("<Plan>") + 6, content.indexOf("</Plan>"));
        String link = content.substring(content.indexOf("<Link>") + 6, content.indexOf("</Link>"));
        String host = content.substring(content.indexOf("<Hostel>") + 8, content.indexOf("</Hostel>"));

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
}
