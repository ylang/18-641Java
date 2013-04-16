package com.ece.smartGallery.DBLayout;

public class User {

	private int Id;
	private String firstName;
	private String lastName;
	private Album[] albums;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Album[] getAlbums() {
		return albums;
	}

	public void setAlbums(Album[] albums) {
		this.albums = albums;
	}
}