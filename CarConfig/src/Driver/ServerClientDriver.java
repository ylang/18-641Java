package Driver;

import Client.CarModelOptionIO;
import Client.SelectCarOption;
import Server.BuildCarModelOptions;

public class ServerClientDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final BuildCarModelOptions server = new BuildCarModelOptions();
		new Thread(new Runnable(){

			@Override
			public void run() {
				server.startServer();				
			}
			
		}).start();
		
		final CarModelOptionIO client = 
				new CarModelOptionIO("localhost", 18641);
		Thread setup = new Thread(new Runnable(){

			@Override
			public void run() {
				boolean status = client.start("input.txt");		
				System.out.println("CLIENT: created a model " + status);
			}
			
		});
		setup.start();
		try {
			setup.join();
			new Thread(new Runnable(){

				@Override
				public void run() {
					SelectCarOption selector = new SelectCarOption("localhost", 18641);
					selector.start();
				}
				
			}).start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
