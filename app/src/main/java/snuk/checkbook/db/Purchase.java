package snuk.checkbook.db;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Purchase {
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

	private String mId;
	private Date mDate;
	private double mAmount;
	private String mRecipient, mNote;

	public Purchase(String id, Date date, double amount, String rec, String note){
		this.mId = id;
		this.mDate = date;
		this.mAmount = amount;
		this.mRecipient = rec;
		this.mNote = note;
	}

	// Constructors to allow default parameters
	public Purchase(Date date, double amount, String rec, String note){
		this(UUID.randomUUID().toString(), date, amount, rec, note);
	}
	public Purchase(double amount, String rec, String note){
		this(Calendar.getInstance().getTime(), amount, rec, note);
	}
	public Purchase(Date date, double amount, String rec){
		this(date, amount, rec, "");
	}
	public Purchase(double amount, String rec){
		this(Calendar.getInstance().getTime(), amount, rec, "");
	}

	public String getID() {
		return mId;
	}

	public void setID(String id) {
		this.mId = id;
	}

	public Date getDate() {
		return mDate;
	}
	public void setDate(Date date) {
		this.mDate = date;
	}
	public double getAmount() {
		return mAmount;
	}
	public void setAmount(double amount) {
		this.mAmount = amount;
	}
	public String getRecipient() {
		return mRecipient;
	}
	public void setRecipient(String recipient) {
		this.mRecipient = recipient;
	}
	public String getNote() {
		return mNote;
	}
	public void setNote(String note) {
		this.mNote = note;
	}

	@Override
	public String toString() {
		return "Purchase{" +
			"date=" + mDate.toString() +
			", amount=" + mAmount +
			", recipient='" + mRecipient + '\'' +
			", note='" + mNote + '\'' +
			'}';
	}
}
