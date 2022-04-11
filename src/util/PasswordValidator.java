package util;

public class PasswordValidator {
	public  boolean isValidPassword(String password)
    {
            if (password.length() > 15 || password.length() < 8)
            {
            	System.out.println("Password must be less than 20 and more than 8 characters in length.");
                return false;
            }
            String upperCaseChars = "(.*[A-Z].*)";
            if (!password.matches(upperCaseChars ))
            {
            	System.out.println("Password must have atleast one uppercase character");
            	return false;
            }
            String lowerCaseChars = "(.*[a-z].*)";
            if (!password.matches(lowerCaseChars ))
            {
            	System.out.println("Password must have atleast one lowercase character");
            	return false;
            }
            String numbers = "(.*[0-9].*)";
            if (!password.matches(numbers ))
            {
            	System.out.println("Password must have atleast one number");
            	return false;
            }
            String specialChars = "(.*[@,#,$,%].*$)";
            if (!password.matches(specialChars))
            {
            	System.out.println("Password must have atleast one special character among @#$%");
            	return false;
            }
            return true; 
    }
}
