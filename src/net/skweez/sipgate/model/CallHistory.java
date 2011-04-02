package net.skweez.sipgate.model;

import java.util.List;
import java.util.Observable;

import net.skweez.sipgate.api.Call;
import net.skweez.sipgate.api.ISipgateAPI;
import net.skweez.sipgate.api.SipgateException;
import net.skweez.sipgate.api.xmlrpc.SipgateXmlRpcImpl;
import android.util.Log;

public class CallHistory extends Observable {

	private List<Call> callHistory;

	public void startRefresh() {

		new Thread() {

			/** {@inheritDoc} */
			@Override
			public void run() {
				try {
					final ISipgateAPI sipgate = new SipgateXmlRpcImpl();
					setCallHistory(sipgate.getHistoryByDate());
				} catch (SipgateException e) {
					Log.e("Sipgate", "error", e);
					// TODO proper error handling!
				}
			}
		}.start();
	}

	private void setCallHistory(List<Call> callHistory) {
		this.callHistory = callHistory;
		setChanged();
		notifyObservers();
	}

	public Call getCall(int index) {
		return callHistory.get(index);
	}

	public int size() {
		if (callHistory == null) {
			return 0;
		}

		return callHistory.size();
	}

}
