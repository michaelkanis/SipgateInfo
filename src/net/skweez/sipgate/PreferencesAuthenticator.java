package net.skweez.sipgate;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import android.content.SharedPreferences;

/**
 * 
 * @author mks
 * @author $Author: mks $
 * @version $Rev: 9 $
 * @levd.rating RED Rev:
 */
public class PreferencesAuthenticator extends Authenticator {

	private final SharedPreferences prefs;

	public PreferencesAuthenticator(SharedPreferences preferences) {
		this.prefs = preferences;
	}

	/** {@inheritDoc} */
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(prefs.getString("username", ""),
				prefs.getString("password", "").toCharArray());
	}
}
