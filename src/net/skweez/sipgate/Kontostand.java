package net.skweez.sipgate;

import java.net.URI;
import java.util.HashMap;

import net.skweez.sipgate.kontostand.R;

import org.xmlrpc.android.XMLRPCClient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
		myIntent.setClassName("net.skweez.sipgate.kontostand",
				"net.skweez.sipgate.kontostand.Account_setup");
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
		tv.setText("Trying to get your vat...");
		setContentView(tv);

		getBalance();
	}

	public void getBalance() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		URI uri = URI.create("https://samurai.sipgate.net/RPC2");
		XMLRPCClient client = new XMLRPCClient(uri, settings.getString(
				"username", ""), settings.getString("password", ""));

		XMLRPCMethod method = new XMLRPCMethod("samurai.BalanceGet", client,
				new IXMLRPCMethodCallback() {
					public void callFinished(Object result) {
						Log.d("Test", "callFinished");

						HashMap map = (HashMap) result;

						String totalIncludingVat = ((HashMap) map
								.get("CurrentBalance"))
								.get("TotalIncludingVat").toString();
						String currency = ((HashMap) map.get("CurrentBalance"))
								.get("Currency").toString();

						tv.setText(totalIncludingVat + " " + currency);
					}
				});
		method.call();
	}

}