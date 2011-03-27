package net.skweez.sipgate;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * 
 * @author mks
 * @author $Author: mks $
 * @version $Rev: 9 $
 * @levd.rating RED Rev:
 */
public class SipgateTabActivity extends TabActivity {

	/** {@inheritDoc} */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TabHost tabHost = getTabHost();
		TabHost.TabSpec tabSpec;
		Intent intent;

		intent = new Intent().setClass(this, AccountActivity.class);
		tabSpec = tabHost.newTabSpec("account").setIndicator("Account")
				.setContent(intent);
		tabHost.addTab(tabSpec);

		intent = new Intent().setClass(this, CallsListActivity.class);
		tabSpec = tabHost.newTabSpec("calls").setIndicator("Calls")
				.setContent(intent);
		tabHost.addTab(tabSpec);
	}
}