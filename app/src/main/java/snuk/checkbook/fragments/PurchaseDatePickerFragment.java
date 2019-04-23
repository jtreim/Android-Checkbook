package snuk.checkbook.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import snuk.checkbook.R;
import snuk.checkbook.db.Purchase;

public class PurchaseDatePickerFragment extends DialogFragment {

	private Calendar calendar = null;
	private EditText dateText = null;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		dateText = (EditText)getActivity().findViewById(R.id.add_purchase_date);
		calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
	}


	private DatePickerDialog.OnDateSetListener dateSetListener =
		new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int month, int day){
				calendar.set(year, month, day);
				Date date = calendar.getTime();
				if(dateText != null) {
					dateText.setText(Purchase.DATE_FORMAT.format(date));
				}
			}
	};
}
