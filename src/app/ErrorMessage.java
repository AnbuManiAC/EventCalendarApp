package app;

public class ErrorMessage {
	public static final String INVALID_DATE = "Invalid date! Try like this 12-03-2022";
	public static final String INVALID_TIME = "Invaid time! Try like this 12:30";
	public static final String INVALID_MONTH = "Invalid month! Month value must be from 1 to 12";
	public static final String INVALID_DATE_PARSING = "Invalid date! Expected Integer for dd, mm and yyyy";
	public static final String INVALID_NONLEAP_FEB_DATE = "Invalid date for February! Must be from 1 to 28";
	public static final String INVALID_LEAP_FEB_DATE = "Invalid date for Leap year February Must be from 1 to 29";
	public static final String INVALID_TIME_PARSING = "Invalid time! Expected integer for hh:mm";
	public static final String INVALID_HOUR = "Invalid hour! hh must be from 0 to 23";
	public static final String INVALID_MINUTE = "Invalid minute! mm must be from 0 to 59";
	public static final String INVALID_DATE30 = "Invalid date for the specified month! Must be from 1 to 30";
	public static final String INVALID_DATE31 = "Invalid date for the specified month! Must be from 1 t 31";
	public static final String INVALID_END_TIME = "Invalid end time! End time must be after start date and time";
	public static final String INVALID_END_DATE = "Invaid end date! End date must not be before start date";
}