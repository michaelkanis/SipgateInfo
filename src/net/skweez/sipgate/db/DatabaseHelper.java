package net.skweez.sipgate.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String TABLE_CALLS = "calls";

	public static final String COLUMN_ID = "_id";

	public static final String COLUMN_TIMESTAMP = "timestamp";

	public static final String COLUMN_REMOTE_URI = "number";

	public static final String COLUMN_CALL_STATUS = "status";

	public static final String TABLE_ACCOUNTS = "accounts";

	public static final String COLUMN_FIRSTNAME = "name1";

	public static final String COLUMN_LASTNAME = "name2";

	public static final String COLUMN_SIPURI = "sipuri";

	public static final String COLUMN_PHONE_NR = "phone_nr";

	public static final String COLUMN_BALANCE = "balance";

	public static final String COLUMN_CURRENCY = "currency";

	private static final String DATABASE_NAME = "sipgate.db";

	private static final int DATABASE_VERSION = 8;

	// Database creation SQL statement
	private static final String TABLE_CALLS_CREATE = "create table "
			+ TABLE_CALLS + "( " + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_TIMESTAMP
			+ " text not null, " + COLUMN_REMOTE_URI + " text, "
			+ COLUMN_CALL_STATUS + " text);";
	private static final String TABLE_ACCOUNTS_CREATE = "create table "
			+ TABLE_ACCOUNTS + "( " + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_FIRSTNAME
			+ " text, " + COLUMN_LASTNAME + " text, " + COLUMN_PHONE_NR
			+ " text, " + COLUMN_SIPURI + " text, " + COLUMN_BALANCE
			+ " real, " + COLUMN_CURRENCY + " text" + ");";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(TABLE_CALLS_CREATE);
		database.execSQL(TABLE_ACCOUNTS_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALLS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
		onCreate(db);
	}

}
