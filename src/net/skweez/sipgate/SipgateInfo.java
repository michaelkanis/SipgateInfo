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
public class SipgateInfo extends TabActivity {

	/** {@inheritDoc} */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TabHost tabHost = getTabHost();
		TabHost.TabSpec tabSpec;
		Intent intent;

		intent = new Intent().setClass(this, Kontostand.class);
		tabSpec = tabHost.newTabSpec("account").setIndicator("Account")
				.setContent(intent);
		tabHost.addTab(tabSpec);

		intent = new Intent().setClass(this, AccountSetup.class);
		tabSpec = tabHost.newTabSpec("setup").setIndicator("Setup")
				.setContent(intent);
		tabHost.addTab(tabSpec);
	}
}
