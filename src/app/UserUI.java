package app;

import java.util.Scanner;

import auth.Login;
import auth.Signup;
import database.UserAuthRepository;
import model.MyCalendar;
import model.User;
import service.TestInterface;

public class UserUI implements TestInterface {
	private static Scanner input = new Scanner(System.in);
	UserAuthRepository users = UserAuthRepository.getInstance();
	
	public void execute() {
		MyCalendar cal = new MyCalendar();
		cal.getMonthView(2022, 4);
		System.out.println("\nEvent Calendar");
		String choice;
		do {
			printUserMenu();
			System.out.print("Enter your choice : ");
			choice = input.next();
			if(choice.equals("1")) 
				signup();
			else if(choice.equals("2")) {
				login();
			}
			
		}while(!choice.equals("3"));
		if(choice == "3")	System.exit(0);
	}
	private void printUserMenu() {
		System.out.println("1. Signup");
		System.out.println("2. Login");
		System.out.println("3. Exit\n");
	}
	private void signup() {
		System.out.println("Signup Page");
		String name;
		String email;
		String password;
		System.out.print("Enter name : ");
		name = input.next();
		System.out.print("Enter email : ");
		email = input.next();
		if(users.isExistingUser(email)) {
			System.out.println("User already exists.");
			return;
		}
		System.out.print("Enter password : ");
		password = input.next();
		
		User newUser = new User(name, email);
		MyCalendar userCalendar = new MyCalendar();
		Signup newUserSignup = new Signup(newUser, password, userCalendar);
		System.out.println("Successfully signed up");
		login();
	}
	private void login() {
		System.out.println("Login Page");
		String email;
		String password;
		System.out.print("Enter email : ");
		email = input.next();
		if(!users.isExistingUser(email)) {
			System.out.println("User didn't exist! Signup first");
			return;
		}
		while(true) {
			System.out.print("Enter password : ");
			password = input.next();
			
			Login userLogin = new Login(email, password, this);
		
			if(userLogin.checkUser()) {
				System.out.println("Successfully logged in");
				CalendarUI calUI = new CalendarUI();
				calUI.execute();
				return;
			}
			else {
				System.out.println("Incorrect password. Enter corect password.");
				continue;
			}
		}

	}
	@Override
	public void msg(String msg) {
		System.out.println(msg);
		
	}
	
}
