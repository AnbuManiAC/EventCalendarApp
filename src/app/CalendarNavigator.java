package app;

import java.util.GregorianCalendar;
import java.util.Scanner;

import model.MyCalendar;

public class CalendarNavigator {

	private final Scanner input;
	private MyCalendar myCalendar;
	
	public CalendarNavigator(Scanner input) {
		this.input = input;
		myCalendar = new MyCalendar();
	}
	
	void showCalendar() {

		GregorianCalendar calendar = new GregorianCalendar();
		System.out.println(myCalendar.currentMonth(calendar));
		String choice;

		while (true) {
			System.out.println("1. Previous\n2. Next\n3. Change Calendar Size\n4. Main menu");
			choice = input.next().trim();
			if (choice.equals("1"))
				System.out.println(myCalendar.previousMonth(calendar));
			else if (choice.equals("2"))
				System.out.println(myCalendar.nextMonth(calendar));
			else if (choice.equals("3")) {
				customizeCalendar();
				System.out.println(myCalendar.currentMonth(calendar));
			} else if (choice.equals("4"))
				return;
			else
				System.out.println("Invalid option!");
		}
	}

	void customizeCalendar() {
		String sizePreference;
		System.out.println("\n1. Small\n2. Medium\n3. Large");
		System.out.print("Enter your size preference : ");
		String confirmationMessage = "Size preference saved";
		sizePreference = input.next();
		do {
			if (sizePreference.equals("1"))
				myCalendar.setCalendarSizePreference(MyCalendar.CalendarSize.SMALL);
			else if (sizePreference.equals("2"))
				myCalendar.setCalendarSizePreference(MyCalendar.CalendarSize.MEDIUM);
			else if (sizePreference.equals("3"))
				myCalendar.setCalendarSizePreference(MyCalendar.CalendarSize.LARGE);
			else
				System.out.println("Invalid choice!");
		} while (!sizePreference.equals("1") && !sizePreference.equals("2") && !sizePreference.equals("3"));
		System.out.println(confirmationMessage);
	}
	
}
