package Client;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import Model.Option;
import Model.OptionSet;
import Util.Message;
import Util.ObjectIO;

public class ClientMessageHandler {

	/**
	 * Return the type_id of a response message from server
	 * @param messageId message id that suppose to be 
	 * @param response response message from server 
	 * @return type_id
	 */
	public static int processResponse(String messageId, String response) {
		String args[] = response.split(";;;");
		if (args.length != 3) {
			throw new IllegalArgumentException("invalid message");
		}
		if (messageId.equals(args[0])) {
			return Integer.parseInt(args[1]);
		}
		return -1;
	}

	/**
	 * Process a message during select a car model
	 * @param message message sent from server
	 * @param sc scanner of console
	 * @return a replying message should be sent to server
	 * @throws NoSuchElementException 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Message processMessageFromServer(Message message, Scanner sc)
			throws NoSuchElementException, ClassNotFoundException, IOException {

		Message reply = null;
		switch (message.getType()) {
		case (11):
			// select model
			String args[] = message.getPayload().split(";;");
			if (args == null || args.length == 0) {
				System.out.println("No available models on server");
				sc.close();
				throw new NoSuchElementException("no available models");
			} else {
				System.out.println("Available models are listed:");
				for (int i = 0; i < args.length; i++) {
					System.out.printf("%d\t%s\n", i, args[i]);
				}
				System.out.println("Please type in the number "
						+ "of the model you would like to select");
				System.out.print(">");
				int index;
				while (true) {
					try {
						String input = sc.nextLine();
						index = Integer.parseInt(input);
						if (index < args.length) {
							break;
						} else {
							throw new IndexOutOfBoundsException();
						}
					} catch (Exception e) {
						System.out
								.println("The number you typed is invalid. Please re-enter the choice");
						System.out.print(">");
					}
				}
				System.out.println("Car model selected: " + args[index]);
				System.out.println("Your request is sending to the server");
				reply = new Message(message.getId(), 12, args[index]);
			}
			break;
		case (13):
			// select properties
			@SuppressWarnings("unchecked")
			List<OptionSet> optionSetList = (List<OptionSet>) ObjectIO
					.fromString(message.getPayload().split(";;")[1]);
			for (OptionSet optionSet : optionSetList) {
				System.out.println("Please select the option of "
						+ optionSet.getName());
				List<Option> optionList = optionSet.getOptions();
				for (int i = 0; i < optionList.size(); i++) {
					System.out.printf("%d\t%s\t$%d\n", i, optionList.get(i)
							.getName(), optionList.get(i).getPrice());
				}
				int index;
				while (true) {
					try {
						System.out.print(">");
						String input = sc.nextLine();
						System.out.println("sc = " + input);
						index = Integer.parseInt(input);
						if (index < optionList.size()) {
							break;
						} else {
							throw new IndexOutOfBoundsException();
						}
					} catch (Exception e) {
						System.out
								.println("The number you typed is invalid. Please re-enter the choice");
						System.out.print(">");
					}
				}
				optionSet.setOptionChoice(optionList.get(index).getName());
			}
			String payload = ObjectIO.toString((Serializable) optionSetList);
			reply = new Message(message.getId(), 14, message.getPayload()
					.split(";;")[0] + ";;" + payload);
			break;
		case (15):
			// selecting finished
			String result = message.getPayload();
			args = result.split(";;");
			String modelName = args[0];
			int totalPrice = Integer.parseInt(args[1].trim());
			System.out.println("Your car selecting process finished.");
			System.out.printf("The model you chose:\t%s\n", modelName);
			System.out.printf("The total price is:\t$%d\n", totalPrice);
			System.out.println("The list of chosen option:");
			for (int i = 2; i < args.length; i++) {
				if (args[i] == null || args[i].length() == 0) {
					continue;
				}
				String option[] = args[i].split(":");
				System.out.printf("%s:\t%s\n", option[0], option[1]);
			}
			break;
		default:
			System.out.println("unkown type: " + message.getType());
			break;
		}
		return reply;
	}
}
