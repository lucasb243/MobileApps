package com.example.mobileapps;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.lang.reflect.Array;

public class SetListFilters extends Fragment {

    private GridView gridView;
    private Button btnYearFilter;
    private Button btnMonthFilter;
    private Button btnWeekFilter;
    private Button btnRevenueFilter;
    private Button btnExpeneseFilter;
    private Button btnNeutralFilter;
    private Button btnSbmtFilter;
    private Button btnResetFilter;
    private TextView tvShowAmountFilter;
    private SeekBar sbAmountFilters;
    private SharedViewModel viewModel;
    private SQLiteDatabase transactionDB;

    public SetListFilters(){
        super(R.layout.fragment_set_list_filters);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransactionDBHelper dbHelper = new TransactionDBHelper(getActivity());
        transactionDB = dbHelper.getWritableDatabase();

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_list_filters, container, false);

        setUpViews(view);
        setUpButtonOnclick();
        //addRandomTransactionItem()
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }


    private void setUpViews(View view){
        gridView            = view.findViewById(R.id.gvCategoryFilter );
        btnExpeneseFilter   = view.findViewById(R.id.btnExpenseFilter);
        btnMonthFilter      = view.findViewById(R.id.btnMonthFilter);
        btnWeekFilter       = view.findViewById(R.id.btnWeekFilter);
        btnNeutralFilter    = view.findViewById(R.id.btnNeutralFilter);
        btnRevenueFilter    = view.findViewById(R.id.btnRevenueFilter);
        btnSbmtFilter       = view.findViewById(R.id.btnSbmtFilter);
        btnResetFilter      = view.findViewById(R.id.btnResetFilter);
        btnYearFilter       = view.findViewById(R.id.btnYearFilter);
        sbAmountFilters     = view.findViewById(R.id.seekBar);
        tvShowAmountFilter  = view.findViewById(R.id.tvFilterAmount);
    }

    private void setUpButtonOnclick() {
        setOnClickAppearance(btnExpeneseFilter);
        setOnClickAppearance(btnMonthFilter);
        setOnClickAppearance(btnWeekFilter);
        setOnClickAppearance(btnNeutralFilter);
        setOnClickAppearance(btnRevenueFilter);
        setOnClickAppearance(btnYearFilter);

        btnSbmtFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setCurrentCursor(getAllExpenseItems());
                final FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });
        btnResetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setCurrentCursor(getAllItems());
                final FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });
    }

    private void setOnClickAppearance(Button button){
        button.setOnClickListener ( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                changeButtonAppearance(button);
            } });
    }

    private void changeButtonAppearance(Button button) {
        if(button.getTag() == "notPressed"){
            button.setBackgroundResource(R.drawable.rounded_select_button_pressed);
            button.setTextColor(Color.WHITE);
            button.setTag("pressed");
        }else{
            button.setBackgroundResource(R.drawable.rounded_select_button);
            button.setTextColor(ContextCompat.getColor(getContext(),R.color.mainBlue));
            button.setTag("notPressed");
        }
    }

    private Cursor getAllExpenseItems(){
        return transactionDB.query(
                TransactionList.TransactionEntry.TABLE_NAME,
                null,
                "type= ?",
                new String[]{"e"},
                null,
                null,
                TransactionList.TransactionEntry._ID + " DESC");
    }
    private Cursor getAllItems(){
        return transactionDB.query(
                TransactionList.TransactionEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                TransactionList.TransactionEntry._ID + " DESC");
    }

/*    private void setUpSeekbar(){
        sbAmountFilters.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                tvShowAmountFilter.text = "< $i"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
                //Toast.makeText(context!!,"start tracking",Toast.LENGTH_SHORT).show()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
                //Toast.makeText(context!!,"stop tracking",Toast.LENGTH_SHORT).show()
                if(tvShowAmountFilter.text.toString()=="< 0"){
                    tvShowAmountFilter.text = "none"
                }
            }
        })
    }*/
}
