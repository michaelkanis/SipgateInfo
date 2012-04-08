package net.skweez.sipgate;

import net.skweez.sipgate.api.ECallStatus;

public class CallUtils {

	/**
	 * Returns the status image for a given call status (missed, outgoing,
	 * incoming).
	 */
	public static int getImage(ECallStatus status) {
		// FIXME This is a temporary hack
		if (status == null) {
			return R.drawable.ic_call_log_list_incoming_call;
		}

		switch (status) {
		case ACCEPTED:
			return R.drawable.ic_call_log_list_incoming_call;
		case MISSED:
			return R.drawable.ic_call_log_list_missed_call;
		case OUTGOING:
			return R.drawable.ic_call_log_list_outgoing_call;
		default:
			throw new IllegalArgumentException();
		}
	}
}
