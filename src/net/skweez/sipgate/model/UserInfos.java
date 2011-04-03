package net.skweez.sipgate.model;

import java.util.Observable;

import net.skweez.sipgate.api.ISipgateAPI;
import net.skweez.sipgate.api.SipgateException;
import net.skweez.sipgate.api.UserName;
import net.skweez.sipgate.api.UserUri;
import net.skweez.sipgate.api.xmlrpc.SipgateXmlRpcImpl;
import android.util.Log;

public class UserInfos extends Observable {

	private UserName userName;
	private UserUri[] userUriArray;

	public int length;

	public UserInfos() {
		length = 0;
	}

	public void startRefresh() {

		new Thread() {

			/** {@inheritDoc} */
			@Override
			public void run() {
				try {
					final ISipgateAPI sipgate = new SipgateXmlRpcImpl();
					setUserInfos(sipgate.getUserUriList(),
							sipgate.getUserName());
				} catch (SipgateException e) {
					Log.e("Sipgate", "error", e);
					// TODO proper error handling!
				}
			}
		}.start();
	}

	private synchronized void setUserInfos(UserUri[] userUriArray,
			UserName userName) {
		this.userUriArray = userUriArray;
		this.userName = userName;
		this.length = userUriArray.length + 1;

		setChanged();
		notifyObservers();
	}

	public UserUri[] getUserUriArray() {
		return userUriArray;
	}

	public UserUri getDefaultUserUri() {
		for (int i = 0; i < userUriArray.length; i++) {
			if (userUriArray[i].defaultUri)
				return userUriArray[i];
		}
		// if there is no default userUri, we return the first one in the array
		return userUriArray[0];
	}

	public UserName getUserName() {
		return userName;
	}
}
