package com.Thread.controller;

import com.Thread.exception.StudentNotFoundException;
import com.Thread.model.Student;
import com.Thread.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Test
    void testGetStudentsNamesStartingWithA() throws Exception {
        List<String> mockNames = List.of("ALBUS", "ARTHUR", "ALEX");
        when(studentService.getStudentsNamesStartingWithA()).thenReturn(mockNames);

        mockMvc.perform(MockMvcRequestBuilders.get("/students/names-starting-with-a"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0]").value("ALBUS"))
                .andExpect(jsonPath("$[1]").value("ARTHUR"))
                .andExpect(jsonPath("$[2]").value("ALEX"));
    }

    @Test
    void testGetAverageAge() throws Exception {
        when(studentService.averageAge()).thenReturn(20.5);

        mockMvc.perform(MockMvcRequestBuilders.get("/students/average-age"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("20.5"));
    }

    @Test
    public void testGetStudentById() throws Exception {

        Student student = new Student( 44, "Snape");
        when(studentService.getStudent(anyLong())).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.get("/students/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value("Snape"))
                .andExpect(jsonPath("$.age").value(44));
    }

    @Test
    public void testGetStudentByIdWhenStudentNotExist() throws Exception {

        when(studentService.getStudent(anyLong())).thenThrow(StudentNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/students/1"))
                .andDo(print())


                .andExpect(status().isNotFound());

    }

    @Test
    public void testCreateStudent() throws Exception{
        Student student = new Student(44, "Snape");
        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andDo(print())
                .andExpect(status().isOk());

    }
    @Test
    public void testUpdateStudent() throws Exception{
        Student student = new Student(44, "Snape");
        when(studentService.updateStudent(any(), any(Student.class))).thenReturn(student);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.put("/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void testDeleteStudent() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/students/1"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    private final List<Student> students = List.of(
            new Student(15, "Harry"),
            new Student(15, "Hermione"),
            new Student(16, "Ron"),
            new Student(16, "Draco"),
            new Student(16, "Luna"),
            new Student(16, "Neville")
    );

    @BeforeEach
    void setUp() {
        when(studentService.getAllStudents())
                .thenReturn(students);
    }

    @Test
    void testPrintStudentsParallel() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        mockMvc.perform(MockMvcRequestBuilders.get("/students/print-parallel"))
                .andExpect(status().isOk());

        String output = out.toString();

        // Проверяем, что все имена действительно напечатались
        students.forEach(s ->
                assertThat(output).contains(s.getName())
        );
    }

    @Test
    void testPrintStudentsSynchronized() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        mockMvc.perform(MockMvcRequestBuilders.get("/students/print-synchronized"))
                .andExpect(status().isOk());

        String output = out.toString();

        // Проверяем, что все имена действительно напечатались
        students.forEach(s ->
                assertThat(output).contains(s.getName())
        );
    }

}