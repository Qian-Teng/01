package com.example.rollcall.model;

public class Student {
    private Integer id;
    private String studentNo;
    private String name;
    private String gender;
    private Integer classScore;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Integer getClassScore() { return classScore; }
    public void setClassScore(Integer classScore) { this.classScore = classScore; }
}
