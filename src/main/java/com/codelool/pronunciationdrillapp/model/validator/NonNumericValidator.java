package com.codelool.pronunciationdrillapp.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NonNumericValidator implements ConstraintValidator<NonNumeric, String> {


	@Override
	public void initialize(NonNumeric constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		return !s.matches(".*\\d+.*");
	}
}
