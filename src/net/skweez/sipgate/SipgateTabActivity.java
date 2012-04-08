package net.skweez.sipgate;

import java.util.Observable;
import java.util.Observer;

import net.skweez.sipgate.api.AuthenticationException;
import net.skweez.sipgate.api.Call;
import net.skweez.sipgate.db.CallsDataSource;
import net.skweez.sipgate.model.AccountInfo;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.Toast;

/**
 * @author Michael Kanis
 */
public class SipgateTabActivity extends TabActivity implements Observer,
		QueryResultReceiver.Receiver {

	private final AccountInfo accountInfo;

	private CallsDataSource dataSource;

	private CallListCursorAdapter callListAdapter;

	private AccountInfoAdapter accountInfoAdapter;

	private QueryResultReceiver mReceiver;

	public SipgateTabActivity() {
		accountInfo = new AccountInfo();
		accountInfo.addObserver(this);

	}

	/** {@inheritDoc} */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		accountInfoAdapter = new AccountInfoAdapter(this, accountInfo);
		callListAdapter = new CallListCursorAdapter(getApplicationContext(),
				null);

		mReceiver = new QueryResultReceiver(new Handler());
		mReceiver.setReceiver(this);

		initializeUi();
		reloadData();
	}

	@Override
	protected void onResume() {
		super.onResume();

		dataSource = new CallsDataSource(getApplicationContext());
		dataSource.open(true);
		callListAdapter.changeCursor(dataSource.getAllCallsCursor());
	}

	@Override
	protected void onPause() {
		super.onPause();
		mReceiver.setReceiver(null);
		dataSource.close();
		dataSource = null;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		int currentTab = getTabHost().getCurrentTab();
		super.onConfigurationChanged(newConfig);
		initializeUi();
		getTabHost().setCurrentTab(currentTab);
	}

	private void initializeUi() {
		setContentView(R.layout.main);
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
			reloadData();
			return true;
		case R.id.setup:
			showSetupActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void reloadData() {
		if (isNetworkAvailable()) {
			final Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
					QueryService.class);
			intent.putExtra("receiver", mReceiver);
			intent.putExtra("command", "query");
			startService(intent);

			accountInfo.refresh(this);
		} else {
			showToast(getString(R.string.network_not_availale));
		}
	}

	private boolean isNetworkAvailable() {
		final ConnectivityManager connMgr = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		final NetworkInfo[] netInfo = connMgr.getAllNetworkInfo();

		if (netInfo != null) {
			for (NetworkInfo item : netInfo) {
				if (item.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}

		return false;
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

	private final OnItemClickListener contactClickedHandler = new OnItemClickListener() {
		public void onItemClick(AdapterView p, View v, int position, long id) {
			contactClicked(position);
		}
	};

	private void contactClicked(int position) {
		Call call = callListAdapter.getCall(position);

		String number = call.getRemoteNumber();
		final Uri uri = Uri.fromParts("tel", number, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		final String[] items = new String[] { "Call", "Show Contact" };

		builder.setTitle(number);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {

				switch (item) {
				case 0:
					dialNumber(uri);
					break;
				case 1:
					openContact(uri);
					break;
				}
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	private void dialNumber(Uri number) {
		Intent intent = new Intent(Intent.ACTION_DIAL, number);
		startActivity(intent);
	}

	private void openContact(Uri number) {
		Intent intent = new Intent(
				ContactsContract.Intents.SHOW_OR_CREATE_CONTACT, number);
		startActivity(intent);
	}

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

	public void onReceiveResult(int resultCode, Bundle resultData) {
		switch (resultCode) {
		case QueryService.STATUS_RUNNING:
			showToast("Updating …");
			break;
		case QueryService.STATUS_UPDATED_CALLS:
			callListAdapter.notifyDataSetChanged();
			callListAdapter.getCursor().requery();
			showToast("Updated calls.");
			break;
		}
	}

	private void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}
