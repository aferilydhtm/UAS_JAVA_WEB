package com.uas.course.models;

import java.util.HashSet;
import java.util.Set;

import com.uas.course.annotations.FieldMatch;

import jakarta.annotation.Nullable;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = { "courses", "address", "role", "studyClass" })
@Entity
@FieldMatch.List({ @FieldMatch(first = "email", second = "confirmEmail", message = "Confirm Email Dose Not Match!"),
		@FieldMatch(first = "password", second = "confirmPassword", message = "Confirm Password Dose Not Match!") })
public class Person extends BaseEntity {

	public Person() {
		this.courses = new HashSet<>();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int personId;

	@Size(min = 3, message = "at least three characters required in the name")
	private String name;

	@Email
	@Column(updatable = false)
	@NotBlank(message = "email shouldn't be blank!")
	private String email;

	@Transient
	@NotBlank(message = "confirm email shouldn't be blank!")
	private String confirmEmail;

	@Size(min = 5, message = "at least 5 characters required in the password")
	private String password;

	@Lob
	@Nullable
	@Basic(fetch = FetchType.LAZY)
	private String photo;

	@Transient
	@NotBlank(message = "confirm password shouldn't be blank!")
	private String confirmPassword;

	@Pattern(regexp = "(^[0-9]{11}$|^[0-9]{12}$)", message = "Mobile number must be 11 or 12 digits")
	private String mobileNumber;

	@OneToOne(cascade = CascadeType.PERSIST) // fetch and targetEntity not mandatory
	@JoinColumn(name = "class_id", referencedColumnName = "classId", nullable = false)
	private StudyClass studyClass;

	@OneToOne
	@JoinColumn(name = "role_id", referencedColumnName = "roleId", nullable = false)
	private Role role;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "addressId", nullable = true)
	private Address address;

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "person_course", joinColumns = @JoinColumn(name = "person_id", referencedColumnName = "personId"), inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "courseId"))
	private Set<Course> courses;

}
