package net.skweez.sipgate.api;

import android.net.Uri;

/**
 * This is basically a phone number and the according sip://###@sipgate.de URI.
 * 
 * @author Florian Mutter
 * @author Michael Kanis
 */
public class UserUri {

	private final String e164Out;

	private final Uri sipUri;

	private final boolean defaultUri;

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