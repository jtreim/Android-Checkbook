package snuk.checkbook.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;

import snuk.checkbook.R;
import snuk.checkbook.db.DBError;
import snuk.checkbook.db.IDBTool;
import snuk.checkbook.db.Purchase;
import snuk.checkbook.db.SqlTool;
import snuk.checkbook.fragments.PurchaseDatePickerFragment;

public class AddPurchaseActivity extends AppCompatActivity {

	IDBTool dbTool = SqlTool.getInstance();

	EditText dateEditText, amountEditText, recEditText, noteEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_purchase);

		android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		dateEditText = findViewById(R.id.add_purchase_date);
		amountEditText = findViewById(R.id.add_purchase_amount);
		recEditText = findViewById(R.id.add_purchase_rec);
		noteEditText = findViewById(R.id.add_purchase_note);
	}

	@Override
	public boolean onSupportNavigateUp() {
		return exit();
	}

	public boolean exit(){
		onBackPressed();
		return true;
	}

	public void showDatePicker(View v){
		DialogFragment newFragment = new PurchaseDatePickerFragment();
		newFragment.show(getSupportFragmentManager(), "date picker");
	}

	public void cancel(View v){
		exit();
	}

	public void save(View v){
		try {
			Date date = Purchase.DATE_FORMAT.parse(dateEditText.getText().toString());
			double amount = Double.parseDouble(amountEditText.getText().toString());
			String recipient = recEditText.getText().toString();
			String note = noteEditText.getText().toString();
			Purchase to_add = new Purchase(date, amount, recipient, note);
			dbTool.addPurchase(to_add);
			Toast.makeText(getApplicationContext(), "Purchase added.", Toast.LENGTH_SHORT).show();

			exit();
		} catch (ParseException e) {
			Toast.makeText(getApplicationContext(), "Enter a valid date", Toast.LENGTH_LONG).show();
		} catch (NumberFormatException e){
			Toast.makeText(getApplicationContext(), "Enter a valid amount", Toast.LENGTH_LONG).show();
		} catch (DBError dbError) {
			if(dbError.code == DBError.ErrorCode.INVALID_VALUE){
				Toast.makeText(getApplicationContext(), dbError.msg, Toast.LENGTH_SHORT).show();
			}
			dbError.printStackTrace();
		}
	}
}
