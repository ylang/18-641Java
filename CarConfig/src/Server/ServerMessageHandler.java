package Server;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import Model.Automotive;
import Model.OptionSet;
import Util.Message;
import Util.ObjectIO;

public class ServerMessageHandler {

	/**
	 * Handles message sent from client
	 * @param message message sent from client
	 * @return a response message should be sent to client
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Message processMessageFromClient(Message message)
			throws IOException, ClassNotFoundException {
		Message reply = null;
		switch (message.getType()) {
		case (0):
			reply = new Message(message.getId(), 1);
			break;
		case (2):
			Automotive a = ServerMessageHandler.handleProperties(message
					.getPayload());
			if (a != null) {
				BuildCarModelOptions.addModel(a);
				reply = new Message(message.getId(), 3, "success");
				a.printInfo();
			} else {
				reply = new Message(message.getId(), 3, "fail");
			}
			break;
		case (10):
			// Requesting a model list
			Set<String> modelSet = BuildCarModelOptions.getModelSet();
			StringBuilder sb = new StringBuilder();
			for (String model : modelSet) {
				sb.append(model);
				sb.append(";;");
			}
			String payload = sb.toString();
			if (payload != null && payload.length() > 0) {
				payload = payload.substring(0, payload.length() - 2);
			}
			reply = new Message(message.getId(), 11, payload);
			break;
		case (12):
			// sending the model and requesting options
			String model = message.getPayload();
			Automotive targetModel = BuildCarModelOptions.getModel(model);
			if (targetModel == null) {
				throw new IllegalArgumentException(
						"model does not exist, internal error");
			}
			Iterator<String> optionSets = targetModel
					.getOptionSetNamesIterator();
			List<OptionSet> results = new ArrayList<OptionSet>();
			while (optionSets.hasNext()) {
				String setName = optionSets.next();
				OptionSet optionSet = targetModel.getOptionSet(setName);
				results.add(optionSet);
			}
			reply = new Message(message.getId(), 13, message.getPayload()
					+ ";;" + ObjectIO.toString((Serializable) results));
			break;
		case (14):
			@SuppressWarnings("unchecked")
			List<OptionSet> optionSetList = (List<OptionSet>) ObjectIO
					.fromString(message.getPayload().split(";;")[1]);
			Automotive selectedModel = BuildCarModelOptions.getModel(message
					.getPayload().split(";;")[0]);
			StringBuilder optionSB = new StringBuilder();
			for (OptionSet opSet : optionSetList) {
				selectedModel.setOptionChoice(opSet.getName(), opSet
						.getOptionChoice().getName());
				optionSB.append(opSet.getName()).append(":")
						.append(opSet.getOptionChoice().getName()).append(";;");
			}
			sb = new StringBuilder();
			sb.append(message.getPayload().split(";;")[0]).append(";;");
			sb.append(selectedModel.getTotalPrice());
			sb.append(";;").append(optionSB.toString());
			reply = new Message(message.getId(), 15, sb.toString());
			break;
		}
		return reply;

	}

	public static Automotive handleProperties(String properties) {
		Scanner sc = new Scanner(properties);
		sc.useDelimiter(";;");
		String make = null;
		String model = null;
		int basePrice = 0;
		make = sc.next().trim();
		model = sc.next().trim();
		basePrice = Integer.parseInt(sc.next().trim());
		Automotive car = new Automotive();
		car.setMake(make);
		car.setBasePrice(basePrice);
		car.setModel(model);

		// set up options
		String line = null;
		while (sc.hasNext()) {
			line = sc.next().trim();
			if ("end of setup".equals(line)) {
				break;
			}
			String args[] = line.split(",");
			String setName = args[1];
			int setCount = Integer.parseInt(args[2]);
			car.addOptionSet(setName, setCount);
			for (int i = 0; i < setCount; i++) {
				car.setOption(setName, i, args[i * 2 + 3],
						Integer.parseInt((args[i * 2 + 4])));
			}
		}

		// set up choices
		while (sc.hasNext()) {
			line = sc.next();
			String args[] = line.trim().split(":");
			car.setOptionChoice(args[0], args[1]);
		}
		sc.close();
		return car;
	}

}
