package net.skweez.sipgate;

import java.util.Observable;
import java.util.Observer;

import net.skweez.sipgate.model.UserInfos;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
							+ userInfos.getUserName().firstName + " "
							+ userInfos.getUserName().lastName);
					sipIdView.setText(userInfos.getUserUriArray()[0].sipUri);
					phonenumberView.setText("+"
							+ userInfos.getUserUriArray()[0].e164Out);

				}
			});
		}
	}

	private class UserInfoAdapter extends BaseAdapter implements Observer {

		private Activity mActivity;
		private LayoutInflater mInfalter;

		public UserInfoAdapter(Activity activity) {
			this.mActivity = activity;

			this.mInfalter = LayoutInflater.from(activity);

			userInfos = new UserInfos();
			userInfos.addObserver(this);
			userInfos.startRefresh();
		}

		public int getCount() {
			return userInfos.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			if (convertView == null) {
				v = mInfalter.inflate(R.layout.userinfo, null);
			} else {
				v = (View) convertView;
			}

			// TODO: Oh no, this is very ugly. Please fix me...

			TextView tv = (TextView) v.findViewById(R.id.textView2);
			if (position == 0) {
				tv.setText(userInfos.getUserName().firstName + " "
						+ userInfos.getUserName().lastName);
			} else {
				tv.setText("+" + userInfos.getUserUriArray()[0].e164Out);
			}

			return v;
		}

		public void update(Observable observable, Object data) {
			if (observable instanceof UserInfos) {
				mActivity.runOnUiThread(new Runnable() {
					public void run() {
						notifyDataSetChanged();
					}
				});
			}
		}
	}
}
