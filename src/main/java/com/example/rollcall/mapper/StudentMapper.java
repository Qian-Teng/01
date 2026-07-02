package com.example.rollcall.mapper;

import com.example.rollcall.model.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentMapper {
    void createTable();
    int countStudents();
    void insertStudent(Student student);
    List<Student> findAll();
    List<Student> findByFilters(@Param("studentNo") String studentNo, @Param("gender") String gender);
    List<Student> findScoreRanking();
    void updateScore(@Param("id") int id, @Param("delta") int delta);
}
