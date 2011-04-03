package net.skweez.sipgate;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import net.skweez.sipgate.api.Call;
import net.skweez.sipgate.api.ECallStatus;
import net.skweez.sipgate.model.CallHistory;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
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

	private final Map<String, String> contactNamesCache;

	public CallListAdapter(Activity activity, CallHistory callHistory) {
		this.activity = activity;
		this.callHistory = callHistory;

		callHistory.addObserver(this);

		inflater = LayoutInflater.from(activity);

		contactNamesCache = new HashMap<String, String>();
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

		holder.numberText.setText(getContactNameFromNumber(call.getRemoteURI()
				.toString()));
		holder.dateText.setText(call.getTimestamp().toString());
		holder.callStatusIcon.setImageResource(getImage(call.getStatus()));

		return convertView;
	}

	/**
	 * Code just stolen from
	 * http://www.vbsteven.be/blog/android-getting-the-contact-name-of-
	 * a-phone-number/
	 * 
	 * TODO Understand ;) and rewrite this method with new API to fix
	 * deprecation.
	 * 
	 * @param number
	 * @return
	 */
	private String getContactNameFromNumber(String number) {

		String name = contactNamesCache.get(number);
		if (name != null) {
			return name;
		}

		// define the columns I want the query to return
		String[] projection = new String[] { Contacts.Phones.DISPLAY_NAME,
				Contacts.Phones.NUMBER };

		// encode the phone number and build the filter URI
		Uri contactUri = Uri.withAppendedPath(
				Contacts.Phones.CONTENT_FILTER_URL, Uri.encode(number));

		// query
		Cursor c = activity.getContentResolver().query(contactUri, projection,
				null, null, null);

		// returns the first result if the query returns 1 or more results
		if (c.moveToFirst()) {
			name = c.getString(c.getColumnIndex(Contacts.Phones.DISPLAY_NAME));
			contactNamesCache.put(number, name);
			return name;
		}

		// return the original number if no match was found
		return number;
	}

	private int getImage(ECallStatus status) {
		switch (status) {
		case ACCEPTED:
			return R.drawable.ic_call_log_list_incoming_call;
		case MISSED:
			return R.drawable.ic_call_log_list_missed_call;
		case OUTGOING:
			return R.drawable.ic_call_log_list_outgoing_call;
		default:
			throw new IllegalArgumentException();
		}
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
