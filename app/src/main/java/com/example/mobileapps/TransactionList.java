package com.example.mobileapps;

import android.provider.BaseColumns;

public class TransactionList {

    private TransactionList() {
    }

    public static final class TransactionEntry implements BaseColumns {
        public static final String TABLE_NAME = "transactionList";
        public static final String _ID = "_id";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_CATEGORIE = "category";
        public static final String COLUMN_NOTE = "note";
        public static final String COLUMN_CREATEDAT = "createdAt";
        public static final String COLUMN_EDITEDAT = "editedAt";
    }
}
