package com.uas.course.validations;

import org.springframework.beans.BeanWrapperImpl;

import com.uas.course.annotations.FieldMatch;
import com.uas.course.models.Person;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Person> {

	private String firstField;
	private String secondField;

	@Override
	public void initialize(FieldMatch constraintAnnotation) {
		firstField = constraintAnnotation.first();
		secondField = constraintAnnotation.second();
	}

	@Override
	public boolean isValid(Person value, ConstraintValidatorContext context) {
		String firstValue = (String) new BeanWrapperImpl(value).getPropertyValue(firstField);
		String secondValue = (String) new BeanWrapperImpl(value).getPropertyValue(secondField);
		if (firstValue == null || secondValue == null || !firstValue.equals(secondValue))
			return false;
		return true;
	}

}
