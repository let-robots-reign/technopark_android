package com.edumage.bmstu_enrollee.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;

public class TestDialog extends DialogFragment implements OnClickListener {

    final String LOG_TAG = "myLogs";

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("Title!").setPositiveButton("YES", this)
                .setNegativeButton("NO", this)
                .setNeutralButton("MAYBE", this)
                .setMessage("MESSAGE");
        return adb.create();
    }

    public void onClick(DialogInterface dialog, int which) {
        String i = "";
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                i = "yes";
                break;
            case Dialog.BUTTON_NEGATIVE:
                i = "no";
                break;
            case Dialog.BUTTON_NEUTRAL:
                i = "";
                break;
        }

        Log.d(LOG_TAG, "Dialog 2: " + i);
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog 2: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog 2: onCancel");
    }

}