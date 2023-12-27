package com.uas.course.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(exclude = { "persons" })
public class Course extends BaseEntity {

	public Course() {
		this.persons = new HashSet<>();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int courseId;

	@Size(min = 5, message = "at least 5 characters required!")
	private String name;

	@Min(value = 1, message = "minimum price is 1$ !")
	private int price;

	@ManyToMany(mappedBy = "courses")
	private Set<Person> persons;
}
