package tongji.edu.db;

import tongji.edu.useless.OneRecordBean;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 管理数据库
 * 
 * @author zlj
 * 
 */
public class DBAdapter {

	private static final String DB_NAME = "tongjibus.db";
	private static final String DB_TABLE = "record";
	private static final int DB_VERSION = 1;

	public static final String KEY_ID = "id";
	public static final String KEY_get_ticket_time = "get_ticket_time";
	public static final String KEY_bus_time = "bus_time";
	public static final String KEY_route_name = "route_name";
	public static final String KEY_line = "line";
	public static final String KEY_username = "username";

	private SQLiteDatabase db;
	private final Context context;
	private DBOpenHelper dbOpenHelper;

	public DBAdapter(Context _context) {
		context = _context;
	}

	/** Close the database */
	public void close() {
		if (db != null) {
			db.close();
			db = null;
		}
	}

	/** Open the database */
	public void open() throws SQLiteException {
		dbOpenHelper = new DBOpenHelper(context, DB_NAME, null, DB_VERSION);
		try {
			db = dbOpenHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbOpenHelper.getReadableDatabase();
		}
	}

	/**
	 * 增加订票记录
	 * 
	 * @param record
	 * @return
	 */
	public long insert(OneRecordBean record) {
		ContentValues newValues = new ContentValues();

		newValues.put(KEY_get_ticket_time, record.getGet_ticket_time());
		newValues.put(KEY_bus_time, record.getBus_time());
		newValues.put(KEY_route_name, record.getRoute_name());
		newValues.put(KEY_line, record.getLine());
		newValues.put(KEY_username, record.getUsername());

		long tag = db.insert(DB_TABLE, null, newValues);
		return tag;
	}

	public OneRecordBean[] queryAllData() {
		Cursor results = db.query(DB_TABLE, new String[] { KEY_ID,
				KEY_get_ticket_time, KEY_bus_time, KEY_route_name, KEY_line,
				KEY_username }, null, null, null, null, null);
		return ConvertToRecord(results);
	}

	/**
	 * 根据用户名查找该用户的订票记录
	 * 
	 * @param motor_id
	 * @return
	 */
	public OneRecordBean[] queryByUsername(String username) {
		Cursor results = db.query(DB_TABLE, new String[] { KEY_ID,
				KEY_get_ticket_time, KEY_bus_time, KEY_route_name, KEY_line,
				KEY_username }, KEY_username + "=" + username, null, null,
				null, KEY_ID + " desc");
		return ConvertToRecord(results);
	}

	/**
	 * 根据ID查找到相应记录
	 * 
	 * @param ID
	 * @return
	 */
	public OneRecordBean[] queryByID(int ID) {
		Cursor results = db.query(DB_TABLE, new String[] { KEY_ID,
				KEY_get_ticket_time, KEY_bus_time, KEY_route_name, KEY_line,
				KEY_username }, KEY_ID + "=" + ID, null, null, null, null);
		return ConvertToRecord(results);
	}

	/**
	 * 得到数据库中ID最大的一项
	 * 
	 * @return
	 */
	public int getMax() {
		Cursor results = db.query(DB_TABLE, new String[] { KEY_ID,
				KEY_get_ticket_time, KEY_bus_time, KEY_route_name, KEY_line,
				KEY_username }, null, null, null, null, null);
		int max = 0;
		int resultCounts = results.getCount();
		if (resultCounts == 0 || !results.moveToFirst()) {
			return 0;
		}
		for (int i = 0; i < resultCounts; i++) {
			int temp = results.getInt(0);
			if (max < temp)
				max = temp;

			results.moveToNext();
		}
		return max;
	}

	private OneRecordBean[] ConvertToRecord(Cursor cursor) {
		int resultCounts = cursor.getCount();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		OneRecordBean[] records = new OneRecordBean[resultCounts];
		for (int i = 0; i < resultCounts; i++) {
			records[i] = new OneRecordBean();
			records[i].setId(cursor.getInt(0));
			records[i].setGet_ticket_time(cursor.getString(cursor
					.getColumnIndex(KEY_get_ticket_time)));
			records[i].setBus_time(cursor.getString(cursor
					.getColumnIndex(KEY_bus_time)));
			records[i].setRoute_name(cursor.getString(cursor
					.getColumnIndex(KEY_route_name)));
			records[i]
					.setLine(cursor.getString(cursor.getColumnIndex(KEY_line)));
			records[i].setUsername(cursor.getString(cursor
					.getColumnIndex(KEY_username)));

			cursor.moveToNext();
		}

		cursor.close();
		return records;
	}

	/**
	 * 删除全部订票记录
	 * 
	 * @return
	 */
	public long deleteAllData() {
		return db.delete(DB_TABLE, null, null);
	}

	/**
	 * 从数据库中删除指定的数据
	 * 
	 * @param id
	 * @return
	 */
	public int deleteOneData(int id) {
		return db.delete(DB_TABLE, KEY_ID + "=" + id, null);
	}

	/**
	 * 创建帮助类
	 * 
	 * @author Administrator
	 * 
	 */
	private static class DBOpenHelper extends SQLiteOpenHelper {

		public DBOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		private static final String DB_CREATE = "create table " + DB_TABLE
				+ " (" + KEY_ID + " integer primary key autoincrement, "
				+ KEY_get_ticket_time + " text not null, " + KEY_bus_time
				+ " text not null, " + KEY_route_name + " text not null, "
				+ KEY_line + " text not null, " + KEY_username
				+ " text not null);";

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DB_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
				int _newVersion) {
			_db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
			onCreate(_db);
		}
	}
}