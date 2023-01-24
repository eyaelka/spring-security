package com.example.demospringsecurity.services.implementations;

import com.example.demospringsecurity.DAO.StudentRepository;
import com.example.demospringsecurity.models.Student;
import com.example.demospringsecurity.services.interfaces.StudentInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional

public class StudentInterfaceImpl implements StudentInterface {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public Student saveStudent(Student student) {
        student.setId(null);
        student.setPassword(bCryptPasswordEncoder.encode(student.getPassword()));
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student findStudentByUsername(String username){
        return studentRepository.findStudentByUsername(username);
    }
}
