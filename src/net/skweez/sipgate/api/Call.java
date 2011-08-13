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

	private static DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'kk:mm:ssZ");

	private Date timestamp;

	private Uri localURI;

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
			this.timestamp = dateFormat.parse(timestamp);
		} catch (ParseException e) {
			e.printStackTrace();
			// TODO proper error handling
		}
	}

	public Uri getLocalURI() {
		return localURI;
	}

	public void setLocalURI(Uri localURI) {
		this.localURI = localURI;
	}

	public Uri getRemoteURI() {
		return remoteURI;
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
		return getRemoteURI().toString();
	}

}
