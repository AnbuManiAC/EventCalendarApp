package app;

import java.util.Scanner;
import java.util.TreeSet;

import database.EventQueryManager;
import model.Event;
import model.User;
import service.EventInfoPrinter;

public class EventViewNavigator {

	private final Scanner input;
	private EventQueryManager eventManager;
	private EventInfoPrinter eventInfoPrinter;
	
	public EventViewNavigator(Scanner input) {
		this.input = input;
		eventManager = new EventQueryManager();
		eventInfoPrinter = new EventInfoPrinter();
	}
	
	public void showEventViewMenu() {
		System.out.println("\n---Event View Menu---");
		System.out.println("1. Show Today Events");
		System.out.println("2. Show Past Events");
		System.out.println("3. Show Future Events");
		System.out.println("4. Show event for specific date");
		System.out.println("5. Show All Events");
		System.out.println("6. Main menu");
	}
	
	public void navigate(User currentUser) {
		String choice;
		while (true) {
			showEventViewMenu();
			System.out.println("Enter your choice : ");
			choice = input.next();
			switch (choice) {
			case "1":
				TreeSet<Event> todayEvents = eventManager.getTodayEvents(eventManager.getAllEvents(currentUser));
				eventInfoPrinter.print(todayEvents);
				break;
			case "2":
				TreeSet<Event> pastEvents = eventManager.getPastEvents(eventManager.getAllEvents(currentUser));
				eventInfoPrinter.print(pastEvents);
				break;
			case "3":
				TreeSet<Event> futureEvents = eventManager.getFutureEvents(eventManager.getAllEvents(currentUser));
				eventInfoPrinter.print(futureEvents);
				break;
			case "4":
				EventHandler eventHandler = new EventHandler(input);
				TreeSet<Event> events = eventHandler.getEventFromDate(currentUser);
				eventInfoPrinter.print(events);
				break;
			case "5":
				eventInfoPrinter.print(eventManager.getAllEvents(currentUser));
				break;
			case "6":
				return;
			default:
				System.out.println("Please enter a valid option");
				break;
			}
		}
	}
	
}
