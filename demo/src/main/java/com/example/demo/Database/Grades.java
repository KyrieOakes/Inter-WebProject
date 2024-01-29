package com.example.demo.Database;

public class Grades {
    private int id;
    private String student_name;
    private String subject;
    private float score;

    public Grades(int id, String student_name, String subject, float score){
        this.id = id;
        this.student_name = student_name;
        this.subject = subject;
        this.score = score;
    }

    public Grades(){
        id = 2000;
        student_name = "None";
        subject = "none";
        score = 0.0F;
    }

    // Getters (Make sure to include these)
    public int getId() {
        return id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public String getSubject() {
        return subject;
    }

    public float getScore() {
        return score;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Grades{" +
                "id=" + id +
                ", student_name=" + student_name +
                ", subject='" + subject + '\'' +
                ", score=" + score +
                '}';
    }


}
