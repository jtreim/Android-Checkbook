package snuk.checkbook.db;

import android.content.Context;

import java.util.Vector;

public interface IDBTool {
	DBError load();
	DBError getPurchase(String id, Purchase result);
	DBError getAllPurchases(Vector<Purchase> result);
	DBError save();
	DBError addPurchase(Purchase p);
	DBError updatePurchase(Purchase p);
	DBError deletePurchase(Purchase p);
}
