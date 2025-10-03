package com.Thread.service;

import com.Thread.exception.FacultyNotFoundException;
import com.Thread.model.Faculty;
import com.Thread.repository.FacultyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class FacultyServiceImpl implements FacultyService{


    private final FacultyRepository facultyRepository;

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }


    @Override
    public Faculty getFaculty(Long id) {
        logger.debug("Was invoked method for get faculty by id = {}", id);
        return facultyRepository.findById(id)
                .orElseThrow(()-> {
                    logger.error("There is no faculty with id = {}", id);

                    return new FacultyNotFoundException("Факультет не найден");
                });
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");

        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty updateFaculty(Faculty faculty, Long id) {
        logger.debug("Was invoked method for update faculty");

        Faculty existingFaculty = facultyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cannot update. No faculty with id = {}", id);
                    return new FacultyNotFoundException("Факультет не найден");
                });

        existingFaculty.setColor(faculty.getColor());
        existingFaculty.setName(faculty.getName());

        Faculty updated = facultyRepository.save(existingFaculty);
        logger.info("Faculty with id = {} was successfully updated", id);

        return updated;
    }

    @Override
    public void removeFaculty(Long id) {
        logger.info("Faculty with id = {} was remove", id);

        facultyRepository.deleteById(id);
    }


    @Override
    public String getLongestFacultyName() {
        logger.info("Запрос на получение факультета с самым длинным названием");

        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElse("Факультетов нет");
    }

}
