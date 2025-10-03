package com.Thread.controller;

import com.Thread.model.Student;
import com.Thread.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RequestMapping("/students")
@RestController
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student createdStudent = studentService.createStudent(student);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.getStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(id, student);
        if (updatedStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeStudent(@PathVariable Long id) {
        studentService.removeStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countAllStudents() {
        return ResponseEntity.ok(studentService.countAllStudents());
    }

    @GetMapping("/average-age")
    public ResponseEntity<Double> getAverageAge() {
        return ResponseEntity.ok(studentService.averageAge());
    }

    @GetMapping("/names-starting-with-a")
    public ResponseEntity<List<String>> getStudentsNamesStartingWithA(){return ResponseEntity.ok(studentService.getStudentsNamesStartingWithA());}

    @GetMapping("/last-five")
    public ResponseEntity<List<Student>> getLastFiveStudents() {
        return ResponseEntity.ok(studentService.getLastFiveStudents());
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getAllStudents() {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/print-parallel")
    public ResponseEntity<Void> printParallel() {
        studentService.printStudentsParallel();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/print-synchronized")
    public ResponseEntity<Void> printSynchronized() {
        studentService.printStudentsSynchronized();
        return ResponseEntity.ok().build();
    }

}
