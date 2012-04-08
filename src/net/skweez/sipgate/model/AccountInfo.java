package net.skweez.sipgate.model;

import java.util.List;
import java.util.Observable;

import net.skweez.sipgate.api.Price;
import net.skweez.sipgate.api.UserName;
import net.skweez.sipgate.api.UserUri;

/**
 * @author Florian Mutter
 * @author Michael Kanis
 */
public class AccountInfo extends Observable {

	private UserName userName;

	private UserUri defaultUserUri;

	private Price balance;

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

	public void setDefaultUserUri(List<UserUri> uris) {
		for (int i = 0; i < uris.size(); i++) {
			if (uris.get(i).isDefaultUri())
				defaultUserUri = uris.get(i);
		}
		// if there is no default userUri, we use the first one in the array
		defaultUserUri = uris.get(0);
		setChanged();
	}

	public void setDefaultUserUri(UserUri uri) {
		defaultUserUri = uri;
	}

	public UserName getUserName() {
		return userName;
	}

	public void setUserName(UserName userName) {
		this.userName = userName;
		setChanged();
	}

	public Price getBalance() {
		return balance;
	}

	public void setBalance(Price balance) {
		this.balance = balance;
		setChanged();
	}
}
