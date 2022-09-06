package com.poseidon.app.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsIntegerValidator implements ConstraintValidator<IsInteger, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}
		Pattern integerPattern = Pattern.compile("^\\d+$");
		String integerValue = String.valueOf(value);
		return integerPattern.matcher(integerValue).matches();
	}

}
