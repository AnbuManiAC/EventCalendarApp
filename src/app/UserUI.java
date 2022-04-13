package app;

import java.util.Scanner;
import database.UserManager;
import exception.InvalidCredentials;
import exception.UserAlreadyExistsException;
import exception.UserDoesNotExistsException;
import model.User;
import util.EmailValidator;
import util.PasswordValidator;

public class UserUI {
	
	UserManager userManager;
	User currentUser;
	
	
	public UserUI() {
		userManager = new UserManager();
	}
	
	private Scanner input = new Scanner(System.in);

	public void execute() {
		System.out.println("\nEvent Calendar");
		String choice;
		do {
			printUserMenu();
			System.out.print("Enter your choice : ");
			choice = input.next();
			if (choice.equals("1"))
				signup();
			else if (choice.equals("2")) {
				login();
			}
			else if(!choice.equals("3")) {
				System.out.println("Invalid option!\n");
			}

		} while (!choice.equals("3"));
		if (choice == "3")
			System.exit(0);
	}

	private void printUserMenu() {
		System.out.println("1. Signup");
		System.out.println("2. Login");
		System.out.println("3. Exit\n");
	}

	private void signup() {
		System.out.println("---Signup Page---");
		String name;
		String email;
		String password;
		while (true) {
			System.out.print("Enter name : ");
			name = input.next().trim();
			if (name.length() >0)
				break;
			else
				System.out.println("Empty name!");

		}
		while (true) {
			System.out.print("Enter email : ");
			email = input.next();
			EmailValidator emailValidator = new EmailValidator();
			if (emailValidator.isValidEmail(email))
				break;
			else
				System.out.println("Invalid email!");
		}
		if(userManager.isExistingUser(email)) {
			System.out.println("User already exists! Login to continue");
			return;
		}
		System.out.println("[ Password must be between 8 to 20 characters long\n  Password must contains one uppercase, lowercase character and one number\n  Password must contains one special characters among @#$% ]");
		while (true) {
			System.out.print("Enter password : ");
			password = input.next();
			PasswordValidator passwordValidator = new PasswordValidator();
			if (passwordValidator.isValidPassword(password))
				break;
		}

		try {
			userManager.signup(name, email, password);
			System.out.println("Successfully signed up");
			login();
			
		} catch (UserAlreadyExistsException e) {
			System.out.println("User Already exists");
		}
	}

	private void login() {
		System.out.println("---Login Page---");
		String email;
		String password;
		System.out.print("Enter email : ");
		email = input.next();
		while (true) {
			System.out.print("Enter password : ");
			password = input.next();

			try {
				userManager.login(email, password);
				System.out.println("Successfully logged in");
				currentUser = userManager.getLoggedInUser();
				System.out.println("\n------ Welcome "+currentUser.getName()+"! ------");
				CalendarUI calendarUI = new CalendarUI(currentUser);
				calendarUI.execute();
				return;
				
			} catch (InvalidCredentials e) {
				System.out.println("Incorrect password!");
				continue;
			} catch (UserDoesNotExistsException e) {
				System.out.println("User doesn't exists! Signup to continue");
				return;
			}
		}

	}

}
