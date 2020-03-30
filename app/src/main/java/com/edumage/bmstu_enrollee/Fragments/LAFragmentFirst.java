package com.edumage.bmstu_enrollee.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.edumage.bmstu_enrollee.R;
import com.edumage.bmstu_enrollee.WelcomeActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//LA means LaunchActivity
public class LAFragmentFirst extends Fragment implements View.OnClickListener, WelcomeActivity.CompletableFragment {

    public static final String TAG = "LAFragmentFirst";
    private static final String TAG_NAME="USER_NAME";
    private static final String TAG_DATE="DATE_OF_BIRTH";
    private EditText editName;
    private EditText textData;
    private String name;
    private String date;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState!=null){
            name= savedInstanceState.getString(TAG_NAME);
            date = savedInstanceState.getString(TAG_DATE);
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.la_fragment1,container,false);

        textData =v.findViewById(R.id.edittextdate);
        editName = v.findViewById(R.id.edittextName);
        textData.setRawInputType(0x00000000);
        textData.setOnClickListener(this);



        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TAG_NAME,editName.getText().toString());
        outState.putString(TAG_NAME,textData.getText().toString());
    }


    public static LAFragmentFirst newFragment(){
        return new LAFragmentFirst();
    }

    @Override
    public boolean isComplete(){
        if (editName.getText().toString().length()==0){
            Toast.makeText(getContext(),
                    getContext().getResources().getString(R.string.alert_name),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (textData.getText().toString().length()==0){

            Toast.makeText(getContext(),
                    getContext().getResources().getString(R.string.alert_date),
                    Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.edittextdate){
            callDatePicker();
        }
    }


    private void callDatePicker() {
        if (getContext()!=null){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),R.style.DataPickerTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String editTextDateParam = dayOfMonth + "." + (monthOfYear + 1) + "." + year;
                        textData.setText(editTextDateParam);
                    }
                }, 2020,7, 1);
        datePickerDialog.show();}
    }
}
