package net.skweez.sipgate.db;

import static net.skweez.sipgate.db.DatabaseHelper.COLUMN_BALANCE;
import static net.skweez.sipgate.db.DatabaseHelper.COLUMN_CALL_STATUS;
import static net.skweez.sipgate.db.DatabaseHelper.COLUMN_CURRENCY;
import static net.skweez.sipgate.db.DatabaseHelper.COLUMN_FIRSTNAME;
import static net.skweez.sipgate.db.DatabaseHelper.COLUMN_ID;
import static net.skweez.sipgate.db.DatabaseHelper.COLUMN_LASTNAME;
import static net.skweez.sipgate.db.DatabaseHelper.COLUMN_PHONE_NR;
import static net.skweez.sipgate.db.DatabaseHelper.COLUMN_REMOTE_URI;
import static net.skweez.sipgate.db.DatabaseHelper.COLUMN_SIPURI;
import static net.skweez.sipgate.db.DatabaseHelper.COLUMN_TIMESTAMP;
import static net.skweez.sipgate.db.DatabaseHelper.TABLE_ACCOUNTS;
import static net.skweez.sipgate.db.DatabaseHelper.TABLE_CALLS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.skweez.sipgate.api.Call;
import net.skweez.sipgate.api.ECallStatus;
import net.skweez.sipgate.api.Price;
import net.skweez.sipgate.api.UserName;
import net.skweez.sipgate.api.UserUri;
import net.skweez.sipgate.model.AccountInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class DataSource {

	private static final String TAG = DataSource.class.getSimpleName();

	private SQLiteDatabase database;

	private final DatabaseHelper databaseHelper;

	private final String[] allColumnsInCalls = { COLUMN_ID, COLUMN_TIMESTAMP,
			COLUMN_REMOTE_URI, COLUMN_CALL_STATUS };

	private final String[] allColumnsInAccounts = { COLUMN_ID, COLUMN_SIPURI,
			COLUMN_FIRSTNAME, COLUMN_LASTNAME, COLUMN_PHONE_NR, COLUMN_BALANCE,
			COLUMN_CURRENCY };

	public DataSource(Context context) {
		databaseHelper = new DatabaseHelper(context);
	}

	public void open(boolean readOnly) {
		if (readOnly) {
			database = databaseHelper.getReadableDatabase();
		} else {
			database = databaseHelper.getWritableDatabase();
		}
	}

	public void close() {
		databaseHelper.close();
	}

	public void removeAllCalls() {
		database.delete(TABLE_CALLS, null, null);
	}

	public void insertCalls(Collection<Call> calls) {
		for (Call call : calls) {
			insertCall(call);
		}
		Log.d(TAG, "Inserted " + calls.size() + " calls.");
	}

	public void insertCall(Call call) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_TIMESTAMP,
				Call.DATE_FORMAT.format(call.getTimestamp()));
		values.put(COLUMN_REMOTE_URI, call.getRemoteURI().toString());
		values.put(COLUMN_CALL_STATUS, call.getStatus().toString());

		long id = database.insert(TABLE_CALLS, null, values);
		if (id == -1) {
			Log.e(TAG, "Call could not be inserted.");
		}
	}

	public List<Call> getAllCalls() {
		List<Call> calls = new ArrayList<Call>();

		Cursor cursor = getAllCallsCursor();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			calls.add(getCallFromCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();

		Log.d(TAG, "Loaded " + calls.size() + " calls");

		return calls;
	}

	public Cursor getAllCallsCursor() {
		Cursor cursor = database.query(TABLE_CALLS, allColumnsInCalls, null,
				null, null, null, null);
		return cursor;
	}

	public static Call getCallFromCursor(Cursor cursor) {
		Call call = new Call();
		call.setTimestamp(cursor.getString(1));
		call.setRemoteURI(Uri.parse(cursor.getString(2)));
		call.setStatus(ECallStatus.valueOf(cursor.getString(3)));
		return call;
	}

	public void insertAccountInfo(AccountInfo info) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, Integer.valueOf(info.getCustomerNumber()));
		values.put(COLUMN_FIRSTNAME, info.getUserName().getFirstName());
		values.put(COLUMN_LASTNAME, info.getUserName().getLastName());
		values.put(COLUMN_SIPURI, info.getDefaultUserUri().getSipUri()
				.toString());
		values.put(COLUMN_PHONE_NR, info.getPhoneNumber());
		values.put(COLUMN_BALANCE, info.getBalance().getAmount());
		values.put(COLUMN_CURRENCY, info.getBalance().getCurrencyString());

		long id = database.insert(DatabaseHelper.TABLE_ACCOUNTS, null, values);
		if (id == -1) {
			Log.e(TAG, "Account could not be inserted.");
		}
	}

	public AccountInfo getAccountInfo() {
		AccountInfo info = null;

		Cursor c = database.query(DatabaseHelper.TABLE_ACCOUNTS,
				allColumnsInAccounts, null, null, null, null, null);

		if (c.moveToFirst()) {
			info = new AccountInfo();
			info.setDefaultUserUri(new UserUri(c.getString(4), Uri.parse(c
					.getString(1)), true));
			info.setUserName(new UserName(c.getString(2), c.getString(3)));
			info.setBalance(new Price(c.getDouble(5), c.getString(6)));
		}

		c.close();

		return info;
	}

	public void updateAccountBalance(AccountInfo info) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COLUMN_BALANCE, info.getBalance().getAmount());

		// Don't forget to change the where clause, if you want to support
		// multiple accounts
		database.update(TABLE_ACCOUNTS, values, null, null);
	}
}
