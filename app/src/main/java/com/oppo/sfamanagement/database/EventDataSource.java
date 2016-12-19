package com.oppo.sfamanagement.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.oppo.sfamanagement.model.TimeInOutDetails;

import java.util.ArrayList;

public class EventDataSource {
	private String[] allColumns = { SqliteHelper.COLUMN_ID,
			SqliteHelper.COLUMN_USERNAME, SqliteHelper.COLUMN_CLOCKDATE,
			SqliteHelper.COLUMN_ACTIONTYPE, SqliteHelper.COLUMN_COMMENTS,
			SqliteHelper.COLUMN_LATITUDE, SqliteHelper.COLUMN_LONGITUDE,
			SqliteHelper.COLUMN_ACTIONIMAGE,SqliteHelper.COLUMN_ISPHUSHED, };
	private Context context;
	protected SQLiteDatabase database;
	protected SqliteHelper dbHelper;

	public EventDataSource(Context context) {
		this.context = context;
		initSqliteHelper();
		open();
	}

	protected void initSqliteHelper() {
		dbHelper = new SqliteHelper(context);
	}

	protected void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		database.close();
		dbHelper.close();
	}

	public void insertTimeInOutDetails(TimeInOutDetails data) {
		ContentValues values = new ContentValues();
		values.put(SqliteHelper.COLUMN_USERNAME, data.getUsername());
		values.put(SqliteHelper.COLUMN_CLOCKDATE, data.getClockDate());
		values.put(SqliteHelper.COLUMN_ACTIONTYPE, data.getActionType());
		values.put(SqliteHelper.COLUMN_COMMENTS, data.getComments());
		values.put(SqliteHelper.COLUMN_LATITUDE, data.getLatitude());
		values.put(SqliteHelper.COLUMN_LONGITUDE, data.getLongitude());
		values.put(SqliteHelper.COLUMN_ACTIONIMAGE, data.getActionImage());
		values.put(SqliteHelper.COLUMN_ISPHUSHED, data.getIsPushed());
		database.insert(SqliteHelper.TABLE_TIMEINOUT, null, values);
	}

	public ArrayList<TimeInOutDetails> getTimeInOutDetails() {
		ArrayList<TimeInOutDetails> list = new ArrayList<TimeInOutDetails>();
		String orderBy = SqliteHelper.COLUMN_CLOCKDATE + " ASC";
		Cursor cursor = database.query(SqliteHelper.TABLE_TIMEINOUT, allColumns,
				null, null, null, null, orderBy);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			list.add(TimeInOutDetails.fromCursor(cursor));
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return list;
	}
}
