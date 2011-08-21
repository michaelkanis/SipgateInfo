package net.skweez.sipgate;

import java.util.Observable;
import java.util.Observer;

import net.skweez.sipgate.api.AuthenticationException;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.Toast;

/**
 * @author Michael Kanis
 */
public class SipgateTabActivity extends TabActivity implements Observer {

	private final AccountInfo accountInfo;

	private CallListAdapter callListAdapter;

	private AccountInfoAdapter accountInfoAdapter;

	public SipgateTabActivity() {
		accountInfo = new AccountInfo();
		accountInfo.addObserver(this);

	}

	/** {@inheritDoc} */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		callListAdapter = new CallListAdapter(this, accountInfo);
		accountInfoAdapter = new AccountInfoAdapter(this, accountInfo);

		setContentView(R.layout.main);
		initializeUi();
		refresh();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		int currentTab = getTabHost().getCurrentTab();
		super.onConfigurationChanged(newConfig);
		setContentView(R.layout.main);
		initializeUi();
		getTabHost().setCurrentTab(currentTab);
		refresh();
	}

	private void initializeUi() {
		initializeTabs();
	}

	private void initializeTabs() {
		TabHost tabHost = getTabHost();
		TabHost.TabSpec tabSpec;

		Resources resources = getResources();

		MyTabContentFactory factory = new MyTabContentFactory();

		tabSpec = tabHost.newTabSpec(getString(R.string.account_tab_tag));
		tabSpec.setIndicator(getString(R.string.account_tab_name),
				resources.getDrawable(R.drawable.ic_tab_account));
		tabSpec.setContent(factory);
		tabHost.addTab(tabSpec);

		tabSpec = tabHost.newTabSpec(getString(R.string.call_tab_tag));
		tabSpec.setIndicator(getString(R.string.call_tab_name),
				resources.getDrawable(R.drawable.ic_tab_recent));
		tabSpec.setContent(factory);
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
		accountInfo.refresh();
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

				view.setOnItemClickListener(contactClickedHandler);
				view.setAdapter(callListAdapter);

			} else if (tag.equals(getString(R.string.account_tab_tag))) {

				view.setAdapter(accountInfoAdapter);

			} else {
				throw new IllegalArgumentException();
			}

			return view;
		}

	}

	private OnItemClickListener contactClickedHandler = new OnItemClickListener() {

		public void onItemClick(AdapterView parent, View v, int position,
				long id) {

			callListAdapter.contactClicked(position);
		}
	};

	public void update(Observable observable, Object data) {
		if (data != null && data instanceof Exception) {
			Exception exception = (Exception) data;

			boolean causeIsWrongAuth = exception instanceof AuthenticationException;

			if (causeIsWrongAuth) {
				showSetupActivity();
			}

			showToast(exception.getMessage());
		}
	}

	private void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

}
