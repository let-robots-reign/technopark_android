package com.edumage.bmstu_enrollee.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edumage.bmstu_enrollee.Adapters.CafedraAdapter;
import com.edumage.bmstu_enrollee.R;

import java.util.ArrayList;
import java.util.List;

public class CafedraFragment extends Fragment implements CafedraAdapter.OnCafedraListener{

    private CafedraAdapter adapter;
    private RecyclerView RVcafedra;
    private String nameFac;
    private String num;
    List<String> desc = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        desc.add("Целевая подготовка специалистов для базового предприятия «Красногорский завод им. С.А. Зверева»");
        desc.add("Целевая подготовка специалистов для базовых предприятий РКК «Энергия» им. С.П. Королёва и КБ ХИММАШ им. А.М. Исаева и других градообразующих предприятий");
        desc.add("Целевая подготовка специалистов для базового предприятия «НПО машиностроения»");
        desc.add("Целевая подготовка специалистов для базовых предприятий ФГУП «ЦЭНКИ»; НПЦ Автоматики и приборостроения им. ак. Н.А. Пилюгина; Московского завода электромеханической аппаратуры; Раменского приборостроительного КБ");
        desc.add("Целевая подготовка специалистов для базовых предприятий ОАО «ГСКБ» «Алмаз-Антей»; Центр Научно-исследовательский электромеханический институт (НИЭМИ).");

        nameFac = getArguments().getString("nameFacultet");

        adapter = new CafedraAdapter(getActivity(), nameFac, this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layout;
        if (nameFac == "ОЭП" || nameFac == "РКТ" || nameFac == "АК" || nameFac == "ПС" || nameFac == "РТ") {
            layout = R.layout.cafedra_otr;
        } else {
            layout = R.layout.cafedra_catalog;
        }
        View rootView = inflater.inflate(layout, container, false);
        RVcafedra = rootView.findViewById(R.id.RVcafedra);
        RVcafedra.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        RVcafedra.setAdapter(adapter);

        TextView describe = rootView.findViewById(R.id.describeCaf);
        if (nameFac == "ОЭП") {describe.setText(desc.get(0));}
        else if (nameFac == "РКТ") {describe.setText(desc.get(1));}
        else if (nameFac == "АК") {describe.setText(desc.get(2));}
        else if (nameFac == "ПС") {describe.setText(desc.get(3));}
        else if (nameFac == "РТ") {describe.setText(desc.get(4));}

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setTitle("Кафедры " + nameFac);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        return rootView;
    }

    @Override
    public void onCafedraClick(String cafName) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        Bundle args = new Bundle();
        args.putString("nameFacultet", cafName);
        navController.navigate(R.id.action_cafedraItem_to_cafedraPage, args);
    }
}
