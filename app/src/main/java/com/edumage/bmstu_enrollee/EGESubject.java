package com.edumage.bmstu_enrollee;


import java.io.Serializable;

public class EGESubject implements Serializable {
    private String name;
    private int score;
    private boolean isPassed;
    private int img;

    public EGESubject(String name, int img_id) {
        this.name = name;
        this.img = img_id;
        isPassed = false;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean passed) {
        isPassed = passed;
    }

    public int getImg() {
        return img;
    }

/*    public static ArrayList<EGESubject> LoadEgeSubjects(Context context) {
        ArrayList<EGESubject> res = new ArrayList<>();
        String[] str = context.getResources().getStringArray(R.array.subjects);

        int[] drawables = new int[str.length];
        drawables[0] = R.drawable.russian;
        drawables[1] = R.drawable.math;
        drawables[2] = R.drawable.informatics;
        drawables[3] = R.drawable.physics;
        drawables[4] = R.drawable.chemistry;
        drawables[5] = R.drawable.biology;
        drawables[6] = R.drawable.social;
        drawables[7] = R.drawable.english;
        int i = 0;
        for (String s : str) {
            res.add(new EGESubject(s, drawables[i]));
            i++;
        }
        return res;
    }*/
}
