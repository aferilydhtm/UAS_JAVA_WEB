package com.uas.course.services.servicesImp;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.uas.course.models.Address;
import com.uas.course.models.Course;
import com.uas.course.models.Person;
import com.uas.course.models.Role;
import com.uas.course.models.StudyClass;
import com.uas.course.repositories.ClassRepository;
import com.uas.course.repositories.CourseRepository;
import com.uas.course.repositories.PersonRepository;
import com.uas.course.repositories.RoleRepository;
import com.uas.course.services.PersonService;
import com.uas.course.util.Profile;

@Service
@Transactional(readOnly = true)
public class PersonServiceImp implements PersonService {

	private final CourseRepository courseRepository;
	private final PersonRepository personRepository;
	private final ClassRepository classRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final Environment environment;

	@Autowired
	public PersonServiceImp(PersonRepository personRepository, ClassRepository classRepository,
			RoleRepository roleRepository, CourseRepository courseRepository, PasswordEncoder passwordEncoder,
			Environment environment) {
		this.personRepository = personRepository;
		this.classRepository = classRepository;
		this.roleRepository = roleRepository;
		this.courseRepository = courseRepository;
		this.passwordEncoder = passwordEncoder;
		this.environment = environment;
	}

	@Override
	public boolean isExistUser(String email) {
		return personRepository.existsByEmail(email);
	}

	@Override
	@Transactional // override the default(read-only) to: read-write
	public void createUser(Person person, int classId) {
		StudyClass studyClass = classRepository.findById(classId).orElseThrow();
		Role role = roleRepository.findById(2).orElseThrow();

		person.setStudyClass(studyClass);
		person.setRole(role);

		person.setPassword(passwordEncoder.encode(person.getPassword()));

		personRepository.save(person);
	}

	@Override
	public Page<Person> getAllMembers(int curPage, String sortBy, String sortDir) {
		int pageSize = Integer.parseInt(environment.getProperty("variables.pageSize"));
		Sort sort = sortDir.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(curPage - 1, pageSize, sort);

		Page<Person> members = personRepository.findAll(pageable);
		return members;
	}

	@Override
	public Person getPerson(int personId) {
		return personRepository.findById(personId).orElseThrow();
	}

	@Override
	public Person getPerson(String email) {
		return personRepository.findByEmail(email).orElseThrow();
	}

	@Override
	public Profile initializeUserProfile(int userId) {

		Person person = getPerson(userId);
		Profile profile = new Profile();

		profile.setName(person.getName());
		profile.setEmail(person.getEmail());
		profile.setMobileNumber(person.getMobileNumber());

		if (person.getAddress() != null) {

			Address address = person.getAddress();

			profile.setAddress1(address.getAddress1());
			profile.setAddress2(address.getAddress2());
			profile.setCity(address.getCity());
			profile.setStatus(address.getStatus());
			profile.setZipCode(address.getZipCode());
		}
		return profile;
	}

	@Override
	@Transactional
	public void updateUserProfile(Profile profile, MultipartFile photo, int userId) {
		Person person = getPerson(userId);

		person.setName(profile.getName());
		person.setMobileNumber(profile.getMobileNumber());

		if (!photo.isEmpty() && photo != null) {
			String fileName = StringUtils.cleanPath(photo.getOriginalFilename());
			if (fileName.contains("..")) {
				System.out.println("not a a valid file");
			}
			try {
				person.setPhoto(Base64.getEncoder().encodeToString(photo.getBytes()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Address address;
		if (person.getAddress() != null)
			address = person.getAddress();
		else {
			address = new Address();
			person.setAddress(address);
		}

		address.setAddress1(profile.getAddress1());
		address.setAddress2(profile.getAddress2());
		address.setCity(profile.getCity());
		address.setStatus(profile.getStatus());
		address.setZipCode(profile.getZipCode());

		personRepository.save(person);
	}

	@Override
	@Transactional
	public void enrollCourse(int userId, int courseId) {

		Person person = getPerson(userId);
		Course course = courseRepository.findById(courseId).orElseThrow();

		person.getCourses().add(course);
		personRepository.save(person);

	}

	@Override
	@Transactional
	public void withdrawFromCourse(int userId, int courseId) {
		Person person = getPerson(userId);
		person.getCourses().removeIf(c -> c.getCourseId() == courseId);
		personRepository.save(person);
	}

	@Override
	@Transactional
	public void withdrawAllFromCourse(int courseId) {
		List<Person> persons = personRepository.findAll();
		for (Person person : persons) {
			person.getCourses().removeIf(c -> c.getCourseId() == courseId);
			personRepository.save(person);
		}
	}

	@Override
	@Transactional
	public void deleteMember(int memberId) {
		personRepository.deleteById(memberId);
	}

	@Override
	@Transactional
	public void deleteCourseFromMember(int memberId, int courseId) {
		Person person = getPerson(memberId);
		person.getCourses().removeIf(c -> c.getCourseId() == courseId);
		personRepository.save(person);
	}

	@Override
	@Transactional
	public boolean addMemberToCourse(String memberEmail, int courseId) {
		Optional<Person> person = personRepository.findByEmail(memberEmail);

		if (person.isPresent()) {
			enrollCourse(person.get().getPersonId(), courseId);
			return true;
		} else
			return false;
	}

	@Override
	@Transactional
	public boolean addMemberToClass(String memberEmail, int classId) {
		StudyClass studyClass = classRepository.findById(classId).orElse(new StudyClass());
		Optional<Person> person = personRepository.findByEmail(memberEmail);

		if (person.isPresent()) {
			person.get().setStudyClass(studyClass);
			personRepository.save(person.get());
			return true;
		}
		return false;
	}

	@Override
	public String getPhoto(int userId) {
		Person person = getPerson(userId);
		String photo = person.getPhoto();
		if (photo != null)
			return photo;
		return null;
	}

}
