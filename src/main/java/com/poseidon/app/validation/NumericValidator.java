package com.poseidon.app.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NumericValidator implements ConstraintValidator<Numeric, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}

		Pattern numberPattern = Pattern.compile("-?[0-9][0-9\\.\\,]*");
		String numberValue = String.valueOf(value);
		return numberPattern.matcher(numberValue).matches();
	}

}