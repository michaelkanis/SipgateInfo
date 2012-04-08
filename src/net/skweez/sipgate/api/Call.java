package net.skweez.sipgate.api;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;

/**
 * @author Michael Kanis
 */
public class Call {

	public static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'kk:mm:ssZ");

	/**
	 * @see http://groups.google.com/group/sipgate-api/tree/browse_frm/thread/
	 *      ba8628f1ade4622c/e1b7d6a847c85949
	 */
	private static final String[] statusPrefixes = new String[] { "1100",
			"1000", "1200", "2100", "2000", "2200", "2300", "2301", "2400",
			"3000", "3100", "1020", "4000" };

	private Date timestamp;

	private Uri remoteURI;

	private ECallStatus status;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setTimestamp(String timestamp) {
		try {
			this.timestamp = DATE_FORMAT.parse(timestamp);
		} catch (ParseException e) {
			e.printStackTrace();
			// TODO proper error handling
		}
	}

	public Uri getRemoteURI() {
		return remoteURI;
	}

	public String getRemoteNumber() {
		String number = remoteURI.getUserInfo();

		for (String prefix : statusPrefixes) {
			if (number.startsWith(prefix)) {
				number = number.substring(4);
				break;
			}
		}

		return "00" + number;
	}

	public void setRemoteURI(Uri remoteURI) {
		this.remoteURI = remoteURI;
	}

	public ECallStatus getStatus() {
		return status;
	}

	public void setStatus(ECallStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return getRemoteNumber();
	}

}
