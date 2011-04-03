package net.skweez.sipgate.model;

import net.skweez.sipgate.api.Gender;

public class UserName {
	public String firstName;
	public String lastName;
	public Gender gender;

	public UserName(String firstName, String lastName, Gender gender) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
	}
}