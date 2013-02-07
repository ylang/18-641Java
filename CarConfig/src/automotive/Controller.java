package automotive;

public class Controller {

	public static void main(String[] args) {

	}

	public static void preset() {
		// set colors
		OptionSet colors = new OptionSet("Color", 10);
		colors.setOption(0, "Fort Knox Gold Clearcoat Metallic", 0);
		colors.setOption(1, "Liquid Grey Clearcoat Metallic", 0);
		colors.setOption(2, "Infra-Red Clearcoat", 0);
		colors.setOption(3, "Grabber Green Clearcoat Metallic", 0);
		colors.setOption(4, "Sangria Red Clearcoat Metallic", 0);
		colors.setOption(5, "French Blue Clearcoat Metallic", 0);
		colors.setOption(6, "Twilight Blue Clearcoat Metallic", 0);
		colors.setOption(7, "CD Silver Clearcoat Metallic", 0);
		colors.setOption(8, "Pitch Black Clearcoat", 0);
		colors.setOption(9, "Cloud 9 White Clearcoat", 0);
		// set transmission
		OptionSet transmissions = new OptionSet("Transmission", 2);
		transmissions.setOption(0, "automatic", 0);
		transmissions.setOption(1, "manual", -815);
		// set brakes/traction control
	}
}
