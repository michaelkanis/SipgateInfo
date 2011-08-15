package net.skweez.sipgate;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import android.content.SharedPreferences;

/**
 * An implementation of {@link Authenticator} that gets user name and password
 * from the {@link SharedPreferences}.
 * 
 * @author Michael Kanis
 */
public class PreferencesAuthenticator extends Authenticator {

	private final SharedPreferences prefs;

	public PreferencesAuthenticator(SharedPreferences preferences) {
		this.prefs = preferences;
	}

	/** {@inheritDoc} */
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {

		String userName = prefs.getString("username", null);
		String password = prefs.getString("password", null);

		if (userName == null || password == null) {
			return null;
		}

		return new PasswordAuthentication(userName, password.toCharArray());
	}
}
