package snuk.checkbook.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder> {
	public static class PurchaseViewHolder extends RecyclerView.ViewHolder {
		public TextView dateText, amountText, recText, noteText;

		public PurchaseViewHolder(@NonNull View itemView) {
			super(itemView);

		}
	}


	@NonNull
	@Override
	public PurchaseAdapter.PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		return null;
	}

	@Override
	public void onBindViewHolder(@NonNull PurchaseAdapter.PurchaseViewHolder purchaseViewHolder, int i) {

	}

	@Override
	public int getItemCount() {
		return 0;
	}
}
