package net.skweez.sipgate.model;

import java.util.Observable;

import net.skweez.sipgate.api.ISipgateAPI;
import net.skweez.sipgate.api.Price;
import net.skweez.sipgate.api.SipgateException;
import net.skweez.sipgate.api.UserName;
import net.skweez.sipgate.api.UserUri;
import net.skweez.sipgate.api.xmlrpc.SipgateXmlRpcImpl;
import android.util.Log;

/**
 * @author Florian Mutter
 * @author Michael Kanis
 */
public class AccountInfo extends Observable {

	private UserName userName;

	private UserUri defaultUserUri;

	private Price balance;

	public void startRefresh() {

		new Thread() {

			/** {@inheritDoc} */
			@Override
			public void run() {
				try {
					final ISipgateAPI sipgate = new SipgateXmlRpcImpl();

					setUserName(sipgate.getUserName());
					setDefaultUserUri(sipgate.getUserUriList());
					setBalance(sipgate.getBalance());

					setChanged();
					notifyObservers();
				} catch (SipgateException e) {
					Log.e("Sipgate", "error", e);
					// TODO proper error handling!
				}
			}
		}.start();
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
	}

	public UserName getUserName() {
		return userName;
	}

	private synchronized void setUserName(UserName userName) {
		this.userName = userName;
	}

	public Price getBalance() {
		return balance;
	}

	private synchronized void setBalance(Price balance) {
		this.balance = balance;
	}

}
