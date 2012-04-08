package net.skweez.sipgate;

import java.util.List;

import net.skweez.sipgate.api.Call;
import net.skweez.sipgate.api.ISipgateAPI;
import net.skweez.sipgate.api.xmlrpc.SipgateXmlRpcImpl;
import net.skweez.sipgate.db.CallsDataSource;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

public class QueryService extends IntentService {

	public static final int STATUS_RUNNING = 0;

	public static final int STATUS_UPDATED_CALLS = 1;

	public static final int STATUS_ERROR = 2;

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
			try {
				final ISipgateAPI sipgate = new SipgateXmlRpcImpl();
				queryCalls(sipgate);
				receiver.send(STATUS_UPDATED_CALLS, bundle);
			} catch (Exception e) {
				bundle.putString(Intent.EXTRA_TEXT, e.toString());
				receiver.send(STATUS_ERROR, bundle);
			}
		}
		this.stopSelf();
	}

	private void queryCalls(final ISipgateAPI sipgate) {
		List<Call> calls = sipgate.getHistoryByDate();
		CallsDataSource dataSource = new CallsDataSource(
				getApplicationContext());
		dataSource.open(false);
		dataSource.removeAllCalls();
		dataSource.insertCalls(calls);
		dataSource.close();
	}
}
