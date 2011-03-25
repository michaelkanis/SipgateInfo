package net.skweez.sipgate.kontostand;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Account_setup extends Activity {
	
	public static final String PREFS_NAME = "com.skweez.net.sipgate.kontostand.pref";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setup);
	}
	
	private void switchToMainActivity() {
		Intent myIntent = new Intent();
		myIntent.setClassName("net.skweez.sipgate.kontostand", "net.skweez.sipgate.kontostand.Kontostand");
		startActivity(myIntent);
	}
	
	public void done(View view) {
		final EditText username = (EditText) findViewById(R.id.username);
		final EditText password = (EditText) findViewById(R.id.password);
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
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
