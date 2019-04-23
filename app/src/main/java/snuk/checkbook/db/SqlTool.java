package snuk.checkbook.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

class SqlTool implements IDBTool {

	// Private Table Entry class
	private static final class PurchaseEntry implements BaseColumns {
		static final String TABLE_NAME = "purchase";
		static final String COLUMN_NAME_DATE = "date";
		static final String COLUMN_NAME_AMOUNT = "amount";
		static final String COLUMN_NAME_RECIPIENT = "rec";
		static final String COLUMN_NAME_NOTE = "note";
	}

	// Private DB helper for tool
	private class PurchaseDbHelper extends SQLiteOpenHelper {
		private static final int DB_VERSION = 1;
		private static final String DB_NAME = "Checkbook.db";
		private static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + PurchaseEntry.TABLE_NAME + " (" +
				PurchaseEntry._ID + " TEXT PRIMARY KEY," +
				PurchaseEntry.COLUMN_NAME_DATE + " TEXT," +
				PurchaseEntry.COLUMN_NAME_AMOUNT + " REAL," +
				PurchaseEntry.COLUMN_NAME_RECIPIENT + " TEXT," +
				PurchaseEntry.COLUMN_NAME_NOTE + " TEXT)";
		private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + PurchaseEntry.TABLE_NAME;

		PurchaseDbHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_CREATE_ENTRIES);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(SQL_DELETE_ENTRIES);
			onCreate(db);
		}
	}


	// Make the tool a singleton
	private static final SqlTool ourInstance = new SqlTool();
	static SqlTool getInstance() {
		return ourInstance;
	}
	private PurchaseDbHelper dbHelper;
	private HashMap<String, Purchase> data;
	private SqlTool() {
		data = new HashMap<>();
	}

	public void setDbHelper(Context context){
		dbHelper = new PurchaseDbHelper(context);
	}

	@Override
	public DBError load() {
		DBError err = null;
		if(dbHelper == null) {
			err = new DBError(DBError.DBErrorCode.NO_DB, "No DBHelper set. You must pass in context for this tool first.");

		} else {
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			boolean loaded = false;
			Cursor cursor = db.rawQuery("SELECT * from " + PurchaseEntry.TABLE_NAME, null);
			while(cursor.moveToNext()){
				String id = cursor.getString(0);
				String date_str = cursor.getString(1);
				Date date = null;
				try {
					date = Purchase.DATE_FORMAT.parse(date_str);
					double amount = cursor.getDouble(2);
					String rec = cursor.getString(3);
					String note = cursor.getString(4);

					data.put(id, new Purchase(id, date, amount, rec, note));
					loaded = true;
				} catch (ParseException e) {
					err = new DBError(DBError.DBErrorCode.INVALID_VALUE, "Unable to parse date format. Skipping...");
				}
			}
			cursor.close();

			if(!loaded){
				err = new DBError(DBError.DBErrorCode.NO_VALUE, "No data loaded from this db.");
			} else if (err == null) {
				err = new DBError(DBError.DBErrorCode.SUCCESS);
			}
		}

		return err;
	}

	@Override
	public DBError getPurchase(String id, Purchase result) {
		DBError err = null;
		if(!data.containsKey(id)){
			err = new DBError(DBError.DBErrorCode.NO_VALUE, "Unable to load purchase with id: " + id);
			result = null;
		}	else {
			err = new DBError(DBError.DBErrorCode.SUCCESS);
			result = data.get(id);
		}

		return err;
	}

	@Override
	public DBError getAllPurchases(Vector<Purchase> result) {
		DBError err = null;
		if(data.isEmpty()){
			err = new DBError(DBError.DBErrorCode.NO_VALUE, "No purchases have been loaded.");
		} else {
			err = new DBError(DBError.DBErrorCode.SUCCESS);
			result.clear();

			Iterator itr = data.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry pair = (Map.Entry)itr.next();
				result.add((Purchase)pair.getValue());
				itr.remove();
			}
		}

		return err;
	}

	@Override
	public DBError save() {
		DBError err = null;
		if(dbHelper == null) {
			err = new DBError(DBError.DBErrorCode.NO_DB, "No DBHelper set. You must pass in context for this tool first.");

		} else {
			// TODO: add or update all data into sqlite DB.
			err = new DBError(DBError.DBErrorCode.SUCCESS);
		}
		return err;
	}

	@Override
	public DBError addPurchase(Purchase p) {
		data.put(p.getID(), p);
		return new DBError(DBError.DBErrorCode.SUCCESS);
	}

	@Override
	public DBError updatePurchase(Purchase p) {
		if(!data.containsKey(p.getID())){
			return new DBError(DBError.DBErrorCode.NO_VALUE, "No purchase with that id found.");
		}

		data.put(p.getID(), p);
		return new DBError(DBError.DBErrorCode.SUCCESS);
	}

	@Override
	public DBError deletePurchase(Purchase p) {
		if(!data.containsKey(p.getID())){
			return new DBError(DBError.DBErrorCode.NO_VALUE, "No purchase with that id found.");
		}

		data.remove(p.getID());
		return new DBError(DBError.DBErrorCode.SUCCESS);
	}
}
