package com.edumage.bmstu_enrollee.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.edumage.bmstu_enrollee.DbEntities.UserInfo;
import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.ViewModels.LAFirstViewModel;
import com.edumage.bmstu_enrollee.WelcomeActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.Calendar;

// LA means LaunchActivity
public class LAFragmentFirst extends Fragment implements WelcomeActivity.CompletableFragment {

    public static final String TAG = "LAFragmentFirst";
    private static final String TAG_NAME = "USER_NAME";
    private static final String TAG_DATE = "DATE_OF_BIRTH";
    private EditText editName;
    private EditText editDate;
    private String name;
    private String date;

    private LAFirstViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            name = savedInstanceState.getString(TAG_NAME);
            date = savedInstanceState.getString(TAG_DATE);
        } else {
            name = "";
            date = "";
        }

        model = ViewModelProviders.of(this).get(LAFirstViewModel.class);
        model.deleteAllInfo();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.la_fragment1, container, false);

        editName = rootView.findViewById(R.id.edittextName);
        editDate = rootView.findViewById(R.id.edittextdate);

        editName.setText(name);
        editDate.setText(date);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDatePicker();
            }
        });
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (editName != null) outState.putString(TAG_NAME, editName.getText().toString());
        if (editDate != null) outState.putString(TAG_DATE, editDate.getText().toString());
    }

    @Override
    public boolean isComplete() {
        name = editName.getText().toString();
        date = editDate.getText().toString();
        if (name.length() == 0) {
            Toast.makeText(getContext(),
                    requireContext().getResources().getString(R.string.alert_name),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (date.length() == 0) {
            Toast.makeText(getActivity(),
                    requireActivity().getResources().getString(R.string.alert_date),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        UserInfo info = new UserInfo(name, date);
        model.insertUserInfo(info);

        return true;
    }

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
