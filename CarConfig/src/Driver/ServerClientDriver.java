package Driver;

import Client.CarModelOptionIO;
import Client.SelectCarOption;
import Server.BuildCarModelOptions;

public class ServerClientDriver {

	/**
	 * This is the driver for unit 4.
	 * @param args
	 */
	public static void main(String[] args) {
		final BuildCarModelOptions server = new BuildCarModelOptions();
		final boolean debug = false;
		
		// start the server
		new Thread(new Runnable(){

			@Override
			public void run() {
				server.startServer(debug);				
			}
			
		}).start();
		
		// create model 1
		final CarModelOptionIO client = 
				new CarModelOptionIO("localhost", 18641, debug);
		Thread setup = new Thread(new Runnable(){

			@Override
			public void run() {
				boolean status = client.start("input.txt");		
				System.out.println("CLIENT: created a model " + status);
			}
			
		});
		
		// create model 2
		final CarModelOptionIO client2 = 
				new CarModelOptionIO("localhost", 18641, debug);
		Thread setup2 = new Thread(new Runnable(){

			@Override
			public void run() {
				boolean status = client2.start("input2.txt");		
				System.out.println("CLIENT: created a model " + status);
			}
			
		});
		
		setup.start();
		setup2.start();
		try {
			setup.join();
			setup2.join();
			
			// start to choose a model
			new Thread(new Runnable(){

				@Override
				public void run() {
					SelectCarOption selector = new SelectCarOption("localhost", 18641, debug);
					selector.start();
				}
				
			}).start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
