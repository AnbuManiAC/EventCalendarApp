package app;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeSet;

import model.Event;
import model.MyCalendar;

public class EventInfoPrinter {
	public void print(TreeSet<Event> events) {

		if (events.size() > 0) {
			for (Event event : events) {
				String startEventDate = getEventDate(event.getStartDateTime());
				String endEventDate = getEventDate(event.getEndDateTime());
				String eventStartTime = getEventTime(event.getStartDateTime());
				String eventEndTime = getEventTime(event.getEndDateTime());

				StringBuilder eventDetails = new StringBuilder();

				if (startEventDate.equals(endEventDate)) {
					eventDetails.append(startEventDate + eventStartTime + " - " + eventEndTime);
				} else {
					eventDetails.append(startEventDate + eventStartTime + " - " + endEventDate + eventEndTime);
				}

				System.out.println("Event Id : (" + event.getId() + ")  " + eventDetails.toString() + event.getName());
			}
		} else {
			System.out.println("No events scheduled");
		}
	}

	private String getEventDate(long millis) {
		StringBuilder eventDate = new StringBuilder();
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(millis);
		eventDate.append(MyCalendar.DAYS[cal.get(Calendar.DAY_OF_WEEK) - 1] + " ");
		eventDate.append(MyCalendar.MONTHS[cal.get(Calendar.MONTH)] + " ");
		eventDate.append(cal.get(Calendar.DAY_OF_MONTH) + " ");
		eventDate.append(cal.get(Calendar.YEAR) + " ");

		return eventDate.toString();
	}

	private String getEventTime(long millis) {
		StringBuilder eventTime = new StringBuilder();
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(millis);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		if (hour <= 9) {
			eventTime.append("0" + cal.get(Calendar.HOUR_OF_DAY) + ":");
		} else
			eventTime.append(cal.get(Calendar.HOUR_OF_DAY) + ":");
		if (minute <= 9)
			eventTime.append("0" + cal.get(Calendar.MINUTE) + " ");
		else
			eventTime.append(cal.get(Calendar.MINUTE) + " ");

		return eventTime.toString();
	}
}
