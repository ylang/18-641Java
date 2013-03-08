package Util;

/**
 * A class represents a message exchanged between server and client in this program
 * A message contains a message id, a type id and its payload.
 * @author ylang
 *
 */
public class Message {
	private String id, payload;
	private int type;

	public Message(String message) {
		String args[] = message.split(";;;");
		if (args.length != 3) {
			throw new IllegalArgumentException(
					"message is not valid, number of argument not equal to 3");
		}
		this.setup(args[0], args[1], args[2]);
	}

	public Message(String id, int type, String payload) {
		this.id = id.trim();
		this.type = type;
		this.payload = payload.trim();
	}

	public Message(String id, int type) {
		this.id = id.trim();
		this.type = type;
		this.payload = "null";
	}

	private void setup(String id, String type, String payload) {
		this.id = id.trim();
		this.type = Integer.parseInt(type.trim());
		this.payload = payload.trim();
	}

	public String getId() {
		return this.id;
	}

	public String getPayload() {
		return this.payload;
	}

	public int getType() {
		return this.type;
	}

	public boolean checkMessageId(String id) {
		return (this.id.equals(id));
	}
	
	@Override
	public String toString() {
		return (new StringBuilder().append(this.id).append(";;;")
				.append(this.type).append(";;;").append(this.payload))
				.toString();
	}

	/**
	 * A static method to return a message string with arguments
	 * 
	 * @param id
	 *            message_id
	 * @param type
	 *            type_id
	 * @param payload
	 *            payload of the message
	 * @return the message string
	 */
	public static String getMessage(String id, int type, String payload) {
		return (new StringBuilder().append(id).append(";;;").append(type)
				.append(";;;").append(payload)).toString();
	}

	/**
	 * A static method to return a message string with no payload. Here, payload
	 * set to null by default in the string.
	 * 
	 * @param id
	 *            message_id
	 * @param type
	 *            type_id
	 * @return the message string
	 */
	public static String getMessage(String id, int type) {
		return (new StringBuilder().append(id).append(";;;").append(type)
				.append(";;;").append("null")).toString();
	}

}
