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
package net.skweez.sipgate.api.xmlrpc;

import java.net.URI;
import java.util.Map;

import net.skweez.sipgate.api.ISipgateAPI;
import net.skweez.sipgate.api.SipgateException;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

/**
 * 
 * @author mks
 * @author $Author: mks $
 * @version $Rev: 9 $
 * @levd.rating RED Rev:
 */
public class SipgateXmlRpcImpl implements ISipgateAPI {

	private static final URI API_URI;

	static {
		API_URI = URI.create("https://samurai.sipgate.net/RPC2");
	}

	private final XMLRPCClient client;

	private final String password;

	private final String username;

	public SipgateXmlRpcImpl(String username, String password) {
		this.username = username;
		this.password = password;
		client = new XMLRPCClient(API_URI, username, password);
	}

	public Object getBalance() {
		return executeMethod("samurai.BalanceGet");
	}

	public Map<String, Object> executeMethod(String method, String... params) {
		try {
			return (Map<String, Object>) client.callEx(method, params);
		} catch (final XMLRPCException exception) {
			throw new SipgateException(exception);
		}
	}
}
