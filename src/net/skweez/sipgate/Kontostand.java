package net.skweez.sipgate;

import net.skweez.sipgate.api.ISipgateAPI;
import net.skweez.sipgate.api.xmlrpc.SipgateXmlRpcImpl;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class Kontostand extends Activity {
	/** Called when the activity is first created. */

	public static final String PREFS_NAME = "com.skweez.net.sipgate.kontostand.pref";

	private TextView tv;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	private void account_setup() {
		Intent myIntent = new Intent();
		myIntent.setClassName("net.skweez.sipgate",
				AccountSetup.class.getName());
		startActivity(myIntent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.account_setup:
			account_setup();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tv = new TextView(this);
		tv.setText("Trying to get your balance …");
		setContentView(tv);

		updateBalance();
	}

	public void updateBalance() {
		new Thread() {
			/** {@inheritDoc} */
			@Override
			public void run() {
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				final ISipgateAPI sipgate = new SipgateXmlRpcImpl(
						settings.getString("username", ""), settings.getString(
								"password", ""));

				tv.post(new Runnable() {
					public void run() {
						tv.setText(sipgate.getBalance().toString());
					}
				});
			}
		}.start();
	}

}