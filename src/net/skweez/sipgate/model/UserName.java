package net.skweez.sipgate.model;

import net.skweez.sipgate.api.Gender;

public class UserName {
	private String firstName;
	private String lastName;
	private Gender gender;

	public UserName(String firstName, String lastName, Gender gender) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public Gender getGender() {
		return gender;
	}
}