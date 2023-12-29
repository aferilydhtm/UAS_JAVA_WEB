package com.uas.course.util;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Profile {

	@Size(min = 3, message = "at least three characters required in the name")
	private String name;

	@Email
	@NotBlank(message = "email shouldn't be blank!")
	private String email;

	@Pattern(regexp = "(^$|[0-9]{11})", message = "Mobile number must be 11 digits")
	private String mobileNumber;

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
