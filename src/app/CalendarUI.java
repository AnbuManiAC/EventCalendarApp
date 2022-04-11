package app;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.TreeSet;

import database.CalendarEventRepository;
import database.UserAuthRepository;
import model.Event;
import model.MyCalendar;
import service.EventManager;
import util.DateValidator;
import util.InputFormatter;
import util.TimeValidator;

public class CalendarUI implements ShowErrorMessage {
	private static Scanner input = new Scanner(System.in);
	private DateValidator dateValidator = new DateValidator(this);
	private TimeValidator timeValidator = new TimeValidator(this);
	private EventInfoPrinter eventInfoPrinter = new EventInfoPrinter();
	private CalendarEventRepository calendarEvents = CalendarEventRepository.getInstance();
	private GregorianCalendar calendar = new GregorianCalendar();
	private UserAuthRepository users = UserAuthRepository.getInstance();
	private MyCalendar myCalendar = new MyCalendar();

	public void printCalendarMenu() {
		System.out.println("\n---Calendar Menu---");
		System.out.println("1. Show Calendar");
		System.out.println("2. Create Event");
		System.out.println("3. Delete Event");
		System.out.println("4. List Today Events");
		System.out.println("5. List Past Events");
		System.out.println("6. List Future Events");
		System.out.println("7. Goto Date");
		System.out.println("8. Show All Events");
		System.out.println("9. Logout\n");
	}

	public void execute() {

		System.out.println("\nEvent Calendar");
		String choice;
		while (true) {
			printCalendarMenu();
			System.out.println("Enter your choice : ");
			choice = input.next();
			switch (choice) {
			case "1":
				showCalendar();
				break;
			case "2":
				createEvent();
				break;
			case "3":
				deleteEvent();
				break;
			case "4":
				break;
			case "5":
				break;
			case "6":
				break;
			case "7":
				getEventFromDate();
				break;
			case "8":
				viewEvent();
				break;
			case "9":
				users.setCurrentUser(null);
				System.out.println("Successfully logged out");
				return;
			default:
				System.out.println("Please enter a valid option");
				break;
			}
		}
	}

	private void showCalendar() {
		
		GregorianCalendar calendar = new GregorianCalendar();
		myCalendar.getCurrentMonth(calendar);
		String choice;
		
		while(true) {
			System.out.println("1. Previous\n2. Next\n3. Main menu");
			choice = input.next().trim();
			if(choice.equals("1"))
				myCalendar.getPreviousMonth(calendar);
			else if(choice.equals("2"))
				myCalendar.getNextMonth(calendar);
			else if(choice.equals("3"))
				return;
			else
				System.out.println("Invalid option!");
		}
	}
	
	private void viewEvent() {
		TreeSet<Event> allEvents = calendarEvents.getAllEvents();

		if (allEvents != null && allEvents.size() > 0) {
			eventInfoPrinter.print(allEvents);
		} else {
			System.out.println("No events scheduled");
		}
		allEvents.clear();
	}

	private void createEvent() {
		String name;
		String startDate;
		String startTime;
		String endDate;
		String endTime;

		System.out.print("Enter Event name : ");
		name = input.next();
		while (true) {
			System.out.print("Enter starting date(dd-mm-yyyy) : ");
			startDate = input.next().trim();
			if (dateValidator.isValidDate(startDate))
				break;
		}

		while (true) {
			System.out.println("Enter starting time(hh:mm in 24 hours format) : ");
			startTime = input.next().trim();
			if (timeValidator.isValidTime(startTime))
				break;
		}

		while (true) {
			System.out.print("Enter ending date(dd-mm-yyyy) : ");
			endDate = input.next().trim();
			if (dateValidator.isValidDate(endDate))
				if (dateValidator.isValidEndDate(startDate, endDate))
					break;
		}

		while (true) {
			System.out.println("Enter ending time(hh:mm in 24 hours format) : ");
			endTime = input.next().trim();
			if (timeValidator.isValidTime(endTime))
				if (timeValidator.isValidEndTime(startDate, endDate, startTime, endTime))
					break;
		}

		EventManager eventCreator = new EventManager();

		eventCreator.createEvent(name, InputFormatter.dateTimeToMillisecond(startDate, startTime),
				InputFormatter.dateTimeToMillisecond(endDate, endTime));
	}

	private void deleteEvent() {
		TreeSet<Event> eventList = getEventFromDate();
		if (eventList != null) {
			eventInfoPrinter.print(eventList);
			System.out.println("Enter Event id to delete event : ");
			int eventIdtoDelete = input.nextInt();
			EventManager eventDeleter = new EventManager();
			if (eventDeleter.deleteEvent(eventIdtoDelete))
				System.out.println("Event deleted");
			else
				System.out.println("Event doesn't exists");
		} else {
			System.out.println("No events scheduled");
		}
	}

	private TreeSet<Event> getEventFromDate() {
		String date;
		while (true) {
			System.out.print("Enter event date(dd-mm-yyyy) : ");
			date = input.next().trim();
			if (dateValidator.isValidDate(date))
				break;
		}
		Date dateOfEvent = InputFormatter.toDate(date);
		calendar.setTime(dateOfEvent);
		TreeSet<Event> eventList = calendarEvents.getEventsOnThisDay(dateOfEvent.getTime());
		if (eventList.size() > 0)
			return eventList;
		return null;
	}

	@Override
	public void errorMessage(String errorMessage) {
		System.out.println(errorMessage);
	}

}
