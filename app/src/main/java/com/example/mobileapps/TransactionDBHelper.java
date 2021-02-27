package com.example.mobileapps;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mobileapps.TransactionList.*;

public class TransactionDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "transactionList.db";
    public static final int DATABASE_VERSION = 1;

    public TransactionDBHelper(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TRANSACTION_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TransactionEntry.TABLE_NAME.toString() + " (" +
                TransactionEntry._ID.toString() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TransactionEntry.COLUMN_TYPE.toString() + " TEXT NOT NULL, " +
                TransactionEntry.COLUMN_AMOUNT.toString() + " INTEGER NOT NULL, " +
                TransactionEntry.COLUMN_CATEGORIE.toString() + ", " +
                TransactionEntry.COLUMN_NOTE.toString() + ", " +
                TransactionEntry.COLUMN_CREATEDAT.toString() +", "+ // " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                TransactionEntry.COLUMN_EDITEDAT.toString() + // TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        db.execSQL(SQL_CREATE_TRANSACTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TransactionEntry.TABLE_NAME);
        onCreate(db);
    }
}
