package com.Thread.service;

import com.Thread.exception.StudentNotFoundException;
import com.Thread.model.Student;
import com.Thread.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student getStudent(Long id) {
        logger.debug("Was invoked method for get student by id = {}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is no student with id = {}", id);
                    return new StudentNotFoundException("Студент не найден");
                });
    }

    @Override
    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Long id, Student student) {
        logger.debug("Was invoked method for update student with id = {}", id);


        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cannot update. No student with id = {}", id);
                    return new StudentNotFoundException("Студент не найден");
                });

        existingStudent.setAge(student.getAge());
        existingStudent.setName(student.getName());

        Student updated = studentRepository.save(existingStudent);
        logger.info("Student with id = {} was successfully updated", id);

        return updated;
    }


    @Override
    public Long countAllStudents() {
        return studentRepository.count();
    }

    @Override
    public Double averageAge() {
        logger.info("Запрос на получение среднего возраста студентов");
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }

    @Override
    public List<Student> getLastFiveStudents() {return studentRepository.findTop5ByOrderByIdDesc();}


    @Override
    public void removeStudent(Long id) {
        logger.info("Student with id = {} was remove", id);

        studentRepository.deleteById(id);
    }


    public List<String> getStudentsNamesStartingWithA() {
        logger.info("Запрос на получение студентов, имена которых начинаются с 'A'");
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> name.startsWith("A"))
                .map(String::toUpperCase)
                .sorted()
                .toList();
    }


    @Override
    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public void printStudentsParallel() {
        logger.info("Вывод студентов (параллельный режим)");

        List<Student> students = studentRepository.findAll()
                .stream()
                .limit(6)
                .toList();

        if (students.size() < 6) {
            logger.warn("Недостаточно студентов для демонстрации (нужно >= 6)");
            return;
        }

        // Первые два в основном потоке
        System.out.println(students.get(0).getName());
        System.out.println(students.get(1).getName());

        // 3 и 4 в отдельном потоке
        Thread thread1 = new Thread(() -> {
            System.out.println(students.get(2).getName());
            System.out.println(students.get(3).getName());
        });

        // 5 и 6 в другом потоке
        Thread thread2 = new Thread(() -> {
            System.out.println(students.get(4).getName());
            System.out.println(students.get(5).getName());
        });

        thread1.start();
        thread2.start();
    }

    @Override
    public void printStudentsSynchronized() {
        logger.info("Вывод студентов (синхронизированный режим)");

        List<Student> students = studentRepository.findAll()
                .stream()
                .limit(6)
                .toList();

        if (students.size() < 6) {
            logger.warn("Недостаточно студентов для демонстрации (нужно >= 6)");
            return;
        }

        // Первые два в основном потоке
        printName(students.get(0).getName());
        printName(students.get(1).getName());

        // 3 и 4 в отдельном потоке
        Thread thread1 = new Thread(() -> {
            printName(students.get(2).getName());
            printName(students.get(3).getName());
        });

        // 5 и 6 в другом потоке
        Thread thread2 = new Thread(() -> {
            printName(students.get(4).getName());
            printName(students.get(5).getName());
        });

        thread1.start();
        thread2.start();
    }

    private synchronized void printName(String name) {
        System.out.println(name);
    }


}