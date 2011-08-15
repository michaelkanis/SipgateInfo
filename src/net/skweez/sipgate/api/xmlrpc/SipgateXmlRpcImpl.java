package net.skweez.sipgate.api.xmlrpc;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import net.skweez.sipgate.api.AuthenticationException;
import net.skweez.sipgate.api.Call;
import net.skweez.sipgate.api.ECallStatus;
import net.skweez.sipgate.api.Gender;
import net.skweez.sipgate.api.ISipgateAPI;
import net.skweez.sipgate.api.Price;
import net.skweez.sipgate.api.SipgateException;
import net.skweez.sipgate.api.UserName;
import net.skweez.sipgate.api.UserUri;

import org.apache.http.HttpStatus;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

/**
 * @author Michael Kanis
 */
public class SipgateXmlRpcImpl implements ISipgateAPI {

	private static final URI API_URI;

	static {
		API_URI = URI.create("https://samurai.sipgate.net/RPC2");
	}

	/** {@inheritDoc} */
	public Price getBalance() {
		Map<String, Map> result = (Map<String, Map>) executeMethod("samurai.BalanceGet");
		Map currentBalance = result.get("CurrentBalance");

		return new Price((Double) currentBalance.get("TotalIncludingVat"),
				(String) currentBalance.get("Currency"));
	}

	public List<Call> getHistoryByDate() {
		List<Call> callList = new ArrayList<Call>();

		Map result = executeMethod("samurai.HistoryGetByDate");
		Object[] history = (Object[]) result.get("History");

		for (Object object : history) {
			callList.add(createCallFromMap((Map) object));
		}

		Collections.sort(callList, new Comparator<Call>() {
			public int compare(Call call1, Call call2) {
				// Sort the list in reverse order (-1), so that the newest call
				// comes first
				return call1.getTimestamp().compareTo(call2.getTimestamp())
						* (-1);
			}
		});

		return callList;
	}

	private Call createCallFromMap(Map map) {
		Call call = new Call();

		call.setLocalURI(SipgateUriHelper.createUriFromString((String) map
				.get("LocalUri")));
		call.setRemoteURI(SipgateUriHelper.createUriFromString((String) map
				.get("RemoteUri")));
		call.setStatus(ECallStatus.fromString((String) map.get("Status")));
		call.setTimestamp((String) map.get("Timestamp"));

		return call;
	}

	public UserUri[] getUserUriList() {
		Map<String, Object> result = (Map<String, Object>) executeMethod("samurai.OwnUriListGet");

		Object[] userUriMap = (Object[]) result.get("OwnUriList");
		UserUri[] userUriList = new UserUri[userUriMap.length];

		for (int i = 0; i < userUriMap.length; i++) {
			Map entry = (Map) userUriMap[i];

			userUriList[i] = new UserUri(entry.get("E164Out").toString(),
					SipgateUriHelper.createUriFromString(entry.get("SipUri")
							.toString()), new Boolean(entry.get("DefaultUri")
							.toString()));
		}
		return userUriList;
	}

	public UserName getUserName() {
		Map<String, String> result = (Map<String, String>) executeMethod("samurai.UserdataGreetingGet");

		return new UserName(result.get("FirstName"), result.get("LastName"),
				Gender.fromString(result.get("Gender")));
	}

	private Map<String, ? extends Object> executeMethod(String method,
			String... params) {

		try {
			return (Map<String, Object>) getAuthenticatedClient().callEx(
					method, params);
		} catch (final XMLRPCException exception) {
			
			// Sorry, this uses a modified version of XMLRPC :-(
			// see http://code.google.com/p/android-xmlrpc/issues/detail?id=30
			if (exception.getHttpStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
				throw new AuthenticationException("Wrong username or password.");
			}
			
			throw new SipgateException(exception);
		}
	}

	private XMLRPCClient getAuthenticatedClient()
			throws AuthenticationException {

		PasswordAuthentication authentication = Authenticator
				.requestPasswordAuthentication(null, 80, "http", null, null);

		if (authentication != null) {
			String username = authentication.getUserName();
			String password = String.valueOf(authentication.getPassword());

			return new XMLRPCClient(API_URI, username, password);
		} else {
			throw new AuthenticationException("Please set up your username and password first.");
		}
	}
}
