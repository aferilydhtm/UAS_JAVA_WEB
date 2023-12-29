package com.uas.course.services;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.uas.course.models.Person;
import com.uas.course.util.Profile;

public interface PersonService {

	boolean isExistUser(String email);

	void createUser(Person person, int classId);

	Page<Person> getAllMembers(int curPage, String sortBy, String sortDir);

	Person getPerson(int personId);

	Person getPerson(String email);

	Profile initializeUserProfile(int userId);

	void updateUserProfile(Profile profile, MultipartFile photo, int userId);

	void enrollCourse(int userId, int courseId);

	void withdrawFromCourse(int userId, int courseId);

	void withdrawAllFromCourse(int courseId);

	void deleteMember(int memberId);

	void deleteCourseFromMember(int memberId, int courseId);

	boolean addMemberToCourse(String memberEmail, int courseId);

	boolean addMemberToClass(String memberEmail, int classId);

	String getPhoto(int userId);
}
