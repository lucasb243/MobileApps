package com.example.mobileapps;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public TransactionAdapter(Context aContext, Cursor aCursor) {
        mCursor = aCursor;
        mContext = aContext;
    }


    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        public TextView amountText;
        public TextView dateText;
        public TextView categoryText;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            amountText = itemView.findViewById(R.id.tvAmount);
            dateText = itemView.findViewById(R.id.tvDate);
            categoryText = itemView.findViewById(R.id.tvCategory);
        }
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.transaction_list_item, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        String amount      = mCursor.getString(mCursor.getColumnIndex(TransactionList.TransactionEntry.COLUMN_AMOUNT));
        String date        = mCursor.getString(mCursor.getColumnIndex(TransactionList.TransactionEntry.COLUMN_CREATEDAT));
        String category    = mCursor.getString(mCursor.getColumnIndex(TransactionList.TransactionEntry.COLUMN_CATEGORIE));
        String type        = mCursor.getString(mCursor.getColumnIndex(TransactionList.TransactionEntry.COLUMN_TYPE));
        float _id          = mCursor.getFloat(mCursor.getColumnIndex(TransactionList.TransactionEntry._ID));

        amount = manipulateAmountView(holder, amount, type);
        amount = amount + "€";
        holder.amountText.setText(amount);
        holder.dateText.setText(date);
        holder.categoryText.setText(category);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    private String manipulateAmountView(TransactionViewHolder holder, String aAmount, String type){
        String amount=aAmount;
        if (type.equals("e")){
            holder.amountText.setTextColor(ContextCompat.getColor(mContext, R.color.expense));
            amount = "- "+amount;
        }else if (type.equals("ne")){
            holder.amountText.setTextColor(ContextCompat.getColor(mContext,R.color.mainYellow));
            amount = "- "+amount;
        }else if (type.equals("nr")){
            holder.amountText.setTextColor(ContextCompat.getColor(mContext,R.color.mainYellow));
            amount = "+ "+amount;
        }else{
            amount = "+ "+amount;
            holder.amountText.setTextColor(ContextCompat.getColor(mContext,R.color.revenue));
        }
        return amount;
    }
}
