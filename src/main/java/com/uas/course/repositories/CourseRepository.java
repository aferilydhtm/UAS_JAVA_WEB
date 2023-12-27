package com.uas.course.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uas.course.models.Course;
import com.uas.course.models.Person;

public interface CourseRepository extends JpaRepository<Course, Integer> {

	Page<Course> findAllByPersonsContaining(Person member, Pageable pageable);

	Page<Course> findAllByPersonsNotContaining(Person member, Pageable pageable);

}
