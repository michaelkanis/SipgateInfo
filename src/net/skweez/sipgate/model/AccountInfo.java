package net.skweez.sipgate.model;

import java.util.Observable;

import net.skweez.sipgate.api.ISipgateAPI;
import net.skweez.sipgate.api.Price;
import net.skweez.sipgate.api.SipgateException;
import net.skweez.sipgate.api.UserName;
import net.skweez.sipgate.api.UserUri;
import net.skweez.sipgate.api.xmlrpc.SipgateXmlRpcImpl;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

/**
 * @author Florian Mutter
 * @author Michael Kanis
 */
public class AccountInfo extends Observable {

	private static final String TAG = AccountInfo.class.getName();

	private UserName userName;

	private UserUri defaultUserUri;

	private Price balance;

	private Activity mActivity;

	public void refresh(Activity activity) {
		mActivity = activity;
		new RefreshAccountInfoTask().execute();
	}

	private class RefreshAccountInfoTask extends AsyncTask<Void, Void, Void> {

		private Exception exception = null;

		@Override
		protected Void doInBackground(Void... params) {
			try {
				final ISipgateAPI sipgate = new SipgateXmlRpcImpl();

				setUserName(sipgate.getUserName());
				setDefaultUserUri(sipgate.getUserUriList());
				setBalance(sipgate.getBalance());
			} catch (SipgateException exception) {
				Log.e(TAG, "Exception when refreshing AccountInfo", exception);
				this.exception = exception;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mActivity.setProgressBarIndeterminateVisibility(false);
			notifyObservers(exception);
		}

		@Override
		protected void onPreExecute() {
			mActivity.setProgressBarIndeterminateVisibility(true);
		}

	}

	public String getCustomerNumber() {
		if (getDefaultUserUri() != null)
			return getDefaultUserUri().getSipUri().getUserInfo();
		else
			return "";
	}

	public String getPhoneNumber() {
		if (getDefaultUserUri() != null)
			return getDefaultUserUri().getOutgoingNumber();
		else
			return "";
	}

	public UserUri getDefaultUserUri() {
		return defaultUserUri;
	}

	private synchronized void setDefaultUserUri(UserUri[] userUriArray) {
		for (int i = 0; i < userUriArray.length; i++) {
			if (userUriArray[i].isDefaultUri())
				defaultUserUri = userUriArray[i];
		}
		// if there is no default userUri, we use the first one in the array
		defaultUserUri = userUriArray[0];
		setChanged();
	}

	public UserName getUserName() {
		return userName;
	}

	private synchronized void setUserName(UserName userName) {
		this.userName = userName;
		setChanged();
	}

	public Price getBalance() {
		return balance;
	}

	private synchronized void setBalance(Price balance) {
		this.balance = balance;
		setChanged();
	}
}
