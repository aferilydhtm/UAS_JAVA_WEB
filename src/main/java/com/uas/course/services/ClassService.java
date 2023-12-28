package com.uas.course.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.uas.course.models.Person;
import com.uas.course.models.StudyClass;

import jakarta.validation.Valid;

public interface ClassService {

	Page<StudyClass> getAllClasses(int curPage, String sortBy, String sortDir);

	List<StudyClass> getAllClasses();

	Page<Person> getClassMembers(int classId, int curPage, String sortBy, String sortDir);

	void addNewClass(@Valid StudyClass studyClass);

	StudyClass getClassById(int classId);

}
