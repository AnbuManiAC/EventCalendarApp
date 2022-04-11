package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class InputFormatter {
	
	public static long dateTimeToMillisecond(String date, String time) {
		date = date + " " + time;
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date dateObj = null;
		try {
			dateObj = dateFormatter.parse(date);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return dateObj.getTime();
	}
	
	public static long dateToMillisecond(String date) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		Date dateObj = null;
		try {
			dateObj = dateFormatter.parse(date);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return dateObj.getTime();
	}
	
	public static String millisecondToDate(long millisecond) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy  HH:mm");
		Date date = new Date(millisecond);
		return dateFormatter.format(date);
	}
	
	public static Date toDate(String date) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		Date dateObj = null;
		try {
			dateObj = dateFormatter.parse(date);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return dateObj;
	}
	
	
	public static LocalTime toLocalTime(String time) {
		LocalTime timeObj = null;
		try {
			timeObj	= LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
		} catch (Exception e) {
			System.out.println("Invalid time!");
		}
		return timeObj;
	}
}
