package net.skweez.sipgate.model;

import java.util.Observable;

import net.skweez.sipgate.api.ISipgateAPI;
import net.skweez.sipgate.api.SipgateException;
import net.skweez.sipgate.api.UserUri;
import net.skweez.sipgate.api.xmlrpc.SipgateXmlRpcImpl;
import android.util.Log;

public class UserInfos extends Observable {

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
					setOwnURIs(sipgate.getOwnURIList());
					setChanged();
					notifyObservers();
				} catch (SipgateException e) {
					Log.e("Sipgate", "error", e);
					// TODO proper error handling!
				}
			}
		}.start();
	}

	private synchronized void setOwnURIs(UserUri[] userUriArray) {
		this.userUriArray = userUriArray;
		this.length = userUriArray.length;
	}

	public UserUri[] getOwnURIs() {
		return userUriArray;
	}
}
