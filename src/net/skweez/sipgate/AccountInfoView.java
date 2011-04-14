package net.skweez.sipgate;

import java.util.Observable;
import java.util.Observer;

import net.skweez.sipgate.model.Balance;
import net.skweez.sipgate.model.UserInfos;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TwoLineListItem;

public class AccountInfoView extends LinearLayout implements Observer {

	private final TextView userNameView;
	private final TextView sipIdView;
	private final TextView phoneNumberView;
	private final TextView balanceView;

	public AccountInfoView(Context context) {
		super(context);
		setOrientation(VERTICAL);
		LayoutInflater.from(context).inflate(R.layout.accountinfo, this, true);

		userNameView = (TextView) findViewById(R.id.userName);
		sipIdView = ((TwoLineListItem) findViewById(R.id.sip_id)).getText2();
		phoneNumberView = ((TwoLineListItem) findViewById(R.id.phone_number))
				.getText2();
		balanceView = ((TwoLineListItem) findViewById(R.id.account_balance))
				.getText2();
	}

	public void update(final Observable observable, Object data) {
		if (observable instanceof UserInfos) {
			this.post(new Runnable() {
				public void run() {
					updateUserInfos((UserInfos) observable);
				}
			});
		} else if (observable instanceof Balance) {
			this.post(new Runnable() {
				public void run() {
					updateBalance((Balance) observable);
				}
			});
		}
	}

	private void updateUserInfos(final UserInfos userInfos) {
		userNameView.setText(userInfos.getUserName().toString());
		sipIdView.setText(userInfos.getDefaultUserUri().sipUri.getNumber());
		phoneNumberView.setText("+" + userInfos.getDefaultUserUri().e164Out);
	}

	protected void updateBalance(Balance balance) {
		balanceView.setText(balance.getBalance().toString());
	}

}
