package com.oppo.sfamanagement.database;

import android.database.Cursor;

public class Event {
	protected int id;
	protected String Time;
	protected String TimeIn;
	protected String TimeOut;
	protected String placeName;
	protected String Date;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String Date) {
		this.Date = Date;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String Time) {
		this.Time = Time;
	}

	public String getTimeOut() {
		return TimeOut;
	}

	public void setTimeOut(String TimeOut) {
		this.TimeOut = TimeOut;
	}
	public String getTimeIn() {
		return TimeIn;
	}

	public void setTimeIn(String TimeIn) {
		this.TimeIn = TimeIn;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

//	static public Event fromCursor(Cursor cursor) {
//		if (cursor != null && cursor.getCount() != 0) {
//			Event e = new Event();
//			e.setId(cursor.getInt(cursor.getColumnIndex(SqliteHelper.COLUMN_ID)));
//			e.setType(cursor.getString(cursor
//					.getColumnIndex(SqliteHelper.COLUMN_EVENT_TYPE)));
//			e.setDate(cursor.getString(cursor
//					.getColumnIndex(SqliteHelper.COLUMN_EVENT_DATE)));
//			e.setPlaceName(cursor.getString(cursor
//					.getColumnIndex(SqliteHelper.COLUMN_PLACE_NAME)));
//			return e;
//		}
//		return null;
//	}
}