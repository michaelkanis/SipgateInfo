package net.skweez.sipgate.api;

import android.net.Uri;

/**
 * @author Florian Mutter
 */
public class UserUri {
	public String e164Out;
	public Uri sipUri;
	public Boolean defaultUri;

	public UserUri(String e164Out, Uri sipUri, Boolean defaultUri) {
		this.e164Out = e164Out;
		this.sipUri = sipUri;
		this.defaultUri = defaultUri;
	}
}
