package com.example.mobileapps;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class SetListFilters extends Fragment {

    private GridView gridView;
    private Button btnYearFilter;
    private Button btnMonthFilter;
    private Button btnWeekFilter;
    private Button btnRevenueFilter;
    private Button btnExpenseFilter;
    private Button btnNeutralFilter;
    private Button btnSbmtFilter;
    private Button btnResetFilter;
    private TextView tvShowAmountFilter;
    private SeekBar sbAmountFilters;
    private SharedViewModel viewModel;
    private SQLiteDatabase transactionDB;

    public SetListFilters() {
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


    private void setUpViews(View view) {
        gridView = view.findViewById(R.id.gvCategoryFilter);
        btnExpenseFilter = view.findViewById(R.id.btnExpenseFilter);
        btnMonthFilter = view.findViewById(R.id.btnMonthFilter);
        btnWeekFilter = view.findViewById(R.id.btnWeekFilter);
        btnNeutralFilter = view.findViewById(R.id.btnNeutralFilter);
        btnRevenueFilter = view.findViewById(R.id.btnRevenueFilter);
        btnSbmtFilter = view.findViewById(R.id.btnSbmtFilter);
        btnResetFilter = view.findViewById(R.id.btnResetFilter);
        btnYearFilter = view.findViewById(R.id.btnYearFilter);
        sbAmountFilters = view.findViewById(R.id.seekBar);
        tvShowAmountFilter = view.findViewById(R.id.tvFilterAmount);
        setUpSeekBar();
    }

    private void setUpButtonOnclick() {
        setOnClickAppearance(btnExpenseFilter);
        setOnClickAppearance(btnMonthFilter);
        setOnClickAppearance(btnWeekFilter);
        setOnClickAppearance(btnNeutralFilter);
        setOnClickAppearance(btnRevenueFilter);
        setOnClickAppearance(btnYearFilter);

        btnSbmtFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setCurrentCursor(getAppliedFilters());
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

    private void setOnClickAppearance(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableOtherButtons(v);
            }
        });
    }

    private void disableOtherButtons(View view){
        if(view.getId()==R.id.btnExpenseFilter){
            changeButtonAppearance(btnExpenseFilter);
            if(btnRevenueFilter.getTag()=="pressed"){
                changeButtonAppearance(btnRevenueFilter);
            }
            if(btnNeutralFilter.getTag()=="pressed"){
                changeButtonAppearance(btnNeutralFilter);
            }
        }
        if(view.getId()==R.id.btnRevenueFilter){
            changeButtonAppearance(btnRevenueFilter);
            if(btnExpenseFilter.getTag()=="pressed"){
                changeButtonAppearance(btnExpenseFilter);
            }
            if(btnNeutralFilter.getTag()=="pressed"){
                changeButtonAppearance(btnNeutralFilter);
            }
        }
        if(view.getId()==R.id.btnNeutralFilter){
            changeButtonAppearance(btnNeutralFilter);
            if(btnRevenueFilter.getTag()=="pressed"){
                changeButtonAppearance(btnRevenueFilter);
            }
            if(btnExpenseFilter.getTag()=="pressed"){
                changeButtonAppearance(btnExpenseFilter);
            }
        }
        if(view.getId()==R.id.btnYearFilter){
            changeButtonAppearance(btnYearFilter);
            if(btnMonthFilter.getTag()=="pressed"){
                changeButtonAppearance(btnMonthFilter);
            }
            if(btnWeekFilter.getTag()=="pressed"){
                changeButtonAppearance(btnWeekFilter);
            }
        }
        if(view.getId()==R.id.btnWeekFilter){
            changeButtonAppearance(btnWeekFilter);
            if(btnMonthFilter.getTag()=="pressed"){
                changeButtonAppearance(btnMonthFilter);
            }
            if(btnYearFilter.getTag()=="pressed"){
                changeButtonAppearance(btnYearFilter);
            }
        }
        if(view.getId()==R.id.btnMonthFilter){
            changeButtonAppearance(btnMonthFilter);
            if(btnYearFilter.getTag()=="pressed"){
                changeButtonAppearance(btnYearFilter);
            }
            if(btnWeekFilter.getTag()=="pressed"){
                changeButtonAppearance(btnWeekFilter);
            }
        }
    }
    private void changeButtonAppearance(Button button) {
        if (button.getTag() == "notPressed") {
            button.setBackgroundResource(R.drawable.rounded_select_button_pressed);
            button.setTextColor(Color.WHITE);
            button.setTag("pressed");
        } else {
            button.setBackgroundResource(R.drawable.rounded_select_button);
            button.setTextColor(ContextCompat.getColor(getContext(), R.color.mainBlue));
            button.setTag("notPressed");
        }
    }

    private Cursor getAllExpenseItems() {
        return transactionDB.query(
                TransactionList.TransactionEntry.TABLE_NAME,
                null,
                "type= ?",
                new String[]{"e"},
                null,
                null,
                TransactionList.TransactionEntry._ID + " DESC");
    }

    private Cursor getAllItems() {
        return transactionDB.query(
                TransactionList.TransactionEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "createdAt DESC, "+TransactionList.TransactionEntry._ID + " DESC");
    }

    private Cursor getAppliedFilters() {

        String whereClause = "";
        LinkedList<String> selectionArguments = new LinkedList<>();
        String[] dates = getNextDates();
        ArrayList<String> selectedTimeSpanstest = new ArrayList<String>();
        Button[] timeSpanButtons = {btnWeekFilter,btnMonthFilter,btnYearFilter};
        Button[] transCategoryButtons = {btnRevenueFilter,btnExpenseFilter,btnNeutralFilter};

        int amount = 0;
        for(int i=0; i<3; i++){
            if(timeSpanButtons[i].getTag()=="pressed"){
                if(whereClause==""){
                    whereClause+= "createdAt > ?";
                }else{
                    whereClause+=" AND createdAt > ?";
                }
                selectionArguments.add(dates[i]);
            }
        }
        for(int i=0; i<3; i++){
            if(transCategoryButtons[i].getTag()=="pressed") {
                if (whereClause == "") {
                    whereClause += "type = ?";
                } else {
                    whereClause += " AND type = ?";
                }
                switch (i) {
                    case 0:
                        selectionArguments.add("r");
                        break;
                    case 1:
                        selectionArguments.add("e");
                        break;
                    case 2:
                        whereClause += " OR type = ?";selectionArguments.add("ne");selectionArguments.add("nr");
                        break;
                    default:
                        break;
                }
            }
        }
        if(sbAmountFilters.getProgress()!=0){
            if (whereClause == "") {
                whereClause += "amount < ?";
            } else {
                whereClause += " AND amount < ?";
            }
             int amountHelper = sbAmountFilters.getProgress();
            selectionArguments.add(Integer.toString(amountHelper));
        }

        //String[] columns = {"_id","type", "amount", "category", "note", "substr(createdAt,7)||substr(createdAt,4,2)||substr(createdAt,1,2) as 'ca'", "editedAt"};
        return transactionDB.query(
                TransactionList.TransactionEntry.TABLE_NAME,
                null,
                whereClause,
                selectionArguments.toArray(new String[selectionArguments.size()]),
                null,
                null,
                "createdAt DESC, "+TransactionList.TransactionEntry._ID + " DESC");
    }

    private String[] getNextDates(){
        String dt = new SimpleDateFormat("yyyy-MM-dd").format(new Date());;  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        Calendar c3 = Calendar.getInstance();

        String date1 =null;
        String date2 =null;
        String date3 =null;

        try {
            c1.setTime(sdf.parse(dt));
            c2.setTime(sdf.parse(dt));
            c3.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c1.add(Calendar.DATE, -7);  // number of days to add
        date1 = sdf.format(c1.getTime());
        c2.add(Calendar.DATE, -30);  // number of days to add
        date2 = sdf.format(c2.getTime());
        c3.add(Calendar.DATE, -365);  // number of days to add
        date3 = sdf.format(c3.getTime());
        String[] dates = {date1, date2, date3};
        return dates;
    }

    private void setUpSeekBar() {
        sbAmountFilters.setOnSeekBarChangeListener(new OnSeekBarChangeListener (){

            public void onProgressChanged (SeekBar seekBar, int progressValue, boolean fromUser) {
                String value = "< "+progressValue;
                tvShowAmountFilter.setText(value);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(tvShowAmountFilter.getText().toString()=="< 0"){tvShowAmountFilter.setText("none");}
            }
        });
    }
}