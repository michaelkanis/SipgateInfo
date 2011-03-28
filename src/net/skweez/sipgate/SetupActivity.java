package net.skweez.sipgate;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;

public class SetupActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	private EditTextPreference userNamePreference;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.setup);
		
		addPreferencesFromResource(R.xml.preferences);

		userNamePreference = (EditTextPreference) getPreferenceScreen()
				.findPreference("username");
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
