package net.skweez.sipgate;

import java.util.Observable;
import java.util.Observer;

import net.skweez.sipgate.model.UserInfos;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AccountInfoActivity extends Activity implements Observer {
	
	private TextView sipuriView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.account);
		
		sipuriView = (TextView)findViewById(R.id.sipuriView);
		
		refreshAccountInfo();
	}
	
	private void refreshAccountInfo() {
		sipuriView.setText("Loading …");

		UserInfos userInfos = new UserInfos();
		userInfos.addObserver(this);
		userInfos.startRefresh();
	}

	public void update(final Observable observable, Object data) {
		if (observable instanceof UserInfos) {
			sipuriView.post(new Runnable() {
				public void run() {
					sipuriView.setText(((UserInfos) observable).getOwnURIs()[0].sipURI);
				}
			});
		}
		
	}

}
