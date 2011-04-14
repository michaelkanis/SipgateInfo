package net.skweez.sipgate.model;

import net.skweez.sipgate.api.Gender;

public class UserName {
	private final String firstName;
	private final String lastName;
	private final Gender gender;

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

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getFirstName() + " " + getLastName();
	}
}