package com.uas.course.services;

import org.springframework.data.domain.Page;

import com.uas.course.models.Course;
import com.uas.course.models.Person;

import jakarta.validation.Valid;

public interface CourseService {

	Page<Course> getUnEnrolledCourses(int memberId, int curPage, String sortBy, String sortDir);

	Page<Course> getEnrolledCourses(int memberId, int curPage, String sortBy, String sortDir);

	Page<Course> getAllCourses(int curPage, String sortBy, String sortDir);

	Page<Person> getCourseMembers(int curPage, int courseId, String sortBy, String sortDir);

	void deleteMemberFromCourse(int memberId, int courseId);

	void addNewCourse(@Valid Course course);

	void deleteCourse(int courseId);

	Course getCourseById(int courseId);
}
