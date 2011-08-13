package net.skweez.sipgate;

import java.util.Observable;
import java.util.Observer;

import net.skweez.sipgate.model.Balance;
import net.skweez.sipgate.model.UserInfos;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TwoLineListItem;

public class AccountInfoActivity extends Activity implements Observer {

	private TextView userNameView;
	private TextView sipIdView;
	private TextView phoneNumberView;
	private TextView balanceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.accountinfo);

		userNameView = (TextView) findViewById(R.id.userName);
		sipIdView = ((TwoLineListItem) findViewById(R.id.sip_id)).getText2();
		phoneNumberView = ((TwoLineListItem) findViewById(R.id.phone_number))
				.getText2();
		balanceView = ((TwoLineListItem) findViewById(R.id.account_balance))
				.getText2();

		refreshUserInfos();
		refreshBalance();
	}

	private void refreshUserInfos() {
		UserInfos userInfos = new UserInfos();
		userInfos.addObserver(this);
		userInfos.startRefresh();
	}

	/** Tells the balance object to refresh itself. */
	private void refreshBalance() {
		Balance balance = new Balance();
		balance.addObserver(this);
		balance.startRefresh();
	}

	public void update(final Observable observable, Object data) {
		if (observable instanceof UserInfos) {
			this.runOnUiThread(new Runnable() {
				public void run() {
					updateUserInfos((UserInfos) observable);
				}
			});
		} else if (observable instanceof Balance) {
			this.runOnUiThread(new Runnable() {
				public void run() {
					updateBalance((Balance) observable);
				}
			});
		}
	}

	private void updateUserInfos(final UserInfos userInfos) {
		userNameView.setText(userInfos.getUserName().toString());
		sipIdView.setText(userInfos.getDefaultUserUri().sipUri.getUserInfo());
		phoneNumberView.setText("+" + userInfos.getDefaultUserUri().e164Out);
	}

	protected void updateBalance(Balance balance) {
		balanceView.setText(balance.getBalance().toString());
	}
}
