package com.example.mobileapps;

import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfileFragment extends Fragment {
    SQLiteDatabase transanctionDB;
    //TransactionAdapter adapter;
    ImageButton filterBtn;
    FloatingActionButton flActBtn;
    //SharedViewModel viewModel;

    public ProfileFragment() {
        super(R.layout.fragment_profile);
        //transanctionDB = new SQLiteDatabase();

        //adapter = new TransactionAdapter;

    }
}
