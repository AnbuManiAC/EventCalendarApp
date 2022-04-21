package app;

import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.TreeSet;
import database.EventQueryManager;
import exception.InvalidEventException;
import model.Event;
import model.RecurrenceType;
import model.User;
import service.EventInfoPrinter;
import service.RecurringEventManager;
import util.DateAndTimeFormatter;
import util.DateValidator;
import util.TimeValidator;

public class EventHandler implements ShowErrorMessage {
	
	private final Scanner input;
	private DateValidator dateValidator;
	private TimeValidator timeValidator;
	private EventQueryManager eventManager;
	private EventInfoPrinter eventInfoPrinter;
	private GregorianCalendar calendar;

	
	public EventHandler(Scanner input) {
		this.input = input;
		dateValidator = new DateValidator(this);
		timeValidator = new TimeValidator(this);
		eventManager = new EventQueryManager();
		eventInfoPrinter = new EventInfoPrinter();
		calendar = new GregorianCalendar();
	}
	
	void createEvent(User currentUser) {
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
			System.out.print("Enter starting time(hh:mm in 24 hours format) : ");
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
			System.out.print("Enter ending time(hh:mm in 24 hours format) : ");
			endTime = input.next().trim();
			if (timeValidator.isValidTime(endTime))
				if (timeValidator.isValidDateTimeRange(startDate, endDate, startTime, endTime))
					break;
		}

		try {
			Event event;
			DateAndTimeFormatter dateAndTimeFormatter = new DateAndTimeFormatter();
			try {
				event = eventManager.createEvent(currentUser, name,
						dateAndTimeFormatter.dateTimeToMillisecond(startDate, startTime),
						dateAndTimeFormatter.dateTimeToMillisecond(endDate, endTime));
			} catch (ParseException e) {
				System.out.println("Invalid date or time");
				return;
			}
			System.out.println("Event created successfully");
			while (true) {
				System.out.print("Do you want to recur this event(y/n) : ");
				String recurChoice = input.next().trim();
				if (recurChoice.equalsIgnoreCase("y")) {
					recurEvent(currentUser, event);
					break;
				} else if (recurChoice.equalsIgnoreCase("n"))
					break;
				else
					System.out.println("Invalid choice!");
			}

		} catch (InvalidEventException e) {
			System.out.println(e.getClass().getName() + " : " + e.getMessage());
		}

	}

	void recurEvent(User currentUser, Event event) {
		int recurCount;
		RecurrenceType recurType;
		System.out.print("Enter recurring count : ");
		while (true) {
			recurCount = input.nextInt();
			if (recurCount > 0) {
				System.out.println("1. " + RecurrenceType.DAILY + "\n" + "2. " + RecurrenceType.WEEKLY);
				System.out.print("Enter recurring type : ");
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
		RecurringEventManager recurringEventManager = new RecurringEventManager(currentUser, event, recurCount,
				recurType);
		try {
			recurringEventManager.createEvent();
			System.out.println("Recurring event created successfully");
		} catch (InvalidEventException e) {
			System.out.println("Invalid date time range");
		}
	}

	void deleteEvent(User currentUser) {
		TreeSet<Event> eventList = getEventFromDate(currentUser);
		if (eventList != null) {
			eventInfoPrinter.print(eventList);
			System.out.print("Enter Event id to delete event : ");
			int eventIdtoDelete = input.nextInt();
			if (eventManager.deleteEvent(currentUser, eventIdtoDelete)) {
				System.out.println("Event deleted");
			}
			else
				System.out.println("Event doesn't exists");
		} else {
			System.out.println("No events scheduled");
		}
	}

	void showEvent(TreeSet<Event> events) {
		eventInfoPrinter.print(events);
	}

	TreeSet<Event> getEventFromDate(User currentUser) {
		String date;
		DateAndTimeFormatter dateAndTimeFormatter = new DateAndTimeFormatter();
		while (true) {
			System.out.print("Enter event date(dd-mm-yyyy) : ");
			date = input.next().trim();
			if (dateValidator.isValidDate(date))
				break;
		}
		Date dateOfEvent;
		try {
			dateOfEvent = dateAndTimeFormatter.toDate(date);
			calendar.setTime(dateOfEvent);
			TreeSet<Event> eventList = eventManager.getAllEvents(currentUser);
			eventList = eventManager.filterEventsOnThisDay(eventList, dateOfEvent.getTime());
			if (eventList.size() > 0)
				return eventList;
		} catch (ParseException e) {
			System.out.println("Invalid date");
		}

		return null;
	}
	
	@Override
	public void errorMessage(String errorMessage) {
		System.out.println(errorMessage);
	}
}
