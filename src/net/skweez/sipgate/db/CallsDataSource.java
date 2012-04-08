package net.skweez.sipgate.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.skweez.sipgate.api.Call;
import net.skweez.sipgate.api.ECallStatus;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class CallsDataSource {

	private static final String TAG = "CallsDataSource";

	private SQLiteDatabase database;

	private final DatabaseHelper databaseHelper;

	private final String[] allColumns = { DatabaseHelper.COLUMN_ID,
			DatabaseHelper.COLUMN_TIMESTAMP, DatabaseHelper.COLUMN_REMOTE_URI,
			DatabaseHelper.COLUMN_CALL_STATUS };

	public CallsDataSource(Context context) {
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
		database.delete(DatabaseHelper.TABLE_CALLS, null, null);
	}

	public void insertCalls(Collection<Call> calls) {
		for (Call call : calls) {
			insertCall(call);
		}
		Log.d(TAG, "Inserted " + calls.size() + " calls.");
	}

	public void insertCall(Call call) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COLUMN_TIMESTAMP,
				Call.DATE_FORMAT.format(call.getTimestamp()));
		values.put(DatabaseHelper.COLUMN_REMOTE_URI, call.getRemoteURI()
				.toString());
		values.put(DatabaseHelper.COLUMN_CALL_STATUS, call.getStatus()
				.toString());
		long insertId = database.insert(DatabaseHelper.TABLE_CALLS, null,
				values);

		if (insertId == -1) {
			Log.e(TAG, "Call could not be inserted.");
		}
	}

	public List<Call> getAllCalls() {
		List<Call> calls = new ArrayList<Call>();

		Cursor cursor = getAllCallsCursor();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			calls.add(callFromCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();

		Log.d(TAG, "Loaded " + calls.size() + " calls");

		return calls;
	}

	public Cursor getAllCallsCursor() {
		Cursor cursor = database.query(DatabaseHelper.TABLE_CALLS, allColumns,
				null, null, null, null, null);
		return cursor;
	}

	public static Call callFromCursor(Cursor cursor) {
		Call call = new Call();
		call.setTimestamp(cursor.getString(1));
		call.setRemoteURI(Uri.parse(cursor.getString(2)));
		call.setStatus(ECallStatus.valueOf(cursor.getString(3)));
		return call;
	}
}
