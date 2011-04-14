package net.skweez.sipgate;

import net.skweez.sipgate.model.Balance;
import net.skweez.sipgate.model.UserInfos;
import android.app.Activity;
import android.os.Bundle;

public class AccountInfoActivity extends Activity {

	private AccountInfoView view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		view = new AccountInfoView(this);
		setContentView(view);

		refreshUserInfos();
		refreshBalance();
	}

	private void refreshUserInfos() {
		UserInfos userInfos = new UserInfos();
		userInfos.addObserver(view);
		userInfos.startRefresh();
	}

	/** Tells the balance object to refresh itself. */
	private void refreshBalance() {
		Balance balance = new Balance();
		balance.addObserver(view);
		balance.startRefresh();
	}

}
