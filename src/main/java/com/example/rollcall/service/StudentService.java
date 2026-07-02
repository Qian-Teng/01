package com.example.rollcall.service;

import com.example.rollcall.mapper.StudentMapper;
import com.example.rollcall.model.Student;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentService {
    private static final String[] NAMES = {
            "张伟", "王芳", "李娜", "刘洋", "陈静", "杨磊", "赵敏", "黄强", "周丽", "吴刚",
            "徐慧", "孙勇", "胡雪", "朱杰", "高婷", "林涛", "何琴", "郭峰", "马欣", "罗晨",
            "梁梅", "宋斌", "郑洁", "谢宇", "韩璐", "唐俊", "冯悦", "董浩", "程霞", "曹睿"
    };

    public StudentService() {
        initializeDatabase();
    }

    public List<Student> findAll() {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.getMapper(StudentMapper.class).findAll();
        }
    }

    public List<Student> rollCall(String studentNo, String gender, int count) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            List<Student> students = session.getMapper(StudentMapper.class).findByFilters(studentNo, gender);
            if (students.size() <= count) {
                return students;
            }
            return new ArrayList<>(students.subList(0, count));
        }
    }

    public List<Student> findScoreRanking() {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.getMapper(StudentMapper.class).findScoreRanking();
        }
    }

    public void adjustScore(List<Student> students, int delta) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);
            for (Student student : students) {
                mapper.updateScore(student.getId(), delta);
            }
            session.commit();
        }
    }

    private void initializeDatabase() {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);
            mapper.createTable();
            if (mapper.countStudents() == 0) {
                seedStudents(mapper);
            }
            session.commit();
        }
    }

    private void seedStudents(StudentMapper mapper) {
        List<String> names = new ArrayList<>(List.of(NAMES));
        Collections.shuffle(names);
        for (int i = 0; i < names.size(); i++) {
            Student student = new Student();
            student.setStudentNo(String.format("2026%03d", i + 1));
            student.setName(names.get(i));
            student.setGender(i % 2 == 0 ? "男" : "女");
            student.setClassScore(60 + (i * 7) % 35);
            mapper.insertStudent(student);
        }
    }
}
