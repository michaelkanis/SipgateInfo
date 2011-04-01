package net.skweez.sipgate.api;

public class SipURI {

	private final String uri;

	public SipURI(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}
	
	@Override
	public String toString() {
		return getUri();
	}

}
