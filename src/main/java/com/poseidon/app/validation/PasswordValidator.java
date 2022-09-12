package com.poseidon.app.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

	/**
	 * A password is considered valid when the following conditions are met :
	 * - Minimum length of 8 characters
	 * - Has one or more uppercase letter
	 * - Has one or more number
	 * - Has one or more symbol
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		// Checking for nullity and length
		if ((value != null) && value.length() >= 8) {

			boolean hasNumber = false;
			boolean hasUppercaseLetter = false;
			boolean hasSymbol = false;

			char[] characters = value.toCharArray();

			// Checking for others conditions
			for (char ch : characters) {

				// If there is space characters in the password it is automatically rejected
				if (Character.isSpaceChar(ch)) {
					return false;
				}

				if (!Character.isLetterOrDigit(ch) && !Character.isSpaceChar(ch)) {
					hasSymbol = true;
				} else if (Character.isDigit(ch)) {
					hasNumber = true;
				} else if (Character.isUpperCase(ch)) {
					hasUppercaseLetter = true;
				}
			}

			return hasNumber && hasUppercaseLetter && hasSymbol;
		}

		return false;
	}

}
