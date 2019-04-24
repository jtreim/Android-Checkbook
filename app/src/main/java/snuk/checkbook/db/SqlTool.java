package snuk.checkbook.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class SqlTool implements IDBTool {

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
	public static SqlTool getInstance() {
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
	public void load() throws DBError {
		DBError err = null;
		if(dbHelper == null) {
			throw new DBError(DBError.ErrorCode.NO_DB, "No DBHelper set. You must pass in context for this tool first.");

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
					e.printStackTrace();
				}
			}
			cursor.close();

			if(!loaded){
				throw new DBError(DBError.ErrorCode.NO_VALUE, "No data loaded from this db.");
			}
		}
	}

	@Override
	public Purchase getPurchase(String id) throws DBError {
		Purchase result;

		if(!data.containsKey(id)){
			throw new DBError(DBError.ErrorCode.NO_VALUE, "Unable to load purchase with id: " + id);
		}	else {
			result = data.get(id);
		}

		return result;
	}

	@Override
	public Vector<Purchase> getAllPurchases() throws DBError {
		Vector<Purchase> result = new Vector<>();
		if(data.isEmpty()){
			throw new DBError(DBError.ErrorCode.NO_VALUE, "No purchases have been loaded.");
		} else {
			Iterator itr = data.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry pair = (Map.Entry)itr.next();
				result.add((Purchase)pair.getValue());
				itr.remove();
			}
		}

		return result;
	}

	@Override
	public void save() throws DBError {
		if(dbHelper == null) {
			throw new DBError(DBError.ErrorCode.NO_DB, "No DBHelper set. You must pass in context for this tool first.");

		} else {
			// TODO: add or update all data into sqlite DB.
		}
	}

	@Override
	public void addPurchase(Purchase p) throws DBError {
		if(p.getAmount() == 0){
			throw new DBError(DBError.ErrorCode.INVALID_VALUE, "Amount for new purchase can\'t be 0.");
		} else if(p.getDate() == null){
			throw new DBError(DBError.ErrorCode.INVALID_VALUE, "Missing date for new purchase.");
		} else if(data.containsKey(p.getID())){
			throw new DBError(DBError.ErrorCode.INVALID_VALUE, "Duplicate ID for new purchase.");
		}

		data.put(p.getID(), p);
	}

	@Override
	public void updatePurchase(Purchase p) throws DBError {
		if(!data.containsKey(p.getID())){
			throw new DBError(DBError.ErrorCode.NO_VALUE, "No purchase with that id found.");
		}

		data.put(p.getID(), p);
	}

	@Override
	public void deletePurchase(String id) throws DBError {
		if(!data.containsKey(id)){
			throw new DBError(DBError.ErrorCode.NO_VALUE, "No purchase with that id found.");
		}

		data.remove(id);
	}
}
