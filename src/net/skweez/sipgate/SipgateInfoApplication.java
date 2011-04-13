package net.skweez.sipgate;

import java.net.Authenticator;

import android.app.Application;
import android.preference.PreferenceManager;

/**
 * 
 * @author mks
 * @author $Author: mks $
 * @version $Rev: 9 $
 * @levd.rating RED Rev:
 */
public class SipgateInfoApplication extends Application {

	/** {@inheritDoc} */
	@Override
	public void onCreate() {
		super.onCreate();
		Authenticator.setDefault(new PreferencesAuthenticator(PreferenceManager
				.getDefaultSharedPreferences(this)));
	}

}
