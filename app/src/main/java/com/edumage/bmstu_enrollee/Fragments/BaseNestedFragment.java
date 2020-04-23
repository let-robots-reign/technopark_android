package com.edumage.bmstu_enrollee.Fragments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.edumage.bmstu_enrollee.MainActivity;

public class BaseNestedFragment extends Fragment {
    // in nested fragments we must hide bottomnavigation onAttach and show it onDetach
    // it's a single-activity app so it is safe to cast to MainActivity here

    private Context context;

    @Override
    public void onAttach(@NonNull Context ctx) {
        super.onAttach(context);
        this.context = ctx;
        ((MainActivity) context).hideBottomNav();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((MainActivity) context).showBottomNav();
    }
}
