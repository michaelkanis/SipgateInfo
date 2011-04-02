package net.skweez.sipgate;

import java.util.Observable;
import java.util.Observer;

import net.skweez.sipgate.api.Call;
import net.skweez.sipgate.model.CallHistory;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CallListAdapter extends BaseAdapter implements Observer {

	static class ViewHolder {
		ImageView callStatusIcon;
		TextView numberText;
		TextView dateText;
	}

	private final CallHistory callHistory;

	private final LayoutInflater inflater;

	private final Activity activity;

	public CallListAdapter(Activity activity, CallHistory callHistory) {
		this.activity = activity;
		this.callHistory = callHistory;

		callHistory.addObserver(this);

		inflater = LayoutInflater.from(activity);
	}

	public int getCount() {
		return callHistory.size();
	}

	public Object getItem(int position) {
		return callHistory.getCall(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.call_list_item, null);

			holder = new ViewHolder();
			holder.callStatusIcon = (ImageView) convertView
					.findViewById(R.id.callStatusIcon);
			holder.numberText = (TextView) convertView
					.findViewById(R.id.numberText);
			holder.dateText = (TextView) convertView
					.findViewById(R.id.dateText);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Call call = callHistory.getCall(position);

		holder.numberText.setText(call.getRemoteURI().toString());
		holder.dateText.setText(call.getTimestamp().toString());

		return convertView;
	}

	public void update(Observable arg0, Object arg1) {
		// call from UI thread, or it will crash
		activity.runOnUiThread(new Runnable() {
			public void run() {
				notifyDataSetChanged();
			}
		});
	}

}
