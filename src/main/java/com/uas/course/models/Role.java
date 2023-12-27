package com.uas.course.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int roleId;

	@Size(min = 3, message = "at least 3 characters in role name!")
	private String name;

}
