package net.skweez.sipgate.model;

import java.util.Observable;

import net.skweez.sipgate.api.ISipgateAPI;
import net.skweez.sipgate.api.Price;
import net.skweez.sipgate.api.SipgateException;
import net.skweez.sipgate.api.xmlrpc.SipgateXmlRpcImpl;
import android.util.Log;

/**
 * @author Michael Kanis
 */
public class Balance extends Observable {

	private Price balance;

	public void startRefresh() {

		new Thread() {

			/** {@inheritDoc} */
			@Override
			public void run() {
				try {
					final ISipgateAPI sipgate = new SipgateXmlRpcImpl();
					setBalance(sipgate.getBalance());
				} catch (SipgateException e) {
					Log.e("Sipgate", "error", e);
					// TODO proper error handling!
				}
			}
		}.start();
	}

	private synchronized void setBalance(Price balance) {
		this.balance = balance;
		setChanged();
		notifyObservers();
	}

	public Price getBalance() {
		return balance;
	}

}
