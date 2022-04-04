package app;

import java.util.GregorianCalendar;
import java.util.Scanner;

import database.UserCalendarTable;
import database.UserTable;
import model.MyCalendar;

public class CalendarUI {
	private static Scanner input = new Scanner(System.in);
	static UserTable users = UserTable.getInstance();
	static UserCalendarTable userCal = UserCalendarTable.getInstance(); 
	public void printCalendarMenu() {
		System.out.println("1. View Event");
		System.out.println("2. Create Event");
		System.out.println("3. Delete Event");
		System.out.println("4. Goto date");
		System.out.println("5. Logout\n");
	}

	public void execute() {
		
		System.out.println("\nEvent Calendar");
		GregorianCalendar calendar = new GregorianCalendar();
		MyCalendar myCalendar = userCal.getUserCelendar(users.getCurrentUser());
		myCalendar.printMonth(calendar);
		String choice;
		while(true) {
			printCalendarMenu();
			System.out.println("Enter your choice : ");
			choice = input.next();
			switch(choice) {
				case "1":
					myCalendar.printAllEvents();
					break;
				case "2":
					String name, date, startTime, endTime;
					System.out.print("Enter Event name : ");
					name = input.next();
					System.out.print("Enter date(dd-mm-yyyy) : ");
					date = input.next();
					System.out.println("Enter starting time(hh:mm) : ");
					startTime = input.next();
					System.out.println("Enter ending time(hh:mm) : ");
					endTime = input.next();
					myCalendar.createEvent(name, date, startTime, endTime);
					
					break;
				case "3":
					String dateOfEventToDelete;
					System.out.println("Enter Event date(dd-mm-yyyy) : ");
					dateOfEventToDelete = input.next();
					System.out.println(myCalendar.getEventsOnThisDay(dateOfEventToDelete));
					System.out.println("Enter Event id to delete event : ");
					int eventIdtoDelete = input.nextInt();
					
					
					break;
				case "4":
					break;
				case "5":
					System.out.println("Successfully logged out");
					return;
				default:
					System.out.println("Please enter a valid option");
					break;
			}
		}
	}
	
}
