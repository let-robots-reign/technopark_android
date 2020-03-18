package com.edumage.bmstu_enrollee;

import android.graphics.Color;

class DocumentStep {

    private String stepContent;
    private int stepStatus;  // can be -1 (completed), 0 (current), 1 (next)

    DocumentStep(String content, int status) {
        stepContent = content;
        stepStatus = status;
    }

    String getStepContent() {
        return stepContent;
    }

    int getStepStatus() {
        return stepStatus;
    }

    String getStepTitle() {
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

    int getStepColor() {
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
