package snuk.checkbook.db;

import android.content.Context;

import java.util.Vector;

public interface IDBTool {
	void load() throws DBError;
	Purchase getPurchase(String id) throws DBError;
	Vector<Purchase> getAllPurchases() throws DBError;
	void save() throws DBError;
	void addPurchase(Purchase p) throws DBError;
	void updatePurchase(Purchase p) throws DBError;
	void deletePurchase(String id) throws DBError;
}
