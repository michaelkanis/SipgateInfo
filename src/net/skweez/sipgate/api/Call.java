package net.skweez.sipgate.api;

import java.util.Date;

public class Call {

	public static enum ECallStatus {
		ACCEPTED, OUTGOING, MISSED
	}

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
