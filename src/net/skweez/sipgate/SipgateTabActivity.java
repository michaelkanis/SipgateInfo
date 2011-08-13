package net.skweez.sipgate;

import net.skweez.sipgate.model.CallHistory;
import net.skweez.sipgate.model.AccountInfo;
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

/**
 * @author Michael Kanis
 */
public class SipgateTabActivity extends TabActivity {

	private final CallHistory history = new CallHistory();

	private final AccountInfo accountInfo = new AccountInfo();

	/** {@inheritDoc} */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initializeUi();
		refresh();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setContentView(R.layout.main);
		initializeUi();
		refresh();
	}

	private void initializeUi() {
		initializeTabs();
	}

	private void initializeTabs() {
		TabHost tabHost = getTabHost();
		TabHost.TabSpec tabSpec;

		Resources resources = getResources();

		tabSpec = tabHost.newTabSpec(getString(R.string.account_tab_tag));
		tabSpec.setIndicator(getString(R.string.account_tab_name),
				resources.getDrawable(R.drawable.ic_tab_account));
		tabSpec.setContent(new MyTabContentFactory());
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
			refresh();
			return true;
		case R.id.setup:
			showSetupActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void refresh() {
		history.startRefresh();
		accountInfo.startRefresh();
		System.out.println("refresh");
	}

	private void showSetupActivity() {
		Intent intent;

		intent = new Intent().setClass(this, SetupActivity.class);
		startActivity(intent);
	}

	private class MyTabContentFactory implements TabContentFactory {

		public View createTabContent(String tag) {

			ListView view = new ListView(getApplicationContext());

			if (tag.equals(getString(R.string.call_tab_tag))) {
				view.setAdapter(new CallListAdapter(SipgateTabActivity.this,
						history));
			} else if (tag.equals(getString(R.string.account_tab_tag))) {
				view.setAdapter(new AccountInfoAdapter(SipgateTabActivity.this,
						accountInfo));
			} else {
				throw new IllegalArgumentException();
			}

			return view;
		}

	}
}
