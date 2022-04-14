package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PasswordValidator {

	public boolean isValidPassword(String password) {
		boolean hasLowerCase = false;
		boolean hasUpperCase = false;
		boolean hasNumber = false;
		boolean hasSpecialCharacter = false;
		Set<Character> allowedSpecialChar = new HashSet<>(Arrays.asList('@', '_', '$', '%', '#'));
		int passwordLength = password.length();

		for (Character passwordCharacter : password.toCharArray()) {
			if (Character.isLowerCase(passwordCharacter))
				hasLowerCase = true;
			else if (Character.isUpperCase(passwordCharacter))
				hasUpperCase = true;
			else if (Character.isDigit(passwordCharacter))
				hasNumber = true;
			else if (allowedSpecialChar.contains(passwordCharacter))
				hasSpecialCharacter = true;
			else
				return false;
		}
		return (passwordLength >= 8 && passwordLength<=15 && hasLowerCase && hasUpperCase && hasNumber && hasSpecialCharacter);
	}
}
