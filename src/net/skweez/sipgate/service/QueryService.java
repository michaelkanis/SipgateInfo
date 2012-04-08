package net.skweez.sipgate.service;

import java.util.List;

import net.skweez.sipgate.api.AuthenticationException;
import net.skweez.sipgate.api.Call;
import net.skweez.sipgate.api.ISipgateAPI;
import net.skweez.sipgate.api.xmlrpc.SipgateXmlRpcImpl;
import net.skweez.sipgate.db.DataSource;
import net.skweez.sipgate.model.AccountInfo;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class QueryService extends IntentService {

	public static final int STATUS_RUNNING = 0;

	public static final int STATUS_UPDATED_CALLS = 1;

	public static final int STATUS_ERROR = 2;

	public static final int STATUS_UPDATED_ACCOUNT = 3;

	public static final int STATUS_NOT_AUTHENTICATED = 4;

	public static final int STATUS_FINISHED = 5;

	public QueryService() {
		super("QueryService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		String command = intent.getStringExtra("command");
		Bundle bundle = new Bundle();
		if (command.equals("query")) {
			receiver.send(STATUS_RUNNING, Bundle.EMPTY);
			DataSource dataSource = new DataSource(getApplicationContext());
			try {
				final ISipgateAPI sipgate = new SipgateXmlRpcImpl();
				dataSource.open(false);

				queryAccount(sipgate, dataSource);
				receiver.send(STATUS_UPDATED_ACCOUNT, bundle);

				queryCalls(sipgate, dataSource);
				receiver.send(STATUS_UPDATED_CALLS, bundle);
			} catch (Exception e) {
				Log.e("QueryService", "Error when updating", e);

				if (e instanceof AuthenticationException) {
					bundle.putString(Intent.EXTRA_TEXT, e.getMessage());
					receiver.send(STATUS_NOT_AUTHENTICATED, bundle);
				} else {
					bundle.putString(Intent.EXTRA_TEXT, e.toString());
					receiver.send(STATUS_ERROR, bundle);
				}
			} finally {
				dataSource.close();
			}
		}
		receiver.send(STATUS_FINISHED, Bundle.EMPTY);
		this.stopSelf();
	}

	private void queryCalls(final ISipgateAPI sipgate,
			final DataSource dataSource) {
		List<Call> calls = sipgate.getHistoryByDate();
		dataSource.removeAllCalls();
		dataSource.insertCalls(calls);
	}

	private void queryAccount(final ISipgateAPI sipgate, DataSource dataSource) {
		AccountInfo account = dataSource.getAccountInfo();
		Log.d("QueryService", "Account: " + account);
		if (account == null) {
			// Initial loading of complete account data
			account = new AccountInfo();
			account.setUserName(sipgate.getUserdataGreeting());
			account.setDefaultUserUri(sipgate.getOwnUriList());
			account.setBalance(sipgate.getBalance());
			dataSource.insertAccountInfo(account);
		} else {
			// Only update balance
			account.setBalance(sipgate.getBalance());
			dataSource.updateAccountBalance(account);
		}
	}
}
