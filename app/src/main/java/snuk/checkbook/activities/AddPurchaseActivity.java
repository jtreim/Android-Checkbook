package snuk.checkbook.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toolbar;

import snuk.checkbook.R;
import snuk.checkbook.fragments.PurchaseDatePickerFragment;

public class AddPurchaseActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_purchase);

		android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}

	public void showDatePicker(View v){
		DialogFragment newFragment = new PurchaseDatePickerFragment();
		newFragment.show(getSupportFragmentManager(), "date picker");
	}

	public boolean close(View v){
		onBackPressed();
		return true;
	}
}
