package net.skweez.sipgate.api;

public class UserUri {
	public String e164Out;
	public Boolean defaultUri;

	public UserUri(String e164Out, Boolean defaultUri) {
		this.e164Out = e164Out;
		this.defaultUri = defaultUri;
	}
}
