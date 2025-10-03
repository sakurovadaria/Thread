package com.Thread.controller;

import com.Thread.exception.FacultyNotFoundException;
import com.Thread.model.Faculty;
import com.Thread.service.FacultyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyService;

    @Test
    void testGetLongestFacultyName() throws Exception {
        when(facultyService.getLongestFacultyName()).thenReturn("Faculty of Dark Arts");

        mockMvc.perform(MockMvcRequestBuilders.get("/faculties/longest-name"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Faculty of Dark Arts"));
    }

    @Test
    public void testGetFacultyById() throws Exception {

        Faculty faculty = new Faculty("Slytherin", "green");
        when(facultyService.getFaculty(anyLong())).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value("Slytherin"))
                .andExpect(jsonPath("$.color").value("green"));
    }

    @Test
    public void testGetFacultyByIdWhenFacultyNotExist() throws Exception {

        when(facultyService.getFaculty(anyLong())).thenThrow(FacultyNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/1"))
                .andDo(print())


                .andExpect(status().isNotFound());

    }
}