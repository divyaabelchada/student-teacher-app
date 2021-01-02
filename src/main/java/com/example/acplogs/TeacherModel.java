package com.example.acplogs;

public class TeacherModel {

    private String teacher_name, teacher_contact, teacher_dept;

    public TeacherModel(){

    }

    public TeacherModel(String teacher_name, String teacher_contact, String teacher_dept) {
        this.teacher_name = teacher_name;
        this.teacher_contact = teacher_contact;
        this.teacher_dept = teacher_dept;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getTeacher_contact() {
        return teacher_contact;
    }

    public void setTeacher_contact(String teacher_contact) {
        this.teacher_contact = teacher_contact;
    }

    public String getTeacher_dept() {
        return teacher_dept;
    }

    public void setTeacher_dept(String teacher_dept) {
        this.teacher_dept = teacher_dept;
    }
}
