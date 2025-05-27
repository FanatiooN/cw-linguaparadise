package com.example.services;

import com.example.dto.CourseCreateRequest;
import com.example.entities.Cours;
import com.example.repositories.CoursRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CoursService
{
    private CoursRepository coursRepository;

    public Iterable<Cours> findAll()
    {
        return coursRepository.findAll();
    }

    public Cours createCourse(CourseCreateRequest request) {
        Cours cours = new Cours();
        cours.setName(request.getName());
        cours.setCoursPrice(request.getCoursPrice());
        cours.setDescription(request.getDescription());
        cours.setAvatar(request.getAvatar());
        return coursRepository.save(cours);
    }

    public Cours findById(int id) {
        return coursRepository.findById(id).orElse(null);
    }
}
