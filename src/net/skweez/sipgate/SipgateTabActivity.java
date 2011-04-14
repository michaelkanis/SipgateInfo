package net.skweez.sipgate;

import java.util.Observable;
import java.util.Observer;

import net.skweez.sipgate.model.Balance;
import net.skweez.sipgate.model.CallHistory;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
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

	private CallHistory history;

	/** {@inheritDoc} */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initializeUi();
		refreshBalance();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setContentView(R.layout.main);
		initializeUi();
		refreshBalance();
	}

	private void initializeUi() {
		initializeTabs();
		balanceView = (TextView) findViewById(R.id.balanceView);
	}

	private void initializeTabs() {
		TabHost tabHost = getTabHost();
		TabHost.TabSpec tabSpec;
		Intent intent;

		Resources resources = getResources();

		intent = new Intent().setClass(this, AccountInfoActivity.class);
		tabSpec = tabHost
				.newTabSpec("account")
				.setIndicator("Account",
						resources.getDrawable(R.drawable.ic_tab_account))
				.setContent(intent);
		tabHost.addTab(tabSpec);

		tabSpec = tabHost.newTabSpec(getString(R.string.call_tab_tag));
		tabSpec.setIndicator(getString(R.string.call_tab_name),
				resources.getDrawable(R.drawable.ic_tab_recent));
		tabSpec.setContent(new MyTabContentFactory());
		tabHost.addTab(tabSpec);
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

	private class MyTabContentFactory implements TabContentFactory {

		public View createTabContent(String tag) {

			if (tag.equals(getString(R.string.call_tab_tag))) {
				return createCallList();
			}

			throw new IllegalArgumentException();
		}

		private View createCallList() {
			ListView view = new ListView(getApplicationContext());

			if (history == null) {
				history = new CallHistory();
				history.startRefresh();
			}

			view.setAdapter(new CallListAdapter(SipgateTabActivity.this,
					history));

			return view;
		}

	}
}
