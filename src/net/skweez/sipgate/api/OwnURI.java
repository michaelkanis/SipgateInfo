package net.skweez.sipgate.api;

public class OwnURI {
	public String sipURI;
	public String e164Out;
	public String[] e164In;
	public String[] tos;
	public String defaultUri;
		
	public OwnURI(String sipURI) {
		this.sipURI = sipURI;
	}
}
