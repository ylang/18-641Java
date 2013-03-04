package Client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarModelOptionIO {
	 
    private String host;
    private int port;
    private Logger log = Logger.getLogger("CLIENT");
 
    public CarModelOptionIO(String host, int port) {
        this.host = host;
        this.port = port;
        log.setLevel(Level.INFO);
    }
 
    public boolean start(String fileName) {
        Socket socket;
        PrintWriter out;
        BufferedReader in = null;
        String status = null;
        StringBuilder properties = new StringBuilder();
        try {
			BufferedReader fileReader = 
					new BufferedReader(new FileReader(fileName));
	        String line;
	        while ((line = fileReader.readLine()) != null) {
	        	properties.append(line);
	        	properties.append(";;");
	        }
	        fileReader.close();
	        log.info("read from file finished");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
            socket = new Socket(this.host, this.port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            log.info("socket created");
        } catch (UnknownHostException e) {
            System.err.println("Unkown host: " + host + ".");
            return false;
        } catch (IOException e) {
            System.err.println("Cannot get I/O for the connection to: "
                    + host + " port: " + port);
            return false;
        }
        String messageId = UUID.randomUUID().toString();
        out.println(messageId + ";;;0;;;null");
        log.info("message 0 sent");
        try {
			String response = in.readLine();
			log.info("message recieved: " + response);
			int typeId = ClientMessageHandler.processResponse(
					messageId, response);
			if (typeId == 1) {
				log.info("message 1 received");
				String propertiesString = properties.toString();
				if (properties != null && properties.length() > 0) {
					propertiesString = propertiesString.substring(0,
							propertiesString.length() - 2);
				}
				out.println(messageId + ";;;2;;;" + propertiesString);
				log.info("message 2 sent");
			}
			response = in.readLine();
			typeId = ClientMessageHandler.processResponse(
					messageId, response);
			if (typeId == 3) {
				log.info("meesage 3 received");
				status= response.split(";;;")[2].trim();
				log.info("status = " + status);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
            out.close();
            in.close();
            socket.close();
            System.out.println("Disconnect to server");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        if (status.equals("success")) {
        	return true;
        } else {
        	return false;
        }
    }
}
