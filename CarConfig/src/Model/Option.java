package Model;

import java.io.Serializable;

public class Option implements Serializable {

	private static final long serialVersionUID = -5285313855089330316L;

	private String name;
	private int price;

	public Option(String name, int price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return this.price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
}