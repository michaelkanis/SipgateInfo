package net.skweez.sipgate;

import java.net.Authenticator;
import java.util.Observable;
import java.util.Observer;

import net.skweez.sipgate.model.Balance;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class AccountActivity extends Activity implements Observer {

	public static final String PREFS_NAME = "net.skweez.sipgate.pref";

	/** The view that shows the account balance. */
	private TextView balanceView;

	/** {@inheritDoc} */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	private void accountSetup() {
		Intent myIntent = new Intent();
		myIntent.setClassName("net.skweez.sipgate",
				SetupActivity.class.getName());
		startActivity(myIntent);
	}

	/** {@inheritDoc} */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.account_setup:
			accountSetup();
			return true;
		case R.id.refresh:
			refreshBalance();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);

		Authenticator.setDefault(new PreferencesAuthenticator(PreferenceManager
				.getDefaultSharedPreferences(this)));

		balanceView = (TextView) findViewById(R.id.balanceView);
		refreshBalance();
	}

	/** Tells the balance object to refresh itself. */
	private void refreshBalance() {
		balanceView.setText("Loading …");

		Balance balance = new Balance();
		balance.addObserver(this);
		balance.startRefresh();
	}

	/** Update the view when the balance object updated itself. */
	public void update(final Observable observable, final Object data) {
		if (observable instanceof Balance) {
			balanceView.post(new Runnable() {
				public void run() {
					balanceView.setText(((Balance) observable).getBalance()
							.toString());
				}
			});
		}
	}

}