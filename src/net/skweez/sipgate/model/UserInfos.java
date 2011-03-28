package net.skweez.sipgate.model;

import java.util.Observable;

import android.util.Log;

import net.skweez.sipgate.api.ISipgateAPI;
import net.skweez.sipgate.api.OwnURI;
import net.skweez.sipgate.api.SipgateException;
import net.skweez.sipgate.api.xmlrpc.SipgateXmlRpcImpl;

public class UserInfos extends Observable {

	private OwnURI[] ownURIArray;
	
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
	
	private synchronized void setOwnURIs(OwnURI[] ownURIArray) {
		this.ownURIArray = ownURIArray;
	}
	
	public OwnURI[] getOwnURIs() {
		return ownURIArray;
	}
}
