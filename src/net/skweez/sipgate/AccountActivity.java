package net.skweez.sipgate;

import java.net.Authenticator;

import net.skweez.sipgate.api.ISipgateAPI;
import net.skweez.sipgate.api.SipgateException;
import net.skweez.sipgate.api.xmlrpc.SipgateXmlRpcImpl;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class AccountActivity extends Activity {

	public static final String PREFS_NAME = "net.skweez.sipgate.pref";

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
		setContentView(R.layout.account);

		tv = (TextView) findViewById(R.id.balanceView);

		Authenticator.setDefault(new PreferencesAuthenticator(
				getSharedPreferences(PREFS_NAME, 0)));

		tv.setText("Loading …");

		updateBalance();
	}

	private void updateBalance() {
		new Thread() {
			/** {@inheritDoc} */
			@Override
			public void run() {
				try {
					final ISipgateAPI sipgate = new SipgateXmlRpcImpl();
					final String balance = sipgate.getBalance().toString();

					tv.post(new Runnable() {
						public void run() {
							tv.setText(balance);
						}
					});
				} catch (SipgateException e) {
					Log.e("Sipgate", "error", e);

					tv.post(new Runnable() {
						public void run() {
							tv.setText("Error");
						}
					});
				}
			}
		}.start();
	}

}