package app;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.TreeSet;
import database.EventManager;
import exception.InvalidEventException;
import model.Event;
import model.MyCalendar;
import model.RecurrenceType;
import model.User;
import service.RecurringEventGenerator;
import util.DateAndTimeFormatter;
import util.DateValidator;
import util.TimeValidator;

public class CalendarUI implements ShowErrorMessage {
	private static Scanner input = new Scanner(System.in);
	private DateValidator dateValidator = new DateValidator(this);
	private TimeValidator timeValidator = new TimeValidator(this);
	private EventInfoPrinter eventInfoPrinter = new EventInfoPrinter();
	private GregorianCalendar calendar = new GregorianCalendar();
	private MyCalendar myCalendar = new MyCalendar();
	private User currentUser;
	private EventManager eventManager;

	public CalendarUI(User currentUser) {
		this.currentUser = currentUser;
		eventManager = new EventManager();
	}

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
				TreeSet<Event> todayEvents = eventManager.getTodayEvents(eventManager.getAllEvents(currentUser));
				showEvent(todayEvents);
				break;
			case "5":
				TreeSet<Event> pastEvents = eventManager.getPastEvents(eventManager.getAllEvents(currentUser));
				showEvent(pastEvents);
				break;
			case "6":
				TreeSet<Event> futureEvents = eventManager.getFutureEvents(eventManager.getAllEvents(currentUser));
				showEvent(futureEvents);
				break;
			case "7":
				TreeSet<Event> events = getEventFromDate();
				showEvent(events);
				break;
			case "8":
				viewEvent();
				break;
			case "9":
				currentUser = null;
				
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
		System.out.println(myCalendar.currentMonth(calendar));
		String choice;

		while (true) {
			System.out.println("1. Previous\n2. Next\n3. Main menu");
			choice = input.next().trim();
			if (choice.equals("1"))
				System.out.println(myCalendar.previousMonth(calendar));
			else if (choice.equals("2"))
				System.out.println(myCalendar.nextMonth(calendar));
			else if (choice.equals("3"))
				return;
			else
				System.out.println("Invalid option!");
		}
	}

	private void viewEvent() {
		TreeSet<Event> allEvents = eventManager.getAllEvents(currentUser);

		if (allEvents != null && allEvents.size() > 0) {
			eventInfoPrinter.print(allEvents);
		} else {
			System.out.println("No events scheduled");
		}
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
				if (dateValidator.isValidDateRange(startDate, endDate))
					break;
		}

		while (true) {
			System.out.println("Enter ending time(hh:mm in 24 hours format) : ");
			endTime = input.next().trim();
			if (timeValidator.isValidTime(endTime))
				if (timeValidator.isValidDateTimeRange(startDate, endDate, startTime, endTime))
					break;
		}

		try {
			eventManager.createEvent(currentUser, name,
					DateAndTimeFormatter.dateTimeToMillisecond(startDate, startTime),
					DateAndTimeFormatter.dateTimeToMillisecond(endDate, endTime));
//			while (true) {
//				System.out.print("Do you want to recur this event(y/n) : ");
//				String recurChoice = input.next().trim();
//				if (recurChoice.equalsIgnoreCase("y")) {
//					recurEvent(event);
//					break;
//				} else if (recurChoice.equalsIgnoreCase("n"))
//					break;
//				else
//					System.out.println("Invalid choice!");
//			}

		} catch (InvalidEventException e) {
			System.out.println(e.getClass().getName() + " : " + e.getMessage());
		}

	}

	private void recurEvent(Event event) {
		int recurCount;
		RecurrenceType recurType;
		System.out.println("Enter recurring count : ");
		while (true) {
			recurCount = input.nextInt();
			if (recurCount > 0) {
				System.out.println("1. " + RecurrenceType.DAILY + "\n" + "2. " + RecurrenceType.WEEKLY);
				System.out.println("Enter recurring type : ");
				String recurTypeEntered = input.next().trim();
				if (recurTypeEntered.equals("1")) {
					recurType = RecurrenceType.DAILY;
					break;
				} else if (recurTypeEntered.equals("2")) {
					recurType = RecurrenceType.WEEKLY;
					break;
				} else {
					System.out.println("Invalid type!");
				}
			}

		}
		RecurringEventGenerator recurringEventGenerator = new RecurringEventGenerator(event, recurCount, recurType);
		try {
			TreeSet<Event> recurringEvents = recurringEventGenerator.getAllEvents();
			for (Event recuringEvent : recurringEvents) {
//				calendarEventRepository.insertRecord(recuringEvent);
			}
		} catch (InvalidEventException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void deleteEvent() {
		TreeSet<Event> eventList = getEventFromDate();
		if (eventList != null) {
			eventInfoPrinter.print(eventList);
			System.out.println("Enter Event id to delete event : ");
			int eventIdtoDelete = input.nextInt();
			if (eventManager.deleteEvent(currentUser, eventIdtoDelete))
				System.out.println("Event deleted");
			else
				System.out.println("Event doesn't exists");
		} else {
			System.out.println("No events scheduled");
		}
	}

	private void showEvent(TreeSet<Event> events) {

		if (events != null && events.size() > 0) {
			eventInfoPrinter.print(events);
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
		Date dateOfEvent = DateAndTimeFormatter.toDate(date);
		calendar.setTime(dateOfEvent);
		TreeSet<Event> eventList = eventManager.getAllEvents(currentUser);
		eventList = eventManager.filterEventsOnThisDay(eventList, dateOfEvent.getTime());
		if (eventList.size() > 0)
			return eventList;
		return null;
	}

	@Override
	public void errorMessage(String errorMessage) {
		System.out.println(errorMessage);
	}

}
