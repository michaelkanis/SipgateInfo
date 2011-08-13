package net.skweez.sipgate.api;

import android.net.Uri;

/**
 * @author Florian Mutter
 * @author Michael Kanis
 */
public class UserUri {
	
	private String e164Out;
	
	private Uri sipUri;
	
	private boolean defaultUri;

	public UserUri(String e164Out, Uri sipUri, Boolean defaultUri) {
		this.e164Out = e164Out;
		this.sipUri = sipUri;
		this.defaultUri = defaultUri;
	}

	public String getOutgoingNumber() {
		return e164Out;
	}

	public Uri getSipUri() {
		return sipUri;
	}

	public boolean isDefaultUri() {
		return defaultUri;
	}

}