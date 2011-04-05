package net.skweez.sipgate;

import java.util.Observable;
import java.util.Observer;

import net.skweez.sipgate.model.UserInfos;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AccountInfoActivity extends Activity implements Observer {

	private UserInfos userInfos;
	private TextView userNameView;
	private TextView sipIdView;
	private TextView phonenumberView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.accountinfo);

		userNameView = (TextView) findViewById(R.id.userName);
		sipIdView = (TextView) findViewById(R.id.sipid);
		phonenumberView = (TextView) findViewById(R.id.phonenumber);

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
					userNameView.setText("Hello, "
							+ userInfos.getUserName().getFirstName() + " "
							+ userInfos.getUserName().getLastName());
					sipIdView.setText(userInfos.getDefaultUserUri().sipUri
							.getNumber());
					phonenumberView.setText("+"
							+ userInfos.getDefaultUserUri().e164Out);

				}
			});
		}
	}
}
