package com.codelool.pronunciationdrillapp.model.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = NonNumericValidator.class)
public @interface NonNumeric {

	String message() default "NonAlphabetic Input";
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };


}
