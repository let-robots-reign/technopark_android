package com.edumage.bmstu_enrollee;

import android.graphics.Color;

public class DocumentStep {

    private String stepContent;
    private int stepStatus;  // can be -1 (completed), 0 (current), 1 (next)

    public DocumentStep(String content, int status) {
        stepContent = content;
        stepStatus = status;
    }

    public String getStepContent() {
        return stepContent;
    }

    public int getStepStatus() {
        return stepStatus;
    }

    public String getStepTitle() {
        switch (stepStatus) {
            case -1:
                return "Предыдущий шаг";
            case 0:
                return "Текущий шаг подачи документов";
            case 1:
                return "Следующий шаг";
            default:
                return "";
        }
    }

    public int getStepColor() {
        // completed or current
        if (stepStatus < 1) {
            return Color.GREEN;
        } else if (stepStatus == 1) { // next
            return Color.RED;
        } else {
            return Color.BLACK;
        }
    }
}
