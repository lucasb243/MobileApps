package com.example.mobileapps;

import android.content.ClipData;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Set;
import java.util.logging.Filter;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<Cursor> currentCursor = new MutableLiveData<>();

    private final LiveData<SQLiteDatabase> transactionDB = null;
    //private final LiveData<List<Cursor>> currentCursor = null;


    public LiveData<Cursor> getCurrentCursor() {
        return currentCursor;
    }

    public void setCurrentCursor(Cursor c) {
        currentCursor.setValue(c);
    }

    private Cursor getAllItems(){
        return transactionDB.getValue().query(
                TransactionList.TransactionEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                TransactionList.TransactionEntry._ID + " DESC");
    }

    public Cursor getStandardFilter(){
        return getAllItems();
    }
}