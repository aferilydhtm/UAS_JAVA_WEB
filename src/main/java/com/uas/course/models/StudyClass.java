package com.uas.course.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(exclude = { "persons" })
@Table(name = "class")
public class StudyClass extends BaseEntity {

	public StudyClass() {
		this.persons = new HashSet<>();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int classId;

	@Size(min = 3, message = "at least 3 characters in the clss name")
	private String name;

	@OneToMany(mappedBy = "studyClass", cascade = CascadeType.PERSIST)
	private Set<Person> persons;

}
