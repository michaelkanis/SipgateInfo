package net.skweez.sipgate.api;

public enum ECallStatus {
	ACCEPTED, OUTGOING, MISSED;

	public static ECallStatus fromString(String status) {
		if (status.equals("accepted")) {
			return ACCEPTED;
		} else if (status.equals("outgoing")) {
			return OUTGOING;
		} else if (status.equals("missed")) {
			return MISSED;
		} else {
			throw new IllegalArgumentException();
		}
	}
}