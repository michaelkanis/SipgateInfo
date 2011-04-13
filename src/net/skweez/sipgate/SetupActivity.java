package net.skweez.sipgate;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

public class SetupActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	private static final String TAG = SetupActivity.class.getName();

	private EditTextPreference userNamePreference;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.setup);

		addPreferencesFromResource(R.xml.preferences);

		userNamePreference = (EditTextPreference) getPreferenceScreen()
				.findPreference("username");

		// Get the version number and set it as the summary of the version pref.
		PackageManager pm = getPackageManager();
		PackageInfo info = null;
		try {
			info = pm.getPackageInfo("net.skweez.sipgate", 0);
		} catch (NameNotFoundException e) {
			Log.e(TAG, "Cannot find package info.");
		}
		Preference version = findPreference(getString(R.string.version_key));
		version.setSummary(info.versionName);
	}

	/** {@inheritDoc} */
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

		if (key.equals("username")) {
			userNamePreference.setSummary(sharedPreferences.getString(
					"username", "Please set up your user name."));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		userNamePreference.setSummary(getPreferenceScreen()
				.getSharedPreferences().getString("username",
						"Please set up your user name."));

		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}
}
