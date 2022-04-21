package app;

import java.util.Scanner;
import database.UserQueryManager;
import exception.InvalidCredentials;
import exception.NoUserLoggedIn;
import exception.UserAlreadyExistsException;
import exception.UserDoesNotExistsException;
import model.User;
import util.EmailValidator;
import util.PasswordValidator;

public class UserUI {

	UserQueryManager userManager;
	User currentUser;

	public UserUI() {
		userManager = new UserQueryManager();
	}

	private Scanner input = new Scanner(System.in);

	public void execute() {
		System.out.println("\nEvent Calendar");
		String choice;
		do {
			showUserMenu();
			System.out.print("Enter your choice : ");
			choice = input.nextLine().trim();
			
			if (choice.equals("1"))
				signup();
			else if (choice.equals("2")) {
				login();
			} else if (choice.equals("3")) {
				break;
			} else {
				System.out.println("Invalid option!\n");
			}
		} while (true);
		if (choice == "3")
			System.exit(0);
	}

	private void showUserMenu() {
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
			name = input.nextLine().trim();
			if (name.length() > 0)
				break;
			else
				System.out.println("Empty name!");

		}
		while (true) {
			System.out.print("Enter email : ");
			email = input.nextLine().trim().toLowerCase();
			EmailValidator emailValidator = new EmailValidator();
			if (emailValidator.isValidEmail(email))
				break;
			else
				System.out.println("Invalid email!");
		}
		if (userManager.isExistingUser(email)) {
			System.out.println("User already exists! Login to continue");
			return;
		}
		while (true) {
			System.out.println(
					"[ Password must be between 8 to 15 characters long\n  Password must contains one uppercase, lowercase character and one number\n  Password must contains one special characters among @#$%_ ]");
			System.out.print("Enter password : ");
			password = input.nextLine();
			PasswordValidator passwordValidator = new PasswordValidator();
			if (passwordValidator.isValidPassword(password))
				break;
			System.out.println("Password doesn't match the required conditions");
		}

		try {
			userManager.signup(name, email, password);
			System.out.println("Successfully signed up");
			login();

		} catch (UserAlreadyExistsException e) {
			System.out.println(e.getClass().getName() + " : " + e.getMessage());
		}
	}

	private void login() {
		System.out.println("---Login Page---");
		String email;
		String password;
		System.out.print("Enter email : ");
		email = input.nextLine().toLowerCase();
		while (true) {
			System.out.print("Enter password : ");
			password = input.nextLine();

			try {
				userManager.login(email, password);
				System.out.println("Successfully logged in");
				try {
					currentUser = userManager.getLoggedInUser();
				} catch (NoUserLoggedIn e) {
					System.out.println(e.getClass().getName() + " : " + e.getMessage());
				}
				System.out.println("\n------ Welcome " + currentUser.getName() + "! ------");
				AppUI appUI = new AppUI(currentUser);
				appUI.execute();
				logout();
				return;

			} catch (InvalidCredentials e) {
				System.out.println(e.getClass().getName() + " : " + e.getMessage());
				continue;
			} catch (UserDoesNotExistsException e) {
				System.out.println(e.getClass().getName() + " : " + e.getMessage());
				return;
			}
		}

	}

	private void logout() {
		this.currentUser = null;
		userManager.logout();
	}

}
