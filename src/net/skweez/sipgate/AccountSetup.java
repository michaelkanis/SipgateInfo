package net.skweez.sipgate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AccountSetup extends Activity {

	private SharedPreferences settings;

	private EditText username;
	private EditText password;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_setup);

		settings = getSharedPreferences(Kontostand.PREFS_NAME, 0);

		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);

		String username_string = settings.getString("username", null);
		String password_string = settings.getString("password", null);

		if (username_string != null) {
			username.setText(username_string);
		}

		if (password_string != null) {
			password.setText(password_string);
		}
	}

	private void switchToMainActivity() {
		Intent myIntent = new Intent();
		myIntent.setClassName("net.skweez.sipgate", Kontostand.class.getName());
		startActivity(myIntent);
	}

	public void done(View view) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("username", username.getText().toString());
		editor.putString("password", password.getText().toString());

		// Commit the edits!
		editor.commit();

		switchToMainActivity();
	}

	public void cancel(View view) {
		switchToMainActivity();
	}
}