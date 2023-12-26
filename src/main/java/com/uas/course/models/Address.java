package com.uas.course.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Address extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int addressId;

	@Size(min = 5, message = "at least 5 characters in address1")
	private String address1;

	private String address2;

	@Size(min = 3, message = "at least 3 characters in city")
	private String city;

	@Size(min = 5, message = "at least 5 characters in status")
	private String status;

	@NotBlank(message = "Zip Code must not be blank")
	@Pattern(regexp = "(^$|[0-9]{5})", message = "zip code must be five digits!")
	private String zipCode;

}
