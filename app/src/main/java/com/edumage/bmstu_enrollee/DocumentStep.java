package com.edumage.bmstu_enrollee;

import android.graphics.Color;

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

    public String getStepTitle() {
        switch (stepStatus) {
            case COMPLETED_STEP:
                return "Предыдущий шаг";
            case CURRENT_STEP:
                return "Текущий шаг поступления";
            case FUTURE_STEP:
                return "Следующий шаг";
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
