package net.skweez.sipgate;

import android.app.ListActivity;
import android.os.Bundle;

public class AccountInfoActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setListAdapter(new UserInfoAdapter(this, this));

	}
}
