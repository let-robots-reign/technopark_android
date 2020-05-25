package com.edumage.bmstu_enrollee.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.edumage.bmstu_enrollee.Adapters.FacultetAdapter;
import com.edumage.bmstu_enrollee.FacultetItem;
import com.edumage.bmstu_enrollee.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FacultetFragment extends Fragment implements FacultetAdapter.OnFacultetListener {

    private FacultetAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<FacultetItem> facultetItems = new ArrayList<>();
        facultetItems.add(new FacultetItem("МТ", "Машиностроительные технологии", R.drawable.mt));
        facultetItems.add(new FacultetItem("ИУ", "Информатика и системы управления", R.drawable.iu));
        facultetItems.add(new FacultetItem("РЛ", "Радиоэлектроника и лазерная техника", R.drawable.rl));
        facultetItems.add(new FacultetItem("ФН", "Фундаментальные науки", R.drawable.fn));
        facultetItems.add(new FacultetItem("СМ", "Специальное машиностроение", R.drawable.sm));
        // TODO: add educational plans for the following faculties
//        facultetItems.add(new FacultetItem("Э", "Энергомашиностроение", R.drawable.e));
//        facultetItems.add(new FacultetItem("РК", "Робототехника и комплексная автоматизация", R.drawable.rk));
//        facultetItems.add(new FacultetItem("БМТ", "Биомедицинская техника", R.drawable.bmt));
//        facultetItems.add(new FacultetItem("ИБМ", "Инженерный бизнес и менеджмент", R.drawable.ibm));
//        facultetItems.add(new FacultetItem("Л", "Теоретическая и Прикладная лингвистика", R.drawable.l));
//        facultetItems.add(new FacultetItem("ОЭП", "Оптико-электронное приборостроение", R.drawable.oep));
//        facultetItems.add(new FacultetItem("РКТ", "Ракетно-космическая техника", R.drawable.rkt));
//        facultetItems.add(new FacultetItem("АК", "Аэрокосмический", R.drawable.ak));
//        facultetItems.add(new FacultetItem("ПС", "Приборостроительный", R.drawable.ps));
//        facultetItems.add(new FacultetItem("РТ", "Радиотехнический", R.drawable.rt));
//        facultetItems.add(new FacultetItem("СГН", "Социальные и гуманитарные науки", R.drawable.sgn));
//        facultetItems.add(new FacultetItem("ЮР", "Кафедра «Юриспруденция, интеллектуальная собственность и судебная экспертиза»", R.drawable.ur));
//        facultetItems.add(new FacultetItem("ФВО", "Военный институт", R.drawable.fvo));
//        facultetItems.add(new FacultetItem("ГУИМЦ", "Головной учебно-исследовательский и методический центр", R.drawable.guimc));
//        facultetItems.add(new FacultetItem("ФМОП", "Факультет международных образовательных программ", R.drawable.fmop));
//        facultetItems.add(new FacultetItem("ФОФ", "Физкультурно-оздоровительный факультет", R.drawable.fof));


        adapter = new FacultetAdapter(facultetItems, this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.facultet_catalog, container, false);

        RecyclerView RVfacultet = rootView.findViewById(R.id.RVfacultet);
        RVfacultet.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
        RVfacultet.setAdapter(adapter);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setTitle("Факультеты");
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
    public void onFacultetClick(String nameFacultet) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        Bundle args = new Bundle();
        args.putString("nameFacultet", nameFacultet);
        navController.navigate(R.id.action_facultetFragment_to_cafedraFragment, args);
    }
}
