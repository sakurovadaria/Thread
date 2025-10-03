package com.Thread.service;

import com.Thread.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentService {
    Student getStudent(Long id);

    Student createStudent(Student student);

    Student updateStudent(Long id, Student student);

    Long countAllStudents();

    Double averageAge();

    List<Student> getLastFiveStudents();

    void removeStudent(Long id);


    Collection<Student> getAllStudents();

    List<String> getStudentsNamesStartingWithA();

}