package com.edumage.bmstu_enrollee;


import java.io.Serializable;

public class EGESubject implements Serializable {


    public static final int CUSTOM_ID=-1;
    public static final int RUSSIAN_ID=0;
    public static final int MATH_ID=1;
    public static final int INFORMATICS_ID=2;
    public static final int PHYSICS_ID=3;
    public static final int CHEMISTRY_ID=4;
    public static final int BIOLOGY_ID=5;
    public static final int SOCIAL_ID=6;
    public static final int ENGLISH_ID=7;

     public enum Subject{
        RUSSIAN,MATH,INFORMATICS,PHYSICS,CHEMISTRY,BIOLOGY,SOCIAL,ENGLISH,CUSTOM
     }

    private String name;
    private int score;
    private boolean isPassed;
    private int img;
    private int id= Integer.MIN_VALUE;

    public EGESubject(String name, int img_id, int id) {
        this.name = name;
        this.img = img_id;
        this.id=id;
        isPassed = false;
    }

    public int getId(){
        return id;
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
