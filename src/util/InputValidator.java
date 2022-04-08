package util;
// *Separation of concerns*
public class InputValidator {
	public static boolean isValidDate(String date) {
		if(date.length() == 10 && date.charAt(2) == '-' && date.charAt(5) == '-') {
 			String[] d = date.split("-");
 			int yyyy,mm,dd;
 			try {
 				dd = Integer.parseInt(d[0]);
 				mm = Integer.parseInt(d[1]);
 				yyyy = Integer.parseInt(d[2]);
 			} catch (Exception e) {
 				return false;
 			}
 			if(yyyy>=1000 && yyyy<=9999) {
 		        if(mm>=1 && mm<=12) {
 		            if((dd>=1 && dd<=31) && (mm==1 || mm==3 || mm==5 || mm==7 || mm==8 || mm==10 || mm==12))
 		                return true;
 		            else if((dd>=1 && dd<=30) && (mm==4 || mm==6 || mm==9 || mm==11))
 		                return true;
 		            else if((dd>=1 && dd<=28) && (mm==2))
 		                return true;
 		            else if(dd==29 && mm==2 && (yyyy%400==0 ||(yyyy%4==0 && yyyy%100!=0)))
 		                return true;
 		        }
 			}
 		}
 		return false;
	}
	public static boolean isValidEndDate(String startDate, String endDate) {
		if(isValidDate(startDate) && isValidDate(endDate)) {
			Long start = InputFormatter.dateToLong(startDate);
			Long end = InputFormatter.dateToLong(endDate);
			int dateComparator = start.compareTo(end);
			if(dateComparator!=1)
				return true;
		}
		return false;
	}
	public static boolean isValidTime(String time) {
		if(time.length() == 5 && time.charAt(2) == ':') {
			String[] temp = time.split(":");
			int hh,mm;
			try {
				hh = Integer.parseInt(temp[0]);
				mm = Integer.parseInt(temp[1]);
 			} catch (Exception e) {
 				return false;
 			}
			if(hh>=0 && hh<=23 && mm>=0 && mm<=59) {
				return true;
			}
		
		}
		return false;
	}
	
	public static boolean isValidEndTime(String startDate, String endDate, String startTime, String endTime) {
		if(isValidDate(startDate) && isValidDate(endDate) && isValidEndDate(startDate, endDate) && isValidTime(endTime) && isValidTime(startTime)) {
			Long startDateTime = InputFormatter.dateTimeToLong(startDate, startTime);
			Long endDateTime = InputFormatter.dateTimeToLong(endDate, endTime);
			int dateTimeComparator = startDateTime.compareTo(endDateTime);
			if(dateTimeComparator == -1)
				return true;
		}
		return false;
	}
	
}
