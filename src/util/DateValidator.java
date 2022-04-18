package util;

import java.text.ParseException;

import app.ErrorMessage;
import app.ShowErrorMessage;

public class DateValidator {

	private ShowErrorMessage showErrorMessage;

	public DateValidator(ShowErrorMessage showErrorMessage) {
		this.showErrorMessage = showErrorMessage;
	}

	public boolean isValidDate(String eventDate) {
		if (eventDate.length() == 10 && eventDate.charAt(2) == '-' && eventDate.charAt(5) == '-') {
			String[] d = eventDate.split("-");
			int year, month, date;
			try {
				date = Integer.parseInt(d[0]);
				month = Integer.parseInt(d[1]);
				year = Integer.parseInt(d[2]);
			} catch (Exception e) {
				showErrorMessage.errorMessage(ErrorMessage.INVALID_DATE_PARSING);
				return false;
			}
			if (year >= 1000 && year <= 9999) {
				if (month >= 1 && month <= 12) {
					if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10
							|| month == 12)
						if (date >= 1 && date <= 31)
							return true;
						else {
							showErrorMessage.errorMessage(ErrorMessage.INVALID_DATE31);
							return false;
						}
					else if (month == 4 || month == 6 || month == 9 || month == 11)
						if (date >= 1 && date <= 30)
							return true;
						else {
							showErrorMessage.errorMessage(ErrorMessage.INVALID_DATE30);
							return false;
						}
					else if (month == 2)
						if ((year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)))
							if (date >= 1 && date <= 29)
								return true;
							else {
								showErrorMessage.errorMessage(ErrorMessage.INVALID_LEAP_FEB_DATE);
								return false;
							}
						else {
							if (date >= 1 && date <= 28)
								return true;
							else {
								showErrorMessage.errorMessage(ErrorMessage.INVALID_NONLEAP_FEB_DATE);
								return false;
							}
						}

				} else {
					showErrorMessage.errorMessage(ErrorMessage.INVALID_MONTH);
					return false;
				}
			}
		}
		showErrorMessage.errorMessage(ErrorMessage.INVALID_DATE);
		return false;
	}

	public boolean isValidDateRange(String startDate, String endDate) {
		DateAndTimeFormatter dateAndTimeFormatter = new DateAndTimeFormatter();
		if (isValidDate(startDate) && isValidDate(endDate)) {
			Long start;
			Long end;
			try {
				start = dateAndTimeFormatter.dateToMillisecond(startDate);
				end = dateAndTimeFormatter.dateToMillisecond(endDate);
			} catch (ParseException e) {
				return false;
			}
			int dateComparator = start.compareTo(end);
			if (dateComparator != 1)
				return true;
			showErrorMessage.errorMessage(ErrorMessage.INVALID_END_DATE);
		}
		return false;
	}
}
