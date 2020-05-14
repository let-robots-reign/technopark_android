package com.edumage.bmstu_enrollee.Fragments;

import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.animation.Animator;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.LAFirstViewModel;

import java.util.Calendar;

public class UserFragment extends Fragment {





    public static UserFragment newInstance() {
        return new UserFragment();
    }


    private EditText editName;
    private EditText editDate;
    private LAFirstViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = ViewModelProviders.of(this).get(LAFirstViewModel.class);

        if (savedInstanceState == null) {
            model.loadData();
        }

        model.date.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                editDate.setText(s);
            }
        });

        model.name.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                editName.setText(s);
            }
        });


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.user_fragment, container, false);
        final TextView questionView = v.findViewById(R.id.question_text);
        final TextView answerView =v.findViewById(R.id.answer_text);

        answerView.setAlpha(0f);
        questionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animator.AnimatorListener animatorListener =  new Animator.AnimatorListener() {
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
                float alpha=0;
                if(answerView.getAlpha()==0)alpha=1;
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
                requireActivity().onBackPressed();
            }
        });

        editName =v.findViewById(R.id.edittextName);
        editDate =v.findViewById(R.id.edittextdate);


        Button acceptButton = v.findViewById(R.id.button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.date.setValue(editDate.getText().toString());
                model.name.setValue(editName.getText().toString());
                int code = model.validateData();
                if (code==model.NO_NAME_WARNING) {
                    Toast.makeText(getContext(),
                            requireContext().getResources().getString(R.string.alert_name),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (code==model.NO_DATE_WARNING) {
                    Toast.makeText(getActivity(),
                            requireActivity().getResources().getString(R.string.alert_date),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (code==model.NO_WARNINGS){

                    model.insertUserInfo();
                    Toast.makeText(getActivity(),
                            requireActivity().getResources().getString(R.string.date_is_up_to_date),
                            Toast.LENGTH_SHORT).show();
                    //return;
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



}
