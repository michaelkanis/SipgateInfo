package net.skweez.sipgate;

import java.net.Authenticator;
import java.util.Observable;
import java.util.Observer;

import net.skweez.sipgate.model.Balance;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * 
 * @author mks
 * @author $Author: mks $
 * @version $Rev: 9 $
 * @levd.rating RED Rev:
 */
public class SipgateTabActivity extends TabActivity implements Observer {

	/** The view that shows the account balance. */
	private TextView balanceView;

	/** {@inheritDoc} */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Resources resources = getResources();

		Authenticator.setDefault(new PreferencesAuthenticator(PreferenceManager
				.getDefaultSharedPreferences(this)));

		TabHost tabHost = getTabHost();
		TabHost.TabSpec tabSpec;
		Intent intent;

		intent = new Intent().setClass(this, AccountInfoActivity.class);
		tabSpec = tabHost.newTabSpec("account").setIndicator("Account")
				.setContent(intent);
		tabHost.addTab(tabSpec);

		intent = new Intent().setClass(this, CallHistoryActivity.class);
		tabSpec = tabHost
				.newTabSpec("calls")
				.setIndicator("Calls",
						resources.getDrawable(R.drawable.ic_tab_recent))
				.setContent(intent);
		tabHost.addTab(tabSpec);

		balanceView = (TextView) findViewById(R.id.balanceView);
		refreshBalance();
	}

	/** {@inheritDoc} */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.refresh:
			refreshBalance();
			return true;
		case R.id.setup:
			showSetupActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/** Tells the balance object to refresh itself. */
	private void refreshBalance() {
		balanceView.setText("Loading …");

		Balance balance = new Balance();
		balance.addObserver(this);
		balance.startRefresh();
	}

	private void showSetupActivity() {
		Intent intent;

		intent = new Intent().setClass(this, SetupActivity.class);
		startActivity(intent);
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
