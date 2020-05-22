package com.edumage.bmstu_enrollee;

public class ExamScore {
    private String examName;
    private int examScore;
    private int subject_id;

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

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }
}
