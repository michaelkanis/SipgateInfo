package net.skweez.sipgate.api;

public enum Gender {
	MALE, FEMALE;
	
	public static Gender fromString(String gender) {
		if (gender.equals("male")) {
			return Gender.MALE;
		} else if (gender.equals("female")) {
			return Gender.FEMALE;
		} else {
			throw new IllegalArgumentException();
		}
	}
}
