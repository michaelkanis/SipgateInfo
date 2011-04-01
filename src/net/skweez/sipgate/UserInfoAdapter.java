package net.skweez.sipgate;

import java.util.Observable;
import java.util.Observer;

import net.skweez.sipgate.model.UserInfos;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserInfoAdapter extends BaseAdapter implements Observer {

	private UserInfos userInfos;
	private Context mContext;
	private Activity mActivity;

	public UserInfoAdapter(Context context, Activity activity) {
		this.mContext = context;
		this.mActivity = activity;

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
		TextView tv;
		if (convertView == null) {
			tv = new TextView(this.mContext);
		} else {
			tv = (TextView) convertView;
		}

		tv.setText(userInfos.getOwnURIs()[position].e164Out);

		return tv;
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
