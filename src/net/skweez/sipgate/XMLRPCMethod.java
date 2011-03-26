/*-----------------------------------------------------------------------+
 | sipgate Kontostand
 |                                                                       |
   $Id: codetemplates.xml 9 2009-03-06 10:09:52Z mks $            
 |                                                                       |
 | Copyright (c)  2004-2011 Technische Universitaet Muenchen             |
 |                                                                       |
 | Technische Universitaet Muenchen               #########  ##########  |
 | Institut fuer Informatik - Lehrstuhl IV           ##  ##  ##  ##  ##  |
 | Prof. Dr. Manfred Broy                            ##  ##  ##  ##  ##  |
 | Boltzmannstr. 3                                   ##  ##  ##  ##  ##  |
 | 85748 Garching bei Muenchen                       ##  ##  ##  ##  ##  |
 | Germany                                           ##  ######  ##  ##  |
 +-----------------------------------------------------------------------*/
package net.skweez.sipgate;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;
import org.xmlrpc.android.XMLRPCFault;

import android.os.Handler;
import android.util.Log;

/**
 * 
 * @author mks
 * @author $Author: mks $
 * @version $Rev: 9 $
 * @levd.rating RED Rev:
 */
public class XMLRPCMethod extends Thread {
	private final String method;
	private Object[] params;
	private final Handler handler;
	private final IXMLRPCMethodCallback callBack;
	private final XMLRPCClient client;

	public XMLRPCMethod(String method, XMLRPCClient client,
			IXMLRPCMethodCallback callBack) {

		this.method = method;
		this.client = client;
		this.callBack = callBack;

		handler = new Handler();
	}

	public void call() {
		call(null);
	}

	public void call(Object[] params) {
		this.params = params;
		start();
	}

	@Override
	public void run() {
		try {
			final Object result = client.callEx(method, params);
			handler.post(new Runnable() {
				public void run() {
					callBack.callFinished(result);
				}
			});
		} catch (final XMLRPCFault e) {
			handler.post(new Runnable() {
				public void run() {
					Log.d("Test", "error", e);
				}
			});
		} catch (final XMLRPCException e) {
			handler.post(new Runnable() {
				public void run() {
					Log.d("Test", "error", e);
				}
			});
		}
	}
}
