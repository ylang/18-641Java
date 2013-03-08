package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import Util.Message;

/**
 * A client of selecting a car model
 * @author ylang
 *
 */
public class SelectCarOption {
	private String host;
	private int port;
	private Logger log = Logger.getLogger("CLIENT");

	public SelectCarOption(String host, int port, boolean debug) {
		this.host = host;
		this.port = port;
		if (debug) {
			log.setLevel(Level.INFO);
		} else {
			log.setLevel(Level.WARNING);
		}
	}

	/**
	 * start to select a model
	 */
	public void start() {
		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		Scanner sc = new Scanner(System.in);
		
		// connect to server
		try {
			socket = new Socket(this.host, this.port);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			log.info("socket created");
		} catch (UnknownHostException e) {
			System.err.println("Unkown host: " + host + ".");
		} catch (IOException e) {
			System.err.println("Cannot get I/O for the connection to: " + host
					+ " port: " + port);
		}
		String messageId = UUID.randomUUID().toString();
		out.println(Message.getMessage(messageId, 10));
		log.info("message 10 sent");
		try {
			while (true) {
				String rowMessage = in.readLine();
				log.info("message received: " + rowMessage);
				Message message = new Message(rowMessage);
				if (!messageId.equals(message.getId())) {
					log.warning("SELECTOR: message id does not equal");
					break;
				}
				try {
					Message response = ClientMessageHandler.processMessageFromServer(message, sc);
					if (message.getType() == 15) {
						break;
					}
					if (response == null) {
						log.warning("did not handle the response well");
						break;
					}
					out.println(response.toString());
				} catch (NoSuchElementException e) {
					System.err.println(e.getMessage());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			out.close();
			in.close();
			socket.close();
			System.out.println("Disconnect to server");
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} finally {
			sc.close();
		}
	}
}
