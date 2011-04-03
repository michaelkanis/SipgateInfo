package net.skweez.sipgate.api;

public class UserUri {
	public String e164Out;
	public SipURI sipUri;
	public Boolean defaultUri;

	public UserUri(String e164Out, SipURI sipUri, Boolean defaultUri) {
		this.e164Out = e164Out;
		this.sipUri = sipUri;
		this.defaultUri = defaultUri;
	}
}
