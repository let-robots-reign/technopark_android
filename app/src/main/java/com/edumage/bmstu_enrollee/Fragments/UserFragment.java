package com.edumage.bmstu_enrollee.Fragments;

import android.animation.Animator;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.edumage.bmstu_enrollee.DbEntities.UserInfo;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.LAFirstViewModel;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class UserFragment extends Fragment {
    private EditText editName;
    private EditText editDate;
    private LAFirstViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(LAFirstViewModel.class);

        if (savedInstanceState == null) {
            try {
                model.init();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        model.getDate().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                editDate.setText(s);
            }
        });
        model.getName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                editName.setText(s);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_fragment, container, false);
        final TextView questionView = v.findViewById(R.id.question_text);
        final TextView answerView = v.findViewById(R.id.answer_text);

        answerView.setAlpha(0f);
        questionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        questionView.setClickable(false);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        questionView.setClickable(true);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                };
                float alpha = 0;
                if (answerView.getAlpha() == 0) alpha = 1;
                answerView.animate()
                        .alpha(alpha)
                        .setDuration(500)
                        .setListener(animatorListener)
                        .start();

            }
        });

        Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setTitle(R.string.user_info);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBack();
            }
        });

        editName = v.findViewById(R.id.edittextName);
        editDate = v.findViewById(R.id.edittextdate);

        Button acceptButton = v.findViewById(R.id.button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String date = editDate.getText().toString();
                model.setName(name);
                model.setDate(date);
                int code = model.validateData();
                if (code == model.NO_NAME_WARNING) {
                    Toast.makeText(getContext(),
                            getResources().getString(R.string.alert_name),
                            Toast.LENGTH_SHORT).show();
                } else if (code == model.NO_DATE_WARNING) {
                    Toast.makeText(getActivity(),
                            getResources().getString(R.string.alert_date),
                            Toast.LENGTH_SHORT).show();
                } else if (code==model.NO_WARNINGS){
                    model.replaceUserInfo(new UserInfo(name, date));
                    Toast.makeText(getActivity(),
                            requireActivity().getResources().getString(R.string.date_is_up_to_date),
                            Toast.LENGTH_SHORT).show();
                    navigateBack();
                }
            }
        });

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDatePicker();
            }
        });

        return v;
    }

    //TODO repeating code. Same in LAFragmentFirst
    //
    private void callDatePicker() {
        if (getContext() != null) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DataPickerTheme,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String editTextDateParam = getDateComponent(dayOfMonth) + "." +
                                    (getDateComponent(monthOfYear + 1)) + "." + year;
                            editDate.setText(editTextDateParam);
                        }
                    }, Calendar.getInstance().get(Calendar.YEAR) - 18, 7, 1);
            datePickerDialog.show();
        }
    }

    private String getDateComponent(int date) {
        return (date < 10) ? "0" + date : String.valueOf(date);
    }

    private void navigateBack() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_userFragment_to_home_tab);
    }
}
