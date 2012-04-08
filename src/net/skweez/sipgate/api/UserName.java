package net.skweez.sipgate.api;

public class UserName {
	private final String firstName;
	private final String lastName;

	public UserName(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return new StringBuilder(getFirstName()).append(' ')
				.append(getLastName()).toString();
	}
}
