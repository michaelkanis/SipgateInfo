package net.skweez.sipgate.api;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Michael Kanis
 */
public class Call {

	private static DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'kk:mm:ssZ");

	private Date timestamp;

	private SipURI localURI;

	private SipURI remoteURI;

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

	public SipURI getLocalURI() {
		return localURI;
	}

	public void setLocalURI(SipURI localURI) {
		this.localURI = localURI;
	}

	public SipURI getRemoteURI() {
		return remoteURI;
	}

	public void setRemoteURI(SipURI remoteURI) {
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
