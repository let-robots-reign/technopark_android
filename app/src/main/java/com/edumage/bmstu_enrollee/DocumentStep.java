package com.edumage.bmstu_enrollee;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;

public class DocumentStep {

    private String stepContent;
    private DocumentStepStatus stepStatus;

    public DocumentStep(String content, DocumentStepStatus status) {
        stepContent = content;
        stepStatus = status;
    }

    public String getStepContent() {
        return stepContent;
    }

    public DocumentStepStatus getStepStatus() {
        return stepStatus;
    }



    public String getStepTitle(@NonNull Context context) {
        switch (stepStatus) {
            case COMPLETED_STEP:
                return context.getString(R.string.current_step_title);
            case CURRENT_STEP:
                return context.getString(R.string.next_step);
            case FUTURE_STEP:
                return context.getString(R.string.prev_step);
            default:
                return "";
        }
    }

    public int getStepColor() {
        // completed or current
        switch (stepStatus) {
            case COMPLETED_STEP:
            case CURRENT_STEP:
                return Color.GREEN;
            case FUTURE_STEP:
                return Color.RED;
            default:
                return Color.BLACK;
        }
    }

    public void setStepStatus(DocumentStepStatus stepStatus) {
        this.stepStatus = stepStatus;
    }
}
