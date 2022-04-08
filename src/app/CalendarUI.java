package app;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.TreeSet;

import database.CalendarEventRepository;
import database.UserAuthRepository;
import database.UserCalendarMappingRepository;
import model.Event;
import model.MyCalendar;
import model.TimeSlot;
import service.EventManager;
import util.InputFormatter;
import util.InputValidator;

public class CalendarUI {
	private static Scanner input = new Scanner(System.in);
	static UserAuthRepository users = UserAuthRepository.getInstance();
	static UserCalendarMappingRepository userCal = UserCalendarMappingRepository.getInstance();
	MyCalendar myCalendar = new MyCalendar();
	static final String[] monthNames = { "January", "February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December" };
	static final String[] dayNames = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

	public void printCalendarMenu() {
		System.out.println("\n---Calendar Menu---");
		System.out.println("1. View Event");
		System.out.println("2. Create Event");
		System.out.println("3. Delete Event");
		System.out.println("4. Goto date");
		System.out.println("5. Logout\n");
	}

	public void execute() {

		System.out.println("\nEvent Calendar");
		GregorianCalendar calendar = new GregorianCalendar();
		CalendarEventRepository calendarEvents = userCal.getUserCalendar(users.getCurrentUser());
		myCalendar.getCurrentMonth(calendar);
		String choice;
		while (true) {
			printCalendarMenu();
			System.out.println("Enter your choice : ");
			choice = input.next();
			switch (choice) {
			case "1":
				TreeSet<Event> allEvents = calendarEvents.getAllEvents();

				if (allEvents.size() <= 0) {
					System.out.println("No events scheduled");
					break;
				}
				printAllEvents(allEvents);
				break;
			case "2":
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
					if (!InputValidator.isValidDate(startDate)) {
						System.out.println("Invalid Date! Try like this 02-03-2012");
					} else
						break;

				}

				while (true) {
					System.out.println("Enter starting time(hh:mm in 24 hours format) : ");
					startTime = input.next().trim();
					if (!InputValidator.isValidTime(startTime)) {
						System.out.println("Invalid Time! Try like this 10:20");
					} else
						break;
				}

				while (true) {
					System.out.print("Enter ending date(dd-mm-yyyy) : ");
					endDate = input.next().trim();
					if (!InputValidator.isValidDate(endDate)) {
						System.out.println("Invalid Date! Try like this 02-03-2012");
					} else if (!InputValidator.isValidEndDate(startDate, endDate)) {
						System.out.println("End date must not be before start date!");
					} else
						break;

				}

				while (true) {
					System.out.println("Enter ending time(hh:mm in 24 hours format) : ");
					endTime = input.next().trim();
					if (!InputValidator.isValidTime(endTime)) {
						System.out.println("Invalid Time! Try like this 10:20");
					} else if (!InputValidator.isValidEndTime(startDate, endDate, startTime, endTime)) {
						System.out.println("Invalid Time! Try like this 10:20");
					} else
						break;
				}
				
				

				EventManager eventCreator = new EventManager();

				eventCreator.createEvent(name, InputFormatter.dateTimeToLong(startDate, startTime), InputFormatter.dateTimeToLong(endDate, endTime));

				break;
			case "3":
				Date dateOfEventToDelete;
				System.out.println("Enter Event date(dd-mm-yyyy) : ");
				dateOfEventToDelete = InputFormatter.toDate(input.next().trim());
				calendar.setTime(dateOfEventToDelete);
				TreeSet<Event> eventList = calendarEvents.getEventsOnThisDay(calendar);
				if (eventList == null) {
					System.out.println("No events scheduled");
					break;
				}
				printAllEvents(eventList);
				System.out.println("Enter Event id to delete event : ");
				int eventIdtoDelete = input.nextInt();
				EventManager eventDeleter = new EventManager();
				if (eventDeleter.deleteEvent(dateOfEventToDelete, eventIdtoDelete))
					System.out.println("Event deleted");
				else
					System.out.println("Event doesn't exists");
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

	public void printAllEvents(TreeSet<Event> events) {
		GregorianCalendar calendar = new GregorianCalendar();

		String nameOfDay = "", monthName = "", startT = "", endT = "", title = "";
		int year;
		int dayOfMonth;
		for (Event event : events) {
			calendar.setTime(null);
			year = calendar.get(Calendar.YEAR);
			nameOfDay = dayNames[calendar.get(Calendar.DAY_OF_WEEK) - 1];
			monthName = monthNames[calendar.get(Calendar.MONTH)];
			dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
//			TimeSlot eventTiming = event.getTimeSlot();
//			startT = eventTiming.getStartTime().toString();
//			endT = eventTiming.getEndTime().toString();
//			title = event.getName();
//			System.out.println(year + " " + nameOfDay + " " + monthName + " " + dayOfMonth + " " + startT + " - " + endT
//					+ " " + title);
		}

	}

}
