package app;

import java.util.Scanner;
import database.EventInvitationQueryManager;
import model.User;

public class AppUI {
	private static Scanner input;
	private User currentUser;
	EventHandler eventHandler;
	EventInvitationQueryManager eventInvitationManager;
	EventInviteNavigator eventInviteNavigator;
	CalendarNavigator calendarNavigator;
	EventViewNavigator eventViewNavigator;

	public AppUI(User currentUser) {
		input = new Scanner(System.in);

		eventHandler = new EventHandler(input);
		this.currentUser = currentUser;
		eventInvitationManager = new EventInvitationQueryManager();
		eventInviteNavigator = new EventInviteNavigator(input);
		calendarNavigator = new CalendarNavigator(input);
		eventViewNavigator = new EventViewNavigator(input);
	}

	void showCalendarMenu() {
		System.out.println("\n---Calendar Menu---");
		System.out.println("1. Show Calendar");
		System.out.println("2. Create Event");
		System.out.println("3. Delete Event");
		System.out.println("4. View Event");
		System.out.println("5. Invite others");
		System.out.println("6. View Invitation");
		System.out.println("7. Logout\n");
	}

	public void execute() {

		System.out.println("\nEvent Calendar");
		String choice;
		while (true) {
			showCalendarMenu();
			System.out.println("Enter your choice : ");
			choice = input.next();
			switch (choice) {
			case "1":
				calendarNavigator.showCalendar();
				break;
			case "2":
				eventHandler.createEvent(currentUser);
				break;
			case "3":
				eventHandler.deleteEvent(currentUser);
				break;
			case "4":
				eventViewNavigator.navigate(currentUser);
				break;
			case "5":
				eventInviteNavigator.inviteUserForEvent(currentUser);
				break;
			case "6":
			 	eventInviteNavigator.viewInvitationNavigator(currentUser);
				break;
			case "7":
				currentUser = null;
				System.out.println("Successfully logged out");
				return;
			default:
				System.out.println("Please enter a valid option");
				break;
			}
		}
	}

}
