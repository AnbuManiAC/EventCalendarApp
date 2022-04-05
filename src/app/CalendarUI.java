package app;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.TreeSet;

import database.CalendarEventRepository;
import database.UserAuthRepository;
import database.UserCalendarMappingRepository;
import model.Event;
import model.MyCalendar;
import model.TimeSlot;
import service.EventScheduler;
import service.EventUnscheduler;
import utility.DateFormatter;

public class CalendarUI {
	private static Scanner input = new Scanner(System.in);
	static UserAuthRepository users = UserAuthRepository.getInstance();
	static UserCalendarMappingRepository userCal = UserCalendarMappingRepository.getInstance(); 
	MyCalendar myCalendar = new MyCalendar();
	static final String[] monthNames = {"January","February","March","April","May","June","July","August",
			"September","October","November","December"};
	static final String[] dayNames = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

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
		CalendarEventRepository calendarEvents = userCal.getUserCelendar(users.getCurrentUser());
		myCalendar.getCurrentMonth(calendar);
		String choice;
		while(true) {
			printCalendarMenu();
			System.out.println("Enter your choice : ");
			choice = input.next();
			switch(choice) {
				case "1":
					myCalendar.getCurrentMonth(calendar);
					TreeSet<Event> allEvents = calendarEvents.getAllEvents();
					
					if(allEvents == null) {
						System.out.println("No events scheduled");
						break;
					}
					printAllEvents(allEvents);
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
					EventScheduler eventScheduler = new EventScheduler();
					eventScheduler.createEvent(name, date, startTime, endTime);
					
					break;
				case "3":
					String dateOfEventToDelete;
					System.out.println("Enter Event date(dd-mm-yyyy) : ");
					dateOfEventToDelete = input.next();
					calendar.setTime(DateFormatter.StringtoDate(dateOfEventToDelete));
					TreeSet<Event> eventList = calendarEvents.getEventsOnThisDay(calendar);
					if(eventList==null) {
						System.out.println("No events scheduled");
						break;
					}
					printAllEvents(eventList);
					System.out.println("Enter Event id to delete event : ");
					int eventIdtoDelete = input.nextInt();
					EventUnscheduler eventUnscheduler = new EventUnscheduler();
					if(eventUnscheduler.deleteSelectedEvent(dateOfEventToDelete, eventIdtoDelete))
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
	
	public void printAllEvents(TreeSet<Event> events){
		GregorianCalendar calendar = new GregorianCalendar();

		String nameOfDay="", monthName="", startT="",endT="",title="";
		int year;
		int dayOfMonth;
		for (Event event : events){
			calendar.setTime(event.getDate());
			year=calendar.get(Calendar.YEAR);
			nameOfDay=dayNames[calendar.get(MyCalendar.DAY_OF_WEEK)-1];
			monthName=monthNames[calendar.get(MyCalendar.MONTH)];
			dayOfMonth=calendar.get(MyCalendar.DAY_OF_MONTH);
			TimeSlot eventTiming = event.getTimeSlot();
			startT = eventTiming.getStartTime().toString();
			endT = eventTiming.getEndTime().toString(); 
			title = event.getName();
			System.out.println(year+" "+nameOfDay+" "+monthName+" "+dayOfMonth+" "+startT+" - "+endT+" "+title);
		}
	
	}
	
}
