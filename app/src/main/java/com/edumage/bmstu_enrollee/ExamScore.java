package com.edumage.bmstu_enrollee;

public class ExamScore {
    private String examName;
    private int examScore;

    public ExamScore(String examName, int examScore) {
        this.examName = examName;
        this.examScore = examScore;
    }

    public String getExamName() {
        return examName;
    }

    public int getExamScore() {
        return examScore;
    }
}
