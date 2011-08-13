package net.skweez.sipgate.api.xmlrpc;

import android.net.Uri;

public abstract class SipgateUriHelper {

	/**
	 * Sipgate sends "URIs" without the "//", so we have to insert them.
	 * 
	 * @param uriString - the URI string from Sipgate (without "//")
	 * @return A Uri object
	 */
	public static Uri createUriFromString(String uriString) {

		Uri uri;
		
		if (uriString.startsWith("sip://")) {
			uri = Uri.parse(uriString);
		} else {
			StringBuilder uriBuilder = new StringBuilder(uriString);
			uri = Uri.parse(uriBuilder.insert(4, "//").toString());
		}

		return uri;
		
	}

}
