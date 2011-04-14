package net.skweez.sipgate;

import java.util.Observable;
import java.util.Observer;

import net.skweez.sipgate.model.UserInfos;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TwoLineListItem;

public class AccountInfoActivity extends Activity implements Observer {

	private UserInfos userInfos;
	private TextView userNameView;
	private TextView sipIdView;
	private TextView phoneNumberView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.accountinfo);

		userNameView = (TextView) findViewById(R.id.userName);
		sipIdView = ((TwoLineListItem) findViewById(R.id.sip_id)).getText2();
		phoneNumberView = ((TwoLineListItem) findViewById(R.id.phone_number))
				.getText2();

		getUserInfo();
	}

	private void getUserInfo() {
		userInfos = new UserInfos();
		userInfos.addObserver(this);
		userInfos.startRefresh();
	}

	public void update(Observable observable, Object data) {
		if (observable instanceof UserInfos) {
			this.runOnUiThread(new Runnable() {
				public void run() {
					userNameView.setText(userInfos.getUserName().toString());
					sipIdView.setText(userInfos.getDefaultUserUri().sipUri
							.getNumber());
					phoneNumberView.setText("+"
							+ userInfos.getDefaultUserUri().e164Out);

				}
			});
		}
	}
}
