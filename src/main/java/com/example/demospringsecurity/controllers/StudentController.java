package com.example.demospringsecurity.controllers;

import com.example.demospringsecurity.models.Student;
import com.example.demospringsecurity.services.interfaces.StudentInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class StudentController {
    @Autowired
    private StudentInterface studentInterface;

    @GetMapping("/students")
    public List<Student> getStudents(){
        return studentInterface.getStudents();
    }

    @PostMapping("/student/save")
    public Student saveStudent(@RequestBody Student student){
        return studentInterface.saveStudent(student);
    }
}
