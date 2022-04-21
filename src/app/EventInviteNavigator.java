package app;

import java.util.List;
import java.util.Scanner;

import database.EventInvitationQueryManager;
import exception.EventDoesNotExistsException;
import exception.InvalidInviteeException;
import exception.UserDoesNotExistsException;
import model.EventInvitation;
import model.User;
import service.EventInfoPrinter;
import util.EmailValidator;

public class EventInviteNavigator {
	
	private final Scanner input;
	private EventInvitationQueryManager eventInvitationManager;
	private EventInfoPrinter eventInfoPrinter;
	
	public EventInviteNavigator(Scanner input) {
		this.input = input;
		eventInvitationManager = new EventInvitationQueryManager();
		eventInfoPrinter = new EventInfoPrinter();
	}

	public void inviteUserForEvent(User currentUser) {
		String email;
		String id;
		int eventId;
		EmailValidator emailValidator = new EmailValidator();
		while (true) {
			System.out.print("Enter email to invite : ");
			email = input.next().trim().toLowerCase();
			if (emailValidator.isValidEmail(email))
				break;
		}
		while (true) {
			System.out.print("Enter id of event : ");
			id = input.next();
			try {
				eventId = Integer.parseInt(id);
				break;
			} catch (Exception e) {
				System.out.println("Invalid input!");
				continue;
			}
		}
		try {
			eventInvitationManager.invite(currentUser, email, eventId);
		} catch (UserDoesNotExistsException e) {
			System.out.println("User with this email does not exists");
			return;
		} catch (InvalidInviteeException e) {
			System.out.println(e.getMessage());
			return;
		}
		catch (EventDoesNotExistsException e) {
			System.out.println(e.getMessage());
			return;
		}
		System.out.println("User invited successfully");
	}

	public void viewInvitationNavigator(User currentUser) {
		String choice;
		while (true) {
			System.out.println("1. Sent\n2. Received");
			System.out.print("Enter your choice : ");
			choice = input.next();
			if (choice.equals("1")) {
				viewSentInvitation(currentUser);
				return;
			} else if (choice.equals("2")) {
				viewReceivedInvitation(currentUser);
				return;
			} else
				System.out.println("Invalid choice!");
		}
	}

	public void acceptOrRejectInvitation(User currentUser) {
		String eventInviteId;
		int eventInvitationId;
		String acceptanceChoice;
		boolean isAcceptanceSuccessfull = false;
		while(true) {
			System.out.println("Enter event invitation id to accept/reject invitation : ");
			try {
				eventInviteId = input.next();
				eventInvitationId = Integer.parseInt(eventInviteId);
				break;
			}
			catch(Exception e) {
				System.out.println("Invalid id!");
			}
		}
		while(true) {
			System.out.println("Enter 1 to accept or 2 to reject invitation : ");
			acceptanceChoice = input.next();
			if(acceptanceChoice.equals("1")) {
				isAcceptanceSuccessfull =  eventInvitationManager.acceptInvite(currentUser, eventInvitationId);
				if(isAcceptanceSuccessfull)
					System.out.println("Invitation accepted");
				break;
			}
			else if(acceptanceChoice.equals("2")) {
				isAcceptanceSuccessfull = eventInvitationManager.rejectInvite(currentUser, eventInvitationId);
				if(isAcceptanceSuccessfull)	
					System.out.println("Invitation rejected");
				break;
			}
			else
				System.out.println("Invalid choice");
		}
		if(!isAcceptanceSuccessfull)
			System.out.println("No invitation received with this invitation id or invitation expired");
		
	}
	
	public void viewReceivedInvitation(User currentUser) {
		List<EventInvitation> receivedInvitation = eventInvitationManager.getReceivedInvitation(currentUser);
		if (receivedInvitation.size()>0) {
			for(EventInvitation eventInvitation : receivedInvitation) {
				viewInvitation(currentUser, eventInvitation);
			}
			String choice;
			while(true) {
				System.out.print("Do you want to accept/reject any invitation (y/n) : ");
				choice = input.next().toLowerCase();
				if(choice.equals("y")) {
					acceptOrRejectInvitation(currentUser);
					break;	
				}
				else if(choice.equals("n"))
					break;
				else
					System.out.println("Invalid choice!");
			}
		}
		else
			System.out.println("No invitations received");

	}

	public void viewSentInvitation(User currentUser) {
		List<EventInvitation> sentInvitation = eventInvitationManager.getSentInvitation(currentUser);
		if (sentInvitation.size() > 0) {
			for (EventInvitation eventInvitation : sentInvitation) {
				viewInvitation(currentUser, eventInvitation);
			}
		}
		else
			System.out.println("No invitations sent");
	}
	public void viewInvitation(User currentUser, EventInvitation eventInvitation) {
		System.out.print("Event invited for :  ");
		eventInfoPrinter.print(eventInvitation.getEventInvitedFor());
		System.out.println("Inviter : " + eventInvitation.getInviter().getEmail() + "  Invitee : "
				+ eventInvitation.getInvitee().getEmail() +"  Invitation status : " + eventInvitation.getStatus());
		System.out.println("\n");
	}
	
}
