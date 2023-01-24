package com.example.demospringsecurity.DAO;

import com.example.demospringsecurity.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findStudentByUsername(String username);
}
