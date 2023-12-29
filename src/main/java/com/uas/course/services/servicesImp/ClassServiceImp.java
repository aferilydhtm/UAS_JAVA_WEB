package com.uas.course.services.servicesImp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uas.course.models.Person;
import com.uas.course.models.StudyClass;
import com.uas.course.repositories.ClassRepository;
import com.uas.course.repositories.PersonRepository;
import com.uas.course.services.ClassService;

import jakarta.validation.Valid;

@Service
@Transactional(readOnly = true)
public class ClassServiceImp implements ClassService {

	private final ClassRepository classRepository;
	private final PersonRepository personRepository;
	private final Environment environment;

	@Autowired
	public ClassServiceImp(ClassRepository classRepository, PersonRepository personRepository,
			Environment environment) {
		this.classRepository = classRepository;
		this.environment = environment;
		this.personRepository = personRepository;
	}

	@Override
	public Page<StudyClass> getAllClasses(int curPage, String sortBy, String sortDir) {
		int pageSize = Integer.parseInt(environment.getProperty("variables.pageSize"));
		Sort sort = sortDir.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(curPage - 1, pageSize, sort);

		return classRepository.findAll(pageable);
	}

	@Override
	public List<StudyClass> getAllClasses() {
		return classRepository.findAll();
	}

	@Override
	@Transactional
	public void addNewClass(@Valid StudyClass studyClass) {
		classRepository.save(studyClass);
	}

	@Override
	public Page<Person> getClassMembers(int classId, int curPage, String sortBy, String sortDir) {
		int pageSize = Integer.parseInt(environment.getProperty("variables.pageSize"));
		Sort sort = sortDir.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(curPage - 1, pageSize, sort);

		return personRepository.getClassMembers(classId, pageable);
	}

	@Override
	public StudyClass getClassById(int classId) {
		return classRepository.findById(classId).orElseThrow();
	}

}
