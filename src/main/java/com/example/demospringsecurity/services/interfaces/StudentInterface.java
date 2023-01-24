package com.example.demospringsecurity.services.interfaces;

import com.example.demospringsecurity.models.Student;

import java.util.List;

public interface StudentInterface {
    Student saveStudent(Student student);
    List<Student> getStudents();
    Student findStudentByUsername(String username);
}
