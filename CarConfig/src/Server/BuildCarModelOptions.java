package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import Model.Automotive;
import Util.Message;

/**
 * A server that can build a car model and provide all the options of models,
 * allows client to select a car and return the total price.
 * 
 * @author ylang
 * 
 */
public class BuildCarModelOptions {

	private static final int SERVER_PORT = 18641;
	private static HashMap<String, Automotive> modelMap = new HashMap<String, Automotive>();
	private Logger log = Logger.getLogger("SERVER");

	@SuppressWarnings("resource")
	public void startServer(boolean debug) {
		if (debug) {
			log.setLevel(Level.INFO);
		} else {
			log.setLevel(Level.WARNING);
		}
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
		} catch (IOException e) {
			System.err.println("Unable to create server socket on port: "
					+ SERVER_PORT);
			e.printStackTrace();
			return;
		}
		while (true) {
			Socket socket;
			try {
				socket = serverSocket.accept();
				log.info("get a new socket request");
			} catch (IOException e) {
				System.err.println("Accept failed.");
				continue;
			}

			new SocketHandler(socket, debug).start();
		}
	}

	/**
	 * Add a model to the server
	 * 
	 * @param a
	 *            target model
	 */
	public static void addModel(Automotive a) {
		modelMap.put(a.getMake() + " " + a.getModel(), a);
	}

	/**
	 * get all available models
	 * 
	 * @return a set of models
	 */
	public static Set<String> getModelSet() {
		return modelMap.keySet();
	}

	/**
	 * Return the automotive corresponding to the name.
	 * 
	 * @param model
	 *            make followed by name
	 * @return the automotive, or null if it is not existed.
	 */
	public static Automotive getModel(String model) {
		return modelMap.get(model);
	}
}

/**
 * Handle each client in an individual thread to keep thread safe.
 * @author ylang
 *
 */
class SocketHandler extends Thread {

	private Socket socket;
	private String messageId;
	private Logger log = Logger.getLogger("SERVER_HANDLER");

	public SocketHandler(Socket clientSocket, boolean debug) {
		this.socket = clientSocket;
		if (debug) {
			log.setLevel(Level.INFO);
		} else {
			log.setLevel(Level.SEVERE);
		}
	}

	@Override
	public void run() {

		PrintWriter out = null;
		BufferedReader in = null;
		try {
			out = new PrintWriter(this.socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					this.socket.getInputStream()));
		} catch (IOException e) {
			System.err.println("Cannot get input/output stream");
		}

		String rowMessage = null;

		try {
			while (true) {
				rowMessage = in.readLine();
				if (rowMessage != null) {
					Message message = new Message(rowMessage);
					log.info("SERVER: message received: = " + rowMessage);
					if (this.messageId == null
							&& (message.getType() == 0 || message.getType() == 10)) {
						this.messageId = message.getId();
						log.info("SERVER: set message id");
					}
					if (!message.checkMessageId(this.messageId)) {
						log.warning("SERVER: message id does not equal");
						
					}
					Message response = ServerMessageHandler
							.processMessageFromClient(message);
					out.println(response.toString());
					log.info("SERVER: message sent: = " + response.toString());
					if (response.getType() == 3) {
						break;
					}
				}
			}
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
