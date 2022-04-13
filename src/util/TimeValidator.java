package util;

import app.ErrorMessage;
import app.ShowErrorMessage;

public class TimeValidator {
	private ShowErrorMessage showErrorMessage;

	public TimeValidator(ShowErrorMessage showErrorMessage) {
		this.showErrorMessage = showErrorMessage;
	}

	public boolean isValidTime(String time) {
		if (time.length() == 5 && time.charAt(2) == ':') {
			String[] temp = time.split(":");
			int hh, mm;
			try {
				hh = Integer.parseInt(temp[0]);
				mm = Integer.parseInt(temp[1]);
			} catch (Exception e) {
				showErrorMessage.errorMessage(ErrorMessage.INVALID_TIME_PARSING);
				return false;
			}
			if (hh >= 0 && hh <= 23) {
				if (mm >= 0 && mm <= 59) {
					return true;
				} else {
					showErrorMessage.errorMessage(ErrorMessage.INVALID_MINUTE);
					return false;
				}
			}
			showErrorMessage.errorMessage(ErrorMessage.INVALID_HOUR);
			return false;

		}
		showErrorMessage.errorMessage(ErrorMessage.INVALID_TIME);
		return false;
	}

	public boolean isValidDateTimeRange(String startDate, String endDate, String startTime, String endTime) {
		if (isValidTime(endTime) && isValidTime(startTime)) {
			Long startDateTime = DateAndTimeFormatter.dateTimeToMillisecond(startDate, startTime);
			Long endDateTime = DateAndTimeFormatter.dateTimeToMillisecond(endDate, endTime);
			int dateTimeComparator = startDateTime.compareTo(endDateTime);
			if (dateTimeComparator == -1)
				return true;
			showErrorMessage.errorMessage(ErrorMessage.INVALID_END_TIME);
		}
		return false;
	}
}
