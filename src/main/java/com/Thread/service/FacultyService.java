package com.Thread.service;

import com.Thread.model.Faculty;

public interface FacultyService {
    Faculty getFaculty(Long id);

    Faculty createFaculty(Faculty faculty);

    Faculty updateFaculty(Faculty faculty, Long id);

    void removeFaculty(Long id);

    String getLongestFacultyName();
}

